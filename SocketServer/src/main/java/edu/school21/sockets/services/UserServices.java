package edu.school21.sockets.services;

import edu.school21.sockets.models.User;

public interface UserServices {
    void createDb();
    void signUp(String name, String password);
    boolean signIn(String name, String password);
    boolean checkUsers(String name);
    User getUser(String name);

}
