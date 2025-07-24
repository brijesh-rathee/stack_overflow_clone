package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Badge;
import com.stackclone.stackoverflow_clone.entity.Bookmark;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.entity.Vote;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface UserService {

    User getUserById(Long id);

    void registerUser(User user);

    void updateUser(User user, Long userId,boolean removeProfile, MultipartFile file);

    void deleteUser(Long userId);

    List<User> getAllUsers();

    Set<Badge> getAllBadgesByUser(Long userId);

    List<Bookmark> getAllBookMarks(Long userId);

    List<Vote> getAllVotesByUser(Long userId);

    User getLoggedInUser();


    User getUserByUsername(String name);

    Page<User> getAllPaginatedUsers(int page, int size, String sortField, String sortDir, String keyword);
}
