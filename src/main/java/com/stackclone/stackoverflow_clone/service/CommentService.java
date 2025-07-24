package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Comment;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CommentService {

    void addCommentToQuestion(Long questionId, String commentText);

    void addCommentToAnswer(Long answerId, String commentText);

    List<Comment> getCommentByQuestionId(Long questionId);

    Map<Long, List<Comment>> getCommentsGroupedByAnswerIds(List<Long> answerIds);
}
