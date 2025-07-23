package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Badge;
import com.stackclone.stackoverflow_clone.entity.Bookmark;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.entity.Vote;

import java.util.List;
import java.util.Set;

public interface UserService {

    User getUserById(Long id);

    void registerUser(User user);

    void updateUser(User user, Long userId);

    void deleteUser(Long userId);

    List<User> getAllUsers();

    Set<Badge> getAllBadgesByUser(Long userId);

    List<Bookmark> getAllBookMarks(Long userId);

    List<Vote> getAllVotesByUser(Long userId);

    User getLoggedInUser();

    List<User> searchBasic(String query);

    User getUserByUsername(String name);
}
