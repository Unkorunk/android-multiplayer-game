package ru.timelimit.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import ru.timelimit.network.*;

public class GameServer {
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
    static ArrayList<Trap> traps = new ArrayList<>();

    private static int preparationTime = 10 * 1000;
    private static Timer preparationTimer;

    public static HashMap<Integer, User> users = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();

        Network.register(server);

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
                                traps.clear();
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
                                actionServer.actionType = ActionServerEnum.START_PREPARATION;

                                server.sendToTCP(id, actionServer);
                            }

                            preparationTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    for (int id : usersInRoom) {
                                        ActionServer actionServer = new ActionServer();
                                        actionServer.actionType = ActionServerEnum.START_GAME;

                                        server.sendToTCP(id, actionServer);
                                    }
                                }
                            }, preparationTime);
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
                    } else if (actionClient.actionType == ActionClientEnum.SET_TRAP) {
                        User user = users.get(connection.getID());
                        if (user.slotRoom == -1) {
                            System.out.println("kick: " + connection.getID());
                            users.remove(connection.getID());
                            connection.close();
                        } else {
                            Trap trap = new Trap();
                            SetTrapRequest setTrapRequest = (SetTrapRequest) actionClient.request;
                            trap.trapId = setTrapRequest.trapId;
                            trap.x = setTrapRequest.x;
                            trap.y = setTrapRequest.y;
                            // TODO: check exist on x, y other trap
                            traps.add(trap);

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
        actionServer.actionType = ActionServerEnum.UPDATE_GAME;
        actionServer.response = new UpdateGameResponse();
        ((UpdateGameResponse)actionServer.response).targetX = new int[]{
                users.get(usersInRoom[0]).targetX, users.get(usersInRoom[1]).targetX,
        };
        ((UpdateGameResponse)actionServer.response).targetY = new int[]{
                users.get(usersInRoom[0]).targetY, users.get(usersInRoom[1]).targetY,
        };
        ((UpdateGameResponse)actionServer.response).traps = (Trap[]) traps.toArray();

        for (int value : usersInRoom) {
            server.sendToTCP(value, actionServer);
        }
    }
}