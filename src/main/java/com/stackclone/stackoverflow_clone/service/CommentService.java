package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Comment;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CommentService {

    public List<Comment> getComments();

    public Comment getComment(Long commentId);

    public void addComment(Comment comment);

    public void updateComment(Long commentId, Comment comment);

    public void deleteComment(Long commentId);
}
