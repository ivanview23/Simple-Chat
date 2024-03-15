package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.repository.chatroom.ChatroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class ChatroomServicesImpl implements ChatroomServices {

    private final ChatroomRepository<Chatroom> chatroomRepository;

    @Autowired
    public ChatroomServicesImpl(ChatroomRepository<Chatroom> chatroomRepository) {
        this.chatroomRepository = chatroomRepository;
    }

    @Override
    public void createDb() {
        chatroomRepository.createBase();
    }

    @Override
    public boolean saveChatroom(String name) {
        if (!chatroomRepository.findByName(name).isPresent()) {
            Chatroom room = new Chatroom(name);
            chatroomRepository.save(room);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Chatroom getRoom(String name) {
        Optional<Chatroom> chatroom = chatroomRepository.findByName(name);
        return chatroom.orElse(null);
    }

    @Override
    public List<String> getRooms() {
        LinkedList<String> messages = new LinkedList<>();
        for(Chatroom chatroom: chatroomRepository.findAll(0)) {
            messages.add(chatroom.getName());
        }
        return messages;
    }
}
