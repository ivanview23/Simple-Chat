package edu.school21.sockets.models;

public class User {
    private long id;
    private final String username;
    private final String password;

    public User (long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User (String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + username +
                '}';
    }

    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }
}
