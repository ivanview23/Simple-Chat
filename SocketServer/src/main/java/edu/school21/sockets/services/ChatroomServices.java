package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.User;

import java.util.List;

public interface ChatroomServices {
    void createDb();
    boolean saveChatroom(String room);
    Chatroom getRoom(String name);
    List<String> getRooms();
}
