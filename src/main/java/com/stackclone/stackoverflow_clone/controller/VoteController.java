package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.service.UserService;
import com.stackclone.stackoverflow_clone.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answers")
public class VoteController {

    private final VoteService voteService;
    private final UserService userService;

    @PostMapping("/{id}/upvote")
    public String upvoteAnswer(@PathVariable("id") Long answerId) {
        User user = userService.getLoggedInUser();
        voteService.upvoteAnswer(answerId, user);
        return "redirect:/questions/" + voteService.getQuestionIdByAnswerId(answerId);
    }

    @PostMapping("/{id}/downvote")
    public String downvoteAnswer(@PathVariable("id") Long answerId) {
        User user = userService.getLoggedInUser();
        voteService.downvoteAnswer(answerId, user);
        return "redirect:/questions/" + voteService.getQuestionIdByAnswerId(answerId);
    }
}

