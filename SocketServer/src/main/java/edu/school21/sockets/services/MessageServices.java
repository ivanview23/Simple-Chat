package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.User;

import java.util.LinkedList;

public interface MessageServices {
    void createDb();
    void saveMessage(User user, Chatroom room, String text);
    LinkedList<String> getListRoom(long id);

}
