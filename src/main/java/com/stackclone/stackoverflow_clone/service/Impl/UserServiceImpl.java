package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Badge;
import com.stackclone.stackoverflow_clone.entity.Bookmark;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.entity.Vote;
import com.stackclone.stackoverflow_clone.enums.UserRole;
import com.stackclone.stackoverflow_clone.repository.UserRepository;
import com.stackclone.stackoverflow_clone.service.CloudinaryService;
import com.stackclone.stackoverflow_clone.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public  class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public void registerUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user, Long userId, boolean removeProfile, MultipartFile file) {
        User existingUser = userRepository.findById(userId).orElseThrow();

        if(removeProfile){
            existingUser.setUrl("https://res.cloudinary.com/dx9zbuld9/image/upload/v1753347401/l9xivdngn5jjf33glmwo.jpg");
        }

        if (file != null && !file.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImageToCloudinary(file);
            existingUser.setUrl(imageUrl);
        }

        existingUser.setEmail(user.getEmail());
        existingUser.setBio(user.getBio());
        existingUser.setUsername(user.getUsername());
        existingUser.setLocation(user.getLocation());

        userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        userRepository.deleteById(userId);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        return (User) authentication.getPrincipal();
    }

    @Override
    public Page<User> getAllPaginatedUsers(int page, int size, String sortField, String sortDir, String keyword) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findAll(pageable);
        } else {
            return userRepository.findByUsernameContainingIgnoreCase(keyword, pageable);
        }
    }


    @Override
    public Page<User> getAllUsersWithPagination(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    @Override
    public Page<User> getUsersByRoleWithPagination(UserRole role, PageRequest pageRequest) {
        return userRepository.findByRole(role, pageRequest);
    }
}