package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Answer;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnswerService {

    public void createAnswer(Answer answer);

    public void deleteAnswer(Long answerId);

    public List<Answer> getAllAnswersByQuestionId(Long questionId);

    public void updateAnswer(Answer answer, Long answerId);

    public Answer getAnswerById(Long answerId);

    public List<Answer> getAnswerByUserId(Long userId);
}
