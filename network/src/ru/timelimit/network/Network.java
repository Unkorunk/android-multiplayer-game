package ru.timelimit.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(ru.timelimit.network.ActionClientEnum.class);
        kryo.register(ru.timelimit.network.ActionClient.class);
        kryo.register(ru.timelimit.network.ActionServerEnum.class);
        kryo.register(ru.timelimit.network.ActionServer.class);
        kryo.register(ru.timelimit.network.Request.class);
        kryo.register(ru.timelimit.network.Response.class);
        kryo.register(ru.timelimit.network.UpdateLobbyResponse.class);
        kryo.register(ru.timelimit.network.UpdateGameResponse.class);
        kryo.register(ru.timelimit.network.SelectTargetRequest.class);
        kryo.register(ru.timelimit.network.SetTrapRequest.class);
        kryo.register(ru.timelimit.network.Trap.class);
        kryo.register(ru.timelimit.network.ConnectResponse.class);
        kryo.register(ru.timelimit.network.ConnectRequest.class);
        kryo.register(int[].class);
        kryo.register(Trap[].class);
        kryo.register(Lobby[].class);
        kryo.register(Lobby.class);
        kryo.register(JoinRequest.class);
        kryo.register(GameUser.class);
    }
}
