package edu.school21.sockets.models;

import java.util.Date;

public class Message {
    private long id;
    private final User author;
    private final Chatroom chatroom;
    private final String text;
    private final Date date;

    public Message (long id, User author, Chatroom chatroom, String text, Date date) {
        this.id = id;
        this.author = author;
        this.chatroom = chatroom;
        this.text = text;
        this.date = date;
    }

    public Message (User author, Chatroom chatroom, String text, Date date) {
        this.author = author;
        this.chatroom = chatroom;
        this.text = text;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", author='" + author.getName() +
                ", chatroom='" + chatroom.getName() +
                ", text='" + text +
                '}';
    }

    public User getUser() {
        return author;
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }
}
