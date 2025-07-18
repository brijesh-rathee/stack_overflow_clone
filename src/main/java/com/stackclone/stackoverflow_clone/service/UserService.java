package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.User;

public interface UserService {

    User getLoggedInUser();

    public User getUserById(Long id);
}
