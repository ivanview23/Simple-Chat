package edu.school21.sockets.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;

public class Client {

    private BufferedReader reader;
    private BufferedReader in;
    private BufferedWriter out;
    private Socket socket;
    private boolean exit = true;
    private final Object lock = new Object();
    private ClientObject clientObject;

    public void connectServer(String host, Integer port) throws IOException {

        this.socket = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.clientObject = new ClientObject();

        new OutMessage().start();
        new InMessage().start();
    }

    private void outClient() throws IOException {
        exit = false;
        System.out.println("Подключение остановлено...");
        out.write("close\n");
        out.flush();
        socket.close();
        in.close();
        out.close();
    }

    private class InMessage extends Thread {
        @Override
        public void run() {
            try {
                synchronized (lock) {
                    while (exit) {
                        readMessage(parseJson());
                    }
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private String parseJson() throws IOException  {
            Gson gson = new Gson();
            clientObject = gson.fromJson(in.readLine(), ClientObject.class);
            return clientObject.getMessage();
        }

        private void readMessage(String message) throws IOException, InterruptedException {
            if (message.equals("exit")) {
                outClient();
                lock.notify();
                return;
            }
            if (message.equals(">")) {
                lock.notify();
                lock.wait();
            } else {
                System.out.println(message);
            }

        }
    }

    private class OutMessage extends Thread {
        @Override
        public void run() {
            try {
                synchronized (lock) {
                    while (exit) {
                        lock.wait();
                        if(exit) {
                            System.out.print("> ");
                            sendMessage(reader.readLine());
                        }
                        lock.notify();
                    }
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void sendMessage(String text) throws IOException {
            clientObject.setMessage(text);
            Gson gson = new Gson();
            out.write(gson.toJson(clientObject) + "\n");
            out.flush();
            System.out.println(gson.toJson(clientObject));
        }
    }
}
