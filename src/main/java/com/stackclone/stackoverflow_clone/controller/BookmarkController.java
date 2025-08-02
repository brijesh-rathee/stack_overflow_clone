package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.service.BookmarkService;
import com.stackclone.stackoverflow_clone.service.QuestionService;
import com.stackclone.stackoverflow_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final QuestionService questionService;
    private final UserService userService;

    @PostMapping("/{questionId}/toggle")
    public String toggleBookmark(@PathVariable Long questionId) {
        User user = userService.getLoggedInUser();
        Question question = questionService.getQuestionById(questionId);
        bookmarkService.toggleBookmark(user, question);

        return "redirect:/questions/" + questionId;
    }

    @GetMapping
    public String viewBookmarks(Model model) {
        User user = userService.getLoggedInUser();
        List<Question> questions = bookmarkService.getBookmarkedQuestion(user);
        model.addAttribute("bookmarkedQuestions", questions);

        return "bookmark/list";
    }
}
