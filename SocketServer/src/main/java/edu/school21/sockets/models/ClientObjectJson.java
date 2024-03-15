package edu.school21.sockets.models;

public class ClientObjectJson {

    private String message;
    private long fromId = 0;
    private long roomId = 0;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }
}
