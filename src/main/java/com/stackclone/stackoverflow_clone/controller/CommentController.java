package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.entity.Comment;
import com.stackclone.stackoverflow_clone.service.Impl.CommentServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentServiceImpl;

    @GetMapping("/")
    public List<Comment> getComments(){

        return commentServiceImpl.getComments();
    }

    @PostMapping("/")
    public void addComment(@RequestBody Comment comment){
        commentServiceImpl.addComment(comment);
    }

    @PostMapping("/{id}")
    public void updateComment(@PathVariable Long id, @RequestBody Comment comment){
        commentServiceImpl.updateComment(id,comment);
    }

    @PostMapping("/delete/{id}")
    public void deleteComment(@PathVariable Long id){
        commentServiceImpl.deleteComment(id);
    }
}
