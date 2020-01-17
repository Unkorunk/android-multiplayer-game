package ru.timelimit.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class GameServer {
    public enum ActionClientEnum {
        CONNECT,
        DISCONNECT
    }
    public enum ActionServerEnum {
        OKAY
    }

    public static class ActionClient {
        public String accessToken;
        ActionClientEnum actionType;
    }

    public static class ActionServer {
        public ActionServerEnum actionType;
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();

        server.getKryo().register(ru.timelimit.server.game.GameServer.ActionClientEnum.class);
        server.getKryo().register(ru.timelimit.server.game.GameServer.ActionClient.class);
        server.getKryo().register(ru.timelimit.server.game.GameServer.ActionServerEnum.class);
        server.getKryo().register(ru.timelimit.server.game.GameServer.ActionServer.class);

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
                System.out.println("connected: " + connection.getRemoteAddressTCP());
            }

            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                if (object instanceof ActionClient) {
                    ActionClient actionClient = (ActionClient) object;
                    System.out.println("[" + actionClient.accessToken + "]: " + actionClient.actionType.name());

                    ActionServer actionServer = new ActionServer();
                    actionServer.actionType = ActionServerEnum.OKAY;
                    connection.sendTCP(actionServer);
                }
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                System.out.println("disconnected: " + connection.getRemoteAddressTCP());
            }
        });
        server.bind(25567);
    }
}