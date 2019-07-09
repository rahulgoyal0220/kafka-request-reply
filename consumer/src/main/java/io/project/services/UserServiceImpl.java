package io.project.services;

import io.project.model.User;
import io.project.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public User getUserById(Long userId) {
        return userRepository.getUserByUserId(userId);
    }
}
