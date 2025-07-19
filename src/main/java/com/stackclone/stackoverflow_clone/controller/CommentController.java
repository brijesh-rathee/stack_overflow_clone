package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Comment;
import com.stackclone.stackoverflow_clone.service.Impl.CommentServiceImpl;

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

    @GetMapping("/")
    public String getComments(Model model){
        List<Comment>comments = commentServiceImpl.getComments();
        model.addAttribute("comments",comments);

        return "";
    }

    @PostMapping("/add")
    public String addComment(@RequestBody Comment comment){
        commentServiceImpl.addComment(comment);

        return "redirect:/questions/" + comment.getQuestion().getId();
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
