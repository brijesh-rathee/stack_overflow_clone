package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Answer;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnswerService {

    void createAnswer(Answer answer);

    void deleteAnswer(Long answerId);

    List<Answer> getAllAnswersByQuestionId(Long questionId);

    void updateAnswer(Answer answer, Long answerId);

    Answer getAnswerById(Long answerId);

    List<Answer> getAnswerByUserId(Long userId);
}
