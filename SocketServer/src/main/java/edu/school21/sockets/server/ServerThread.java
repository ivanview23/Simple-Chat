package edu.school21.sockets.server;

import com.google.gson.Gson;
import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.ClientObjectJson;
import edu.school21.sockets.models.User;
import edu.school21.sockets.services.ChatroomServices;
import edu.school21.sockets.services.MessageServices;
import edu.school21.sockets.services.UserServices;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerThread extends Thread {

    protected final BufferedReader in;
    private final BufferedWriter out;
    private final UserServices userServices;
    private final MessageServices messageServices;
    private final ChatroomServices chatroomServices;
    private User user;
    private boolean exit = true;
    private ClientObjectJson clientObjectJson;

    public ServerThread(Socket socket, UserServices userServices, MessageServices messageServices, ChatroomServices chatroomServices) throws IOException {
        this.userServices = userServices;
        this.messageServices = messageServices;
        this.chatroomServices = chatroomServices;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        clientObjectJson = new ClientObjectJson();

        start();
    }

    @Override
    public void run() {
        try {
            sendMessage("Hello from Server!");
            begin();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void begin() throws IOException {
        sendMessage("1. signIn");
        sendMessage("2. SignUp");
        sendMessage("3. Exit");
        sendMessage(">");
        greetings(readMessage());
    }

    protected void greetings(String command) throws IOException {
        if (exit) {
            switch (command) {
                case "3":
                    exitServer();
                    break;
                case "2":
                    sendMessage("Enter username:");
                    sendMessage(">");
                    signUpUsers(readMessage());
                    break;
                case "1":
                    sendMessage("Enter username:");
                    sendMessage(">");
                    signInUsers(readMessage());
                    break;
                default:
                    sendMessage("Command not found! Try again!");
                    begin();
                    break;
            }
        }
    }

    private void signUpUsers(String name) throws IOException {
        if (userServices.checkUsers(name)) {
            sendMessage("User already exists!");
            alreadyExistsMenu("2");
        } else {
            sendMessage("Enter password:");
            sendMessage(">");
            userServices.signUp(name, readMessage());
            userRegistered(name);
            sendMessage("Successful!");
            createRoomMenu();
        }
    }

    private void userRegistered(String name) {
        user = userServices.getUser(name);
        clientObjectJson.setFromId(user.getId());
    }

    private void alreadyExistsMenu(String n) throws IOException {
        switch (alreadyExistsCommand()) {
            case "1":
                greetings(n);
                break;
            case "2":
                begin();
                break;
            default:
                sendMessage("Command not found! Return!");
                sendMessage(">");
                begin();
                break;
        }
    }

    private String alreadyExistsCommand() throws IOException {
        sendMessage("1. Try again!");
        sendMessage("2. Cancel");
        sendMessage(">");
        return readMessage();
    }

    private void signInUsers(String name) throws IOException {
        if (!userServices.checkUsers(name)) {
            sendMessage("User does not exist!");
            alreadyExistsMenu("1");
        } else {
            sendMessage("Enter password:");
            sendMessage(">");
            if (!userServices.signIn(name, readMessage())) {
                sendMessage("Password wrong!");
                sendMessage(">");
                begin();
            } else {
                userRegistered(name);
                createRoomMenu();
            }
        }
    }

    private void createRoomMenu() throws IOException {
        switch (createRoomCommand()) {
            case "1":
                createRoom();
                break;
            case "2":
                chooseRoom();
                break;
            case "3":
                exitServer();
                break;
            default:
                sendMessage("Command not found! Return!");
                sendMessage(">");
                createRoomMenu();
                break;
        }
    }

    private String createRoomCommand() throws IOException {
        sendMessage("1.\tCreate room");
        sendMessage("2.\tChoose room");
        sendMessage("3.\tExit");
        sendMessage(">");
        return readMessage();
    }

    private void createRoom() throws IOException {
        sendMessage("Enter the chatroom name:");
        sendMessage(">");
        String name = readMessage();
        if (chatroomServices.saveChatroom(name)) {
            inputMessage(chatroomServices.getRoom(name));
        } else {
            alreadyExistsRoom();
        }
    }

    private void alreadyExistsRoom() throws IOException {
        sendMessage("Chatroom already exists!");
        createRoomMenu();
    }

    private void chooseRoom() throws IOException {
        sendMessage("Rooms: ");
        List<String> rooms = chatroomServices.getRooms();
        int i;
        for (i = 0; i < rooms.size(); i++) {
            sendMessage((i + 1) + ". " + rooms.get(i));
        }
        sendMessage(i + 1 + ". Exit");
        sendMessage(">");
        inputMessage(chatroomServices.getRoom(rooms.get(Integer.parseInt(readMessage()) - 1)));
    }

    private void inputMessage(Chatroom room) throws IOException {
        welcomeRoom(room);
        boolean exit = true;
        while (exit) {
            sendMessage(">");
            String text = readMessage();
            if (text.equals("Exit")) {
                sendMessage("You have left the chat.");
                sendMessage("exit");
                exit = false;
            } else {
                messageServices.saveMessage(user, room, text);
                for (ServerThread server : Server.linkedList) {
                    if (server.getRoomId() == clientObjectJson.getRoomId()) {
                        server.sendMessage(user.getName() + ": " + text);
                    }
                }
            }
        }
    }

    private void welcomeRoom(Chatroom room) throws IOException {
        clientObjectJson.setRoomId(room.getId());
        sendMessage(room.getName() + " ---");
        for (String text : messageServices.getListRoom(room.getId())) {
            sendMessage(text);
        }
    }

    private void sendMessage(String text) throws IOException {
        clientObjectJson.setMessage(text);
        Gson gson = new Gson();
        out.write(gson.toJson(clientObjectJson) + "\n");
        out.flush();
    }

    private String readMessage() throws IOException {
        Gson gson = new Gson();
        clientObjectJson = gson.fromJson(in.readLine(), ClientObjectJson.class);
        return clientObjectJson.getMessage();
    }

    public long getRoomId() {
        return clientObjectJson.getRoomId();
    }

    private void exitServer() throws IOException {
        sendMessage("exit");
        exit = false;
        in.close();
        out.close();
    }

}
