package ru.timelimit.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import ru.timelimit.network.*;

public class GameServer {
    private final static Logger LOG = Logger.getLogger(String.valueOf(GameServer.class));

    private static final int preparationTime = 10 * 1000;

    private static Room room = new Room();
    public static HashMap<String, User> users = new HashMap<>();
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

                    if (actionClient.actionType == ActionClientEnum.FINISH) {
                        if (users.get(actionClient.accessToken).slotRoom != -1) {
                            room.usersInRoom[users.get(actionClient.accessToken).slotRoom] = -1;
                            users.get(actionClient.accessToken).slotRoom = -1;
                            room.countInRoom--;

                            ActionServer actionServer = new ActionServer();
                            actionServer.actionType = ActionServerEnum.YOU_WIN;
                            server.sendToTCP(connection.getID(), actionServer);

                            LOG.info(String.format("user %d leave from room", connection.getID()));

                            if (room.countInRoom == 0) {
                                server.sendToAllTCP(updateLobby());
                                room.gameInProcess = false;
                                room.traps.clear();
                                LOG.info("room ready for next game");
                            }
                        }
                    } else if (actionClient.actionType == ActionClientEnum.JOIN) {
                        if (room.countInRoom < 2 && !room.gameInProcess) {
                            users.get(actionClient.accessToken).slotRoom = room.countInRoom;
                            room.usersInRoom[room.countInRoom] = connection.getID();
                            room.countInRoom++;

                            LOG.info(String.format("user %d join at slot %d", connection.getID(), room.countInRoom - 1));

                            if (room.countInRoom == 2) {
                                room.gameInProcess = true;
                            }
                        }

                        if (room.gameInProcess) {
                            for (int id : room.usersInRoom) {
                                ActionServer actionServer = new ActionServer();
                                actionServer.actionType = ActionServerEnum.START_PREPARATION;

                                LOG.info("start preparation");

                                server.sendToTCP(id, actionServer);
                            }

                            room.preparationTimer = new Timer();
                            room.preparationTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    for (int id : room.usersInRoom) {
                                        ActionServer actionServer = new ActionServer();
                                        actionServer.actionType = ActionServerEnum.START_GAME;

                                        LOG.info("start game");

                                        server.sendToTCP(id, actionServer);
                                    }
                                }
                            }, preparationTime);
                        }

                        server.sendToAllTCP(updateLobby());
                    } else if (actionClient.actionType == ActionClientEnum.SELECT_TARGET) {
                        User user = users.get(actionClient.accessToken);
                        if (user.slotRoom == -1) {
                            LOG.warning("kick: " + connection.getID());
                            users.remove(actionClient.accessToken);
                            connection.close();
                        } else {
                            SelectTargetRequest request = (SelectTargetRequest) actionClient.request;
                            user.targetX = request.targetX;
                            user.targetY = request.targetY;
                            LOG.info(String.format("user %d select target (%d, %d)", connection.getID(), user.targetX, user.targetY));

                            updateGame(server);
                        }
                    } else if (actionClient.actionType == ActionClientEnum.UPDATE_LOBBY) {
                        connection.sendTCP(updateLobby());
                    } else if (actionClient.actionType == ActionClientEnum.UPDATE_GAME) {
                        User user = users.get(actionClient.accessToken);
                        if (user.slotRoom == -1) {
                            LOG.warning("kick: " + connection.getID());
                            users.remove(actionClient.accessToken);
                            connection.close();
                        } else {
                            updateGame(server);
                        }
                    } else if (actionClient.actionType == ActionClientEnum.SET_TRAP) {
                        User user = users.get(actionClient.accessToken);
                        if (user.slotRoom == -1) {
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
                            room.traps.add(trap);
                            LOG.info(String.format("user %d set trap %d at (%d, %d)", connection.getID(), trap.trapId, trap.x, trap.y));

                            updateGame(server);
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

                                    users.put(accessToken.toString(), new User());
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
                    }

                }
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                LOG.info("disconnected: " + connection.getID());
                if (usersAuth.containsKey(connection.getID())) {
                    User user = users.get(usersAuth.get(connection.getID()));
                    if (user.slotRoom != -1) {
                        int otherUserId = room.usersInRoom[(user.slotRoom + 1) % 2];

                        room.countInRoom = 0;
                        room.usersInRoom[0] = -1;
                        room.usersInRoom[1] = -1;

                        ActionServer actionServer = new ActionServer();
                        actionServer.actionType = ActionServerEnum.YOU_WIN;

                        room.gameInProcess = false;

                        server.sendToTCP(otherUserId, actionServer);
                        server.sendToAllTCP(updateLobby());
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
        actionServer.response = new UpdateLobbyResponse();
        ((UpdateLobbyResponse)actionServer.response).countInRoom = room.countInRoom;
        ((UpdateLobbyResponse)actionServer.response).usersInRoom = room.usersInRoom;
        ((UpdateLobbyResponse)actionServer.response).gameInProcess = room.gameInProcess;
        return actionServer;
    }

    private static void updateGame(Server server) {
        ActionServer actionServer = new ActionServer();
        actionServer.actionType = ActionServerEnum.UPDATE_GAME;
        actionServer.response = new UpdateGameResponse();
        ((UpdateGameResponse)actionServer.response).targetX = new int[]{
                users.get(usersAuth.get(room.usersInRoom[0])).targetX, users.get(usersAuth.get(room.usersInRoom[1])).targetX,
        };
        ((UpdateGameResponse)actionServer.response).targetY = new int[]{
                users.get(usersAuth.get(room.usersInRoom[0])).targetY, users.get(usersAuth.get(room.usersInRoom[1])).targetY,
        };
        ((UpdateGameResponse)actionServer.response).traps = (Trap[]) room.traps.toArray();

        for (int value : room.usersInRoom) {
            server.sendToTCP(value, actionServer);
        }
    }
}