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
        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof ActionClient) {
                    ActionClient actionClient = (ActionClient) object;
                    System.out.println("[" + actionClient.accessToken + "]: " + actionClient.actionType.name());

                    ActionServer actionServer = new ActionServer();
                    actionServer.actionType = ActionServerEnum.OKAY;
                    connection.sendTCP(actionServer);
                }
            }
        });
        server.start();
        server.bind(25567);
    }
}