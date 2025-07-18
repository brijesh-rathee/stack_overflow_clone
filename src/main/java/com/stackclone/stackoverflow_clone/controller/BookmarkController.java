package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.service.BookmarkService;
import com.stackclone.stackoverflow_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/questions")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final UserService userService;

    @PostMapping("/{id}/bookmark")
    public String bookmarkQuestion(@PathVariable("id") Long questionId) {
        User currentUser = userService.getLoggedInUser();
        bookmarkService.bookmark(questionId, currentUser);

        return "redirect:/questions/" + questionId;
    }

    @PostMapping("/{id}/unbookmark")
    public String removeBookmark(@PathVariable("id") Long questionId) {
        User currentUser = userService.getLoggedInUser();
        bookmarkService.removeBookmark(questionId, currentUser);

        return "redirect:/questions/" + questionId;
    }
}
