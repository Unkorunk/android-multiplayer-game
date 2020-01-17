package ru.timelimit.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashMap;

public class GameServer {
    public enum ActionClientEnum {
        SELECT_TARGET,
        UPDATE_LOBBY,
        UPDATE_GAME,
        CONNECT,
        FINISH
    }
    public enum ActionServerEnum {
        UPDATE_LOBBY,
        UPDATE_GAME,
        START_GAME,
        YOU_WIN,
        YOU_LOSE
    }

    public abstract static class Request {}
    public abstract static class Response {}

    public static class UpdateLobbyResponse extends Response {
        public int countInRoom;
        public int[] usersInRoom;
        public boolean gameInProcess;
    }
    public static class UpdateGameResponse extends Response {
        public int[] targetX;
        public int[] targetY;
    }

    public static class SelectTargetRequest extends Request {
        public int targetX;
        public int targetY;
    }

    public static class ActionClient {
        ActionClientEnum actionType;
        public Request request;
    }

    public static class ActionServer {
        public ActionServerEnum actionType;
        public Response response;
    }

    public static class User {
        int slotRoom;
        int targetX, targetY;
        public User() {
            slotRoom = -1;
            targetX = 0;
            targetY = 0;
        }
    }
    static int countInRoom = 0;
    static int[] usersInRoom = {-1, -1};
    static boolean gameInProcess = false;
    public static HashMap<Integer, User> users = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();

        server.getKryo().register(ru.timelimit.server.game.GameServer.ActionClientEnum.class);
        server.getKryo().register(ru.timelimit.server.game.GameServer.ActionClient.class);
        server.getKryo().register(ru.timelimit.server.game.GameServer.ActionServerEnum.class);
        server.getKryo().register(ru.timelimit.server.game.GameServer.ActionServer.class);
        server.getKryo().register(ru.timelimit.server.game.GameServer.Request.class);
        server.getKryo().register(ru.timelimit.server.game.GameServer.Response.class);
        server.getKryo().register(ru.timelimit.server.game.GameServer.UpdateLobbyResponse.class);
        server.getKryo().register(ru.timelimit.server.game.GameServer.UpdateGameResponse.class);
        server.getKryo().register(ru.timelimit.server.game.GameServer.SelectTargetRequest.class);

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
                System.out.println("connected: " + connection.getID());
                users.put(connection.getID(), new User());
            }

            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                if (object instanceof ActionClient) {
                    ActionClient actionClient = (ActionClient) object;

                    if (actionClient.actionType == ActionClientEnum.FINISH) {
                        if (users.get(connection.getID()).slotRoom != -1) {
                            usersInRoom[users.get(connection.getID()).slotRoom] = -1;
                            users.get(connection.getID()).slotRoom = -1;
                            countInRoom--;

                            ActionServer actionServer = new ActionServer();
                            actionServer.actionType = ActionServerEnum.YOU_WIN;
                            server.sendToTCP(connection.getID(), actionServer);

                            if (countInRoom == 0) {
                                server.sendToAllTCP(updateLobby());
                                gameInProcess = false;
                            }
                        }
                    } else if (actionClient.actionType == ActionClientEnum.CONNECT) {
                        if (countInRoom < 2 && !gameInProcess) {
                            users.get(connection.getID()).slotRoom = countInRoom;
                            usersInRoom[countInRoom] = connection.getID();
                            countInRoom++;

                            if (countInRoom == 2) {
                                gameInProcess = true;
                            }
                        }

                        if (gameInProcess) {
                            for (int id : usersInRoom) {
                                ActionServer actionServer = new ActionServer();
                                actionServer.actionType = ActionServerEnum.START_GAME;

                                server.sendToTCP(id, actionServer);
                            }
                        }

                        server.sendToAllTCP(updateLobby());
                    } else if (actionClient.actionType == ActionClientEnum.SELECT_TARGET) {
                        User user = users.get(connection.getID());
                        if (user.slotRoom == -1) {
                            System.out.println("kick: " + connection.getID());
                            users.remove(connection.getID());
                            connection.close();
                        } else {
                            SelectTargetRequest request = (SelectTargetRequest) actionClient.request;
                            user.targetX = request.targetX;
                            user.targetY = request.targetY;

                            updateGame(server);
                        }
                    } else if (actionClient.actionType == ActionClientEnum.UPDATE_LOBBY) {
                        connection.sendTCP(updateLobby());
                    } else if (actionClient.actionType == ActionClientEnum.UPDATE_GAME) {
                        User user = users.get(connection.getID());
                        if (user.slotRoom == -1) {
                            System.out.println("kick: " + connection.getID());
                            users.remove(connection.getID());
                            connection.close();
                        } else {
                            updateGame(server);
                        }
                    }

                }
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                System.out.println("disconnected: " + connection.getID());
                User user = users.get(connection.getID());
                if (user.slotRoom != -1) {
                    int otherUserId = usersInRoom[(user.slotRoom + 1) % 2];

                    countInRoom = 0;
                    usersInRoom[0] = -1;
                    usersInRoom[1] = -1;

                    ActionServer actionServer = new ActionServer();
                    actionServer.actionType = ActionServerEnum.YOU_WIN;

                    gameInProcess = false;

                    server.sendToTCP(otherUserId, actionServer);
                    server.sendToAllTCP(updateLobby());
                }
                users.remove(connection.getID());
            }
        });
        server.bind(25568);
    }

    private static ActionServer updateLobby() {
        ActionServer actionServer = new ActionServer();
        actionServer.actionType = ActionServerEnum.UPDATE_LOBBY;
        actionServer.response = new UpdateLobbyResponse();
        ((UpdateLobbyResponse)actionServer.response).countInRoom = countInRoom;
        ((UpdateLobbyResponse)actionServer.response).usersInRoom = usersInRoom;
        ((UpdateLobbyResponse)actionServer.response).gameInProcess = gameInProcess;
        return actionServer;
    }

    private static void updateGame(Server server) {
        ActionServer actionServer = new ActionServer();
        actionServer.actionType = ActionServerEnum.UPDATE_LOBBY;
        actionServer.response = new UpdateGameResponse();
        ((UpdateGameResponse)actionServer.response).targetX = new int[]{
                users.get(usersInRoom[0]).targetX, users.get(usersInRoom[1]).targetX,
        };
        ((UpdateGameResponse)actionServer.response).targetY = new int[]{
                users.get(usersInRoom[0]).targetY, users.get(usersInRoom[1]).targetY,
        };

        for (int value : usersInRoom) {
            server.sendToTCP(value, actionServer);
        }
    }
}