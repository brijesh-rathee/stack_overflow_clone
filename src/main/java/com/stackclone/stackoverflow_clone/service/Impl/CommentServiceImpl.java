package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Comment;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.repository.CommentRepository;
import com.stackclone.stackoverflow_clone.service.CommentService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserServiceImpl userServiceImpl;

    private final CommentRepository commentRepository;

    public List<Comment> getComments(){

        return commentRepository.findAll();
    }

    public Comment getComment(Long commentId){
        Optional<Comment> comment = commentRepository.findById(commentId);

        return comment.get();
    }

    public void addComment(Comment comment){
        commentRepository.save(comment);
    }
    public void updateComment(Long commentId, Comment comment){
        Comment originalComment = getComment(commentId);
        originalComment.setComment(comment.getComment());

        commentRepository.save(originalComment);
    }

    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }

    public List<Comment> getCommentsByUserId(Long id){
        User user = userServiceImpl.getUserById(id);
        List<Comment> comments = user.getComments();

        return comments;
    }
}
