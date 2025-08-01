package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Answer;
import com.stackclone.stackoverflow_clone.service.AnswerService;
import com.stackclone.stackoverflow_clone.service.Impl.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentServiceImpl;
    private final AnswerService answerService;

    @PostMapping("/add")
    public String addComment(@RequestParam(required = false) Long questionId,
                             @RequestParam(required = false) Long answerId,
                             @RequestParam String comment) {

        if (questionId != null) {
            commentServiceImpl.addCommentToQuestion(questionId, comment);
            return "redirect:/questions/" + questionId;
        } else {
            Answer answer = answerService.getAnswerById(answerId);
            commentServiceImpl.addCommentToAnswer(answerId, comment);
            return "redirect:/questions/" + answer.getQuestion().getId();
        }
    }
}