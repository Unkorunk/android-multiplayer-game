package ru.timelimit.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

import ru.timelimit.network.*;

public class GameServer {
    private final static Logger LOG = Logger.getLogger(String.valueOf(GameServer.class));

    private static final int preparationTime = 10 * 1000;

    private static int nextRoomId = 0;

    public static HashMap<String, User> users = new HashMap<>();
    public static HashMap<Integer, Room> rooms = new HashMap<>();

    public static HashMap<Integer, String> usersAuth = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();

        java.sql.Connection databaseConnection = null;
        try {
            Class.forName("org.postgresql.Driver");
            databaseConnection = DriverManager.getConnection(GlobalSettings.databaseLink,
                    GlobalSettings.databaseUser, GlobalSettings.databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        LOG.info("server start");

        Network.register(server);

        java.sql.Connection finalDatabaseConnection = databaseConnection;
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
                LOG.info("connected: " + connection.getID());
            }

            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                if (object instanceof ActionClient) {
                    ActionClient actionClient = (ActionClient) object;

                    if (actionClient.actionType != ActionClientEnum.CONNECT && !users.containsKey(actionClient.accessToken)) {
                        LOG.info(String.format("Invalid accessToken: %s", actionClient.accessToken));
                        connection.close();
                        return;
                    }

                    User user = users.getOrDefault(actionClient.accessToken, null);

                    if (actionClient.actionType == ActionClientEnum.FINISH) {
                        if (user.room != null) {
                            user.room.users.remove(user);

                            ActionServer actionServer = new ActionServer();
                            actionServer.actionType = ActionServerEnum.YOU_WIN;
                            server.sendToTCP(connection.getID(), actionServer);

                            LOG.info(String.format("user %d leave from room", connection.getID()));

                            if (user.room.users.size() == 0) {
                                user.room.gameInProcess = false;
                                user.room.traps.clear();
                                LOG.info("room ready for next game");
                                server.sendToAllTCP(updateLobby());
                            }
                            user.room = null;
                        }
                    } else if (actionClient.actionType == ActionClientEnum.JOIN) {
                        JoinRequest joinRequest = ((JoinRequest) actionClient.request);

                        if (joinRequest == null) {
                            LOG.warning("joinRequest is null or ");
                            connection.close();
                        }

                        Room newRoom = rooms.getOrDefault(joinRequest.lobbyId, null);
                        if (newRoom == null) {
                            LOG.warning("Invalid room");
                            return;
                        }

                        if (newRoom.users.size() < 2 && !newRoom.gameInProcess) {
                            user.room = newRoom;
                            newRoom.users.add(user);

                            LOG.info(String.format("user %d join at slot %d", connection.getID(), user.room.users.size()));

                            if (user.room.users.size() == 2) {
                                user.room.gameInProcess = true;
                            }
                        }

                        if (newRoom.gameInProcess) {
                            for (User userTemp : newRoom.users) {
                                ActionServer actionServer = new ActionServer();
                                actionServer.actionType = ActionServerEnum.START_PREPARATION;

                                LOG.info("start preparation");

                                server.sendToTCP(userTemp.connectionId, actionServer);
                            }

                            newRoom.preparationTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    for (User userTemp : newRoom.users) {
                                        ActionServer actionServer = new ActionServer();
                                        actionServer.actionType = ActionServerEnum.START_GAME;

                                        LOG.info("start game");

                                        server.sendToTCP(userTemp.connectionId, actionServer);
                                    }
                                }
                            }, preparationTime);
                        }

                        server.sendToAllTCP(updateLobby());
                    } else if (actionClient.actionType == ActionClientEnum.SELECT_TARGET) {
                        if (user.room == null) {
                            LOG.warning("kick: " + connection.getID());
                            users.remove(actionClient.accessToken);
                            connection.close();
                        } else {
                            SelectTargetRequest request = (SelectTargetRequest) actionClient.request;
                            user.targetX = request.targetX;
                            user.targetY = request.targetY;
                            user.positionX = request.positionX;
                            user.positionY = request.positionY;
                            LOG.info(String.format("user %d select target (%d, %d)", connection.getID(), user.targetX, user.targetY));

                            updateGame(server, user.room);
                        }
                    } else if (actionClient.actionType == ActionClientEnum.UPDATE_LOBBY) {
                        connection.sendTCP(updateLobby());
                    } else if (actionClient.actionType == ActionClientEnum.UPDATE_GAME) {
                        if (user.room == null) {
                            LOG.warning("kick: " + connection.getID());
                            users.remove(actionClient.accessToken);
                            connection.close();
                        } else {
                            updateGame(server, user.room);
                        }
                    } else if (actionClient.actionType == ActionClientEnum.SET_TRAP) {
                        if (user.room == null) {
                            LOG.warning("kick: " + connection.getID());
                            users.remove(actionClient.accessToken);
                            connection.close();
                        } else {
                            Trap trap = new Trap();
                            SetTrapRequest setTrapRequest = (SetTrapRequest) actionClient.request;
                            trap.trapId = setTrapRequest.trapId;
                            trap.x = setTrapRequest.x;
                            trap.y = setTrapRequest.y;
                            // TODO: check exist on x, y other trap
                            user.room.traps.add(trap);
                            LOG.info(String.format("user %d set trap %d at (%d, %d)", connection.getID(), trap.trapId, trap.x, trap.y));

                            updateGame(server, user.room);
                        }
                    } else if (actionClient.actionType == ActionClientEnum.CONNECT) {
                        ConnectRequest connectRequest = (ConnectRequest) actionClient.request;

                        if (connectRequest == null || connectRequest.username == null || connectRequest.password == null) {
                            LOG.info("username or password is null");
                            connection.close();
                            return;
                        }

                        Random random = new Random();
                        StringBuilder accessToken;
                        do {
                            accessToken = new StringBuilder();
                            for (int i = 0; i < 64; i++) {
                                accessToken.append('a' + random.nextInt(26));
                            }
                        } while (users.containsKey(accessToken.toString()));

                        ActionServer actionServer = new ActionServer();
                        actionServer.actionType = ActionServerEnum.CONNECT;
                        actionServer.response = new ConnectResponse();

                        try {
                            PreparedStatement preparedStatementLogin = finalDatabaseConnection.prepareStatement(
                                            "SELECT * FROM users WHERE username=?"
                            );

                            LOG.info(String.format("user %s try log", connectRequest.username));

                            preparedStatementLogin.setString(1, connectRequest.username);
                            ResultSet resultSet = preparedStatementLogin.executeQuery();
                            if (resultSet != null && resultSet.next()) {
                                if (resultSet.getString("password").equals(connectRequest.password)) {
                                    ((ConnectResponse) actionServer.response).accessToken = accessToken.toString();

                                    LOG.info(String.format("user %s success login; accessToken: %s", connectRequest.username, accessToken.toString()));

                                    users.put(accessToken.toString(), new User());
                                    usersAuth.put(connection.getID(), accessToken.toString());
                                } else {
                                    ((ConnectResponse) actionServer.response).accessToken = "INCORRECT PASSWORD";
                                    LOG.info(String.format("user %s incorrect password", connectRequest.username));
                                }
                            } else {
                                PreparedStatement preparedStatementRegister = finalDatabaseConnection.prepareStatement(
                                            "INSERT INTO users(username, password) VALUES(?, ?)"
                                );

                                preparedStatementRegister.setString(1, connectRequest.username);
                                preparedStatementRegister.setString(2, connectRequest.password);

                                if (preparedStatementRegister.executeUpdate() > 0) {
                                    LOG.info(String.format("user %s success reg; accessToken: %s", connectRequest.username, accessToken.toString()));

                                    ((ConnectResponse) actionServer.response).accessToken = accessToken.toString();

                                    User userTemp = new User();
                                    userTemp.connectionId = connection.getID();
                                    users.put(accessToken.toString(), userTemp);
                                    usersAuth.put(connection.getID(), accessToken.toString());
                                } else {
                                    LOG.warning(String.format("user %s fail reg", connectRequest.username));

                                    ((ConnectResponse) actionServer.response).accessToken = "FAILED";
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.exit(1);
                        }

                        connection.sendTCP(actionServer);
                    } else if (actionClient.actionType == ActionClientEnum.CREATE_LOBBY) {
                        rooms.put(nextRoomId++, new Room());
                        server.sendToAllTCP(updateLobby());
                    }

                }
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                LOG.info("disconnected: " + connection.getID());
                if (usersAuth.containsKey(connection.getID())) {
                    User user = users.get(usersAuth.get(connection.getID()));
                    if (user.room != null) {
                        user.room.users.remove(user);
                        if (user.room.users.size() > 0) {
                            User otherUser = user.room.users.get(0);
                            user.room.users.clear();
                            user.room.traps.clear();

                            ActionServer actionServer = new ActionServer();
                            actionServer.actionType = ActionServerEnum.YOU_WIN;

                            user.room.gameInProcess = false;

                            server.sendToTCP(otherUser.connectionId, actionServer);
                            server.sendToAllTCP(updateLobby());
                        }
                    }
                    users.remove(usersAuth.get(connection.getID()));
                }
            }
        });
        server.bind(25568);
    }

    private static ActionServer updateLobby() {
        ActionServer actionServer = new ActionServer();
        actionServer.actionType = ActionServerEnum.UPDATE_LOBBY;
        UpdateLobbyResponse updateLobbyResponse = new UpdateLobbyResponse();

        ArrayList<Integer> availableRooms = new ArrayList<>();
        for (Map.Entry<Integer, Room> entry : rooms.entrySet()) {
            if (!entry.getValue().gameInProcess && entry.getValue().users.size() < 2) {
                availableRooms.add(entry.getKey());
            }
        }

        updateLobbyResponse.lobbies = new Lobby[availableRooms.size()];
        for (int i = 0; i < availableRooms.size(); i++) {
            updateLobbyResponse.lobbies[i].lobbyId = availableRooms.get(i);
        }

        actionServer.response = updateLobbyResponse;
        return actionServer;
    }

    private static void updateGame(Server server, Room room) {
        for (User user : room.users) {
            ActionServer actionServer = new ActionServer();
            actionServer.actionType = ActionServerEnum.UPDATE_GAME;
            actionServer.response = new UpdateGameResponse();
            for (int i = 0; i < room.users.size(); i++) {
                ((UpdateGameResponse) actionServer.response).users[i]
                        = new GameUser(room.users.get(i).connectionId == user.connectionId, room.users.get(i).targetX,
                        room.users.get(i).targetY, room.users.get(i).positionX, room.users.get(i).positionY);
            }
            ((UpdateGameResponse) actionServer.response).traps = (Trap[]) room.traps.toArray();

            server.sendToTCP(user.connectionId, actionServer);
        }
    }
}