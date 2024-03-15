package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserServicesImpl implements UserServices {

    private final UserRepository<User> userRepository;

    @Autowired
    public UserServicesImpl(UserRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createDb() {
        userRepository.createBase();
    }

    @Override
    public void signUp(String name, String password) {
        User user = new User(name, password);
        userRepository.save(user);
    }

    @Override
    public boolean signIn(String name, String password) {
        return userRepository.verification(name, password);
    }

    @Override
    public boolean checkUsers(String name) {
        return userRepository.findByName(name).isPresent();
    }

    @Override
    public User getUser(String name) {
        Optional<User> user = userRepository.findByName(name);
        return user.orElse(null);
    }
}
