package com.phillip;

import com.phillip.networking.ConnectionHandler;

public class Main {

    public static void main(String[] args) {
        System.out.println(ConnectionHandler.getInstance().getConnections());
    }
}
