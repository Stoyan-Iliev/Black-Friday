package com.store.service;

import com.store.entity.User;
import com.store.exception.UserAlreadyExistException;
import com.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository repository) {
        this.userRepository = repository;
    }

    public User registerNewUserAccount(User user) {
        ensureUserNotExist(user);

        return userRepository.save(user);
    }

    private void ensureUserNotExist(User user) {
        if (emailExist(user.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " +
                    user.getEmail());
        }

        if (usernameExist(user.getUsername())){
            throw new UserAlreadyExistException("There is an account with that username: " +
                    user.getUsername());
        }
    }

    private boolean usernameExist(String username) {
        return userRepository.getByUsername(username) != null;
    }

    private boolean emailExist(String email) {
        return userRepository.getByEmail(email) != null;
    }
}
