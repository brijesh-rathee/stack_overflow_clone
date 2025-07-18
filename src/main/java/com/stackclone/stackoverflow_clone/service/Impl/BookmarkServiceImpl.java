package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.repository.BookmarkRepository;
import com.stackclone.stackoverflow_clone.service.BookmarkService;
import org.springframework.stereotype.Service;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }
}
