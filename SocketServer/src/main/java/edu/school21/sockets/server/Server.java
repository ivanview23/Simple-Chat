package edu.school21.sockets.server;

import edu.school21.sockets.services.ChatroomServices;
import edu.school21.sockets.services.MessageServices;
import edu.school21.sockets.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

@Component
public class Server {
    private ServerSocket serverSocket;
    private final UserServices userServices;
    private final MessageServices messageServices;
    private final ChatroomServices chatroomServices;
    protected static final LinkedList<ServerThread> linkedList = new LinkedList<>();
    private boolean exit = true;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Autowired
    public Server(UserServices userServices, MessageServices messageServices, ChatroomServices chatroomServices) {
        this.userServices = userServices;
        this.messageServices = messageServices;
        this.chatroomServices = chatroomServices;

        new outServer().start();
    }

    public void startServer(Integer port) throws IOException {
        System.out.println("Server up\n");
        userServices.createDb();
        messageServices.createDb();
        chatroomServices.createDb();

        serverSocket = new ServerSocket(port);
        try {
            while (exit) {
                Socket socket = serverSocket.accept();
                try {
                    System.out.println("подключился клиент");
                    System.out.println(socket);
                    linkedList.add(new ServerThread(socket, userServices, messageServices, chatroomServices));
                } catch (IOException e) {
                    socket.close();
                }
            }
        } catch (SocketException ignored) {
        }
    }

    public void stopServer() throws IOException {
        for (ServerThread st : linkedList) {
            st.greetings("3");
        }
        serverSocket.close();
    }


    private class outServer extends Thread {
        @Override
        public void run() {
            try {
                while (exit) {
                    if (reader.ready()) {
                        String out = reader.readLine();
                        if (out.equals("Exit")) {
                            stopServer();
                            exit = false;
                        }
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }
}
