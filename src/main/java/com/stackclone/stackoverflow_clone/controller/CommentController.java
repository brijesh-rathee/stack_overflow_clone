package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Comment;
import com.stackclone.stackoverflow_clone.service.AnswerService;
import com.stackclone.stackoverflow_clone.service.Impl.CommentServiceImpl;

import com.stackclone.stackoverflow_clone.service.QuestionService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentServiceImpl;
    private final QuestionService questionService;
    private final AnswerService answerService;

    @GetMapping("/")
    public String getComments(Model model){
        List<Comment>comments = commentServiceImpl.getComments();
        model.addAttribute("comments",comments);

        return "";
    }

    @PostMapping("/add")
    public String addComment(@RequestParam(required = false) Long questionId,
                             @RequestParam(required = false) Long answerId,
                             @RequestParam String comment) {

        if (questionId != null) {
            commentServiceImpl.addCommentToQuestion(questionId, comment);
            return "redirect:/questions/" + questionId;
        } else if (answerId != null) {
            var answer = answerService.getAnswerById(answerId); // just to get the question ID for redirect
            commentServiceImpl.addCommentToAnswer(answerId, comment);
            return "redirect:/questions/" + answer.getQuestion().getId();
        }

        throw new IllegalArgumentException("Either questionId or answerId must be provided.");
    }


    @PostMapping("/update/{id}")
    public String updateComment(@PathVariable Long id, @RequestBody Comment comment){
        commentServiceImpl.updateComment(id,comment);

        return "redirect:/questions/" + comment.getQuestion().getId();
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id, @RequestParam Long questionId){
        commentServiceImpl.deleteComment(id);

        return "redirect:/questions/" + questionId;
    }

}
