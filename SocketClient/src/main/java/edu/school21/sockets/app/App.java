package edu.school21.sockets.app;

import edu.school21.sockets.client.Client;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        Integer port = parsArgs(args[0]);
        Client client = new Client();
        String host = "localhost";
        try {
            client.connectServer(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer parsArgs(String args) {
        String[] split = args.split("=");
        if(split[0].equals("--port")) {
            return Integer.parseInt(split[1]);
        }
        throw new IllegalArgumentException("Port argument not found");
    }

}
