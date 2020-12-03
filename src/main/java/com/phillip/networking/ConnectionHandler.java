package com.phillip.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.phillip.Constants;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.Set;

public class ConnectionHandler {

    public static ConnectionHandler SINGLETON;

    private final Server server;

    public ConnectionHandler() {
        server = new Server();
        server.start();
        register(server.getKryo());
        try {
            server.bind(Constants.PORT);
            server.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (object instanceof Packet) {
                        PacketHandler.process(connection, object);
                    } else {
                        System.out.println("Unknown object received from " + connection.getRemoteAddressTCP().getAddress());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register(Kryo kryo) {
        Reflections reflections = new Reflections("com.phillip.networking.packets");
        Set<Class<? extends Packet>> classes = reflections.getSubTypesOf(Packet.class);
        System.out.printf("[ConnectionHandler] %d packets registered\n", classes.size());
        classes.forEach(kryo::register);
    }

    public int getConnections() {
        return server.getConnections().length;
    }

    public static ConnectionHandler getInstance() {
        if (SINGLETON == null)
            SINGLETON = new ConnectionHandler();
        return SINGLETON;
    }
}
