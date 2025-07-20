package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Answer;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.enums.VoteType;
import com.stackclone.stackoverflow_clone.service.AnswerService;
import com.stackclone.stackoverflow_clone.service.UserService;
import com.stackclone.stackoverflow_clone.service.VoteService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/votes")
public class VoteController {

    private final VoteService voteService;
    private final AnswerService answerService;

    @PostMapping("/question/{questionId}")
    public String voteQuestion(@PathVariable Long questionId,
                               @RequestParam VoteType voteType) {
        voteService.voteQuestion(questionId, voteType);

        return "redirect:/questions/" + questionId;
    }

    @PostMapping("/answer/{answerId}")
    public String voteAnswer(@PathVariable Long answerId,
                             @RequestParam VoteType voteType) {
        voteService.voteAnswer(answerId, voteType);
        Answer answer = answerService.getAnswerById(answerId);
        Long questionId = answer.getQuestion().getId();

        return "redirect:/questions/" + questionId;
    }
}

