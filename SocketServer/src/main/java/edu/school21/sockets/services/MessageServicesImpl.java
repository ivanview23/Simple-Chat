package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repository.message.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;

@Component
public class MessageServicesImpl implements MessageServices {
    private final MessageRepository<Message> messageRepository;

    @Autowired
    public MessageServicesImpl(MessageRepository<Message> messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void createDb() {
        messageRepository.createBase();
    }

    @Override
    public void saveMessage(User user, Chatroom room, String text){
        Message message = new Message(user, room, text, new Date());
        messageRepository.save(message);
    }

    @Override
    public LinkedList<String> getListRoom(long id) {
        LinkedList<String> messages = new LinkedList<>();
        for(Message message: messageRepository.findAll(id)) {
            messages.add(message.getUser().getName() + ": " +message.getText());
        }
        return messages;
    }
}
