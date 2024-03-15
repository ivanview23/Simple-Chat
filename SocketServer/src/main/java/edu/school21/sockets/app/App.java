package edu.school21.sockets.app;

import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class App {

    public static void main(String[] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        Integer port = parsArgs(args[0]);
        Server server = context.getBean(Server.class);
        try {
            server.startServer(port);
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
