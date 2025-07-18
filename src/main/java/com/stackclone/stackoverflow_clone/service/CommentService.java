package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Comment;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {

    List<Comment> getComments();

    Comment getComment(Long commentId);

    void addComment(Comment comment);

    void updateComment(Long commentId, Comment comment);

    void deleteComment(Long commentId);

    List<Comment> getCommentsByUserId(Long userId);

    List<Comment> getCommentByQuestionId(Long questionId);

    List<Comment> getCommentsByAnswerId(Long answerId);
}
