package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.service.BookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookmarkController {
    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    public ResponseEntity<String> bookmark(@PathVariable long questionId){
      return new ResponseEntity<>("", HttpStatus.OK);
    }
}
