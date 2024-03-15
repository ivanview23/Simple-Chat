package edu.school21.sockets.models;

public class Chatroom {
    private long id;
    private final String room;

    public Chatroom(long id, String room) {
        this.id = id;
        this.room = room;
    }

    public Chatroom(String room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Chatroom{" +
                "id=" + id +
                ", name='" + room +
                '}';
    }

    public String getName() {
        return room;
    }

    public long getId() {
        return id;
    }

}
