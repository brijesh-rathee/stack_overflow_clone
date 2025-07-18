package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.repository.UserRepository;
import com.stackclone.stackoverflow_clone.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getLoggedInUser() {
        return null;
    }

    public User getUserById(Long id){
        Optional<User> user = userRepository.findById(id);

        return user.get();
    }
}
