package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Badge;
import com.stackclone.stackoverflow_clone.entity.Bookmark;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.entity.Vote;
import com.stackclone.stackoverflow_clone.repository.UserRepository;
import com.stackclone.stackoverflow_clone.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public User getUserById(Long id){

        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public void registerUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user, Long userId) {
        User existingUser = userRepository.findById(userId).orElseThrow();
        existingUser.setEmail(user.getEmail());
        existingUser.setBio(user.getBio());
        existingUser.setUsername(user.getUsername());
        userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Set<Badge> getAllBadgesByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return user.getBadges();
    }

    @Override
    public List<Bookmark> getAllBookMarks(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return user.getBookmarks();
    }

    @Override
    public List<Vote> getAllVotesByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return user.getVotes();
    }

    @Override
    public User getLoggedInUser() {
        return null;
    }
}
