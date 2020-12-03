package com.phillip.networking;

import com.esotericsoftware.kryonet.Connection;
import com.phillip.networking.packets.AuthenticationPacket;
import com.phillip.networking.packets.AuthenticationResponse;
import com.phillip.networking.packets.MessagePacket;

public class PacketHandler {

    public static void process(Connection connection, Object object) {
        if (object instanceof MessagePacket) {
            System.out.println("Received packet from " + connection.getRemoteAddressTCP().getAddress());
            System.out.println("Contents: " + ((MessagePacket) object).getMessage());
            System.out.println("Sending reply");
            MessagePacket messagePacket = new MessagePacket("Hello, I have received your message!");
            connection.sendTCP(messagePacket);
        } else if (object instanceof AuthenticationPacket) {
            AuthenticationPacket packet = (AuthenticationPacket) object;
            int code = 0;
            switch (packet.getIntent()) {
                case "DRIVER":
                    code = 1;
                    break;
                case "RIDER":
                    code = -1;
                    break;
            }

            System.out.println("USERNAME: " + packet.getUsername());
            System.out.println("PASSWORD: " + packet.getPassword());
            AuthenticationResponse response = new AuthenticationResponse(code);
            connection.sendTCP(response);
            System.out.println("Sent reply");
        }
    }
}
