package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Comment;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.repository.CommentRepository;
import com.stackclone.stackoverflow_clone.service.AnswerService;

import com.stackclone.stackoverflow_clone.service.QuestionService;
import com.stackclone.stackoverflow_clone.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService implements com.stackclone.stackoverflow_clone.service.CommentService {
    private final UserService userService;
    private final QuestionService questionService;
    private final CommentRepository commentRepository;
    private final AnswerService answerService;

    public void addCommentToQuestion(Long questionId, String commentText) {
        Comment comment = new Comment();
        comment.setComment(commentText);
        comment.setUser(userService.getLoggedInUser());
        comment.setQuestion(questionService.getQuestionById(questionId));

        commentRepository.save(comment);
    }

    public void addCommentToAnswer(Long answerId, String commentText) {
        Comment comment = new Comment();
        comment.setComment(commentText);
        comment.setUser(userService.getLoggedInUser());
        var answer = answerService.getAnswerById(answerId);
        comment.setAnswer(answer);

        commentRepository.save(comment);
    }

    public List<Comment> getCommentByQuestionId(Long questionId){
        Question question = questionService.getQuestionById(questionId);
        List<Comment> comments = question.getComments();

        return comments;
    }

    @Override
    public Map<Long, List<Comment>> getCommentsGroupedByAnswerIds(List<Long> answerIds) {
        List<Comment> comments = commentRepository.findByAnswerIdIn(answerIds);

        Map<Long, List<Comment>> answerCommentsMap = new HashMap<>();

        for (Comment comment : comments) {
            Long answerId = comment.getAnswer().getId();
            if (!answerCommentsMap.containsKey(answerId)) {
                answerCommentsMap.put(answerId, new ArrayList<>());
            }
            answerCommentsMap.get(answerId).add(comment);
        }

        return answerCommentsMap;
    }
}