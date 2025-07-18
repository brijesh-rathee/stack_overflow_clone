package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.User;

public interface BookmarkService {

    public void bookmark(Long questionId, User user);

    void removeBookmark(Long questionId, User currentUser);
}
