package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Question;

import java.util.List;

public interface QuestionService {

    public List<Question> getAllQuestions();

    public void createQuestion(Question question);

    public Question getQuestionById(Long id);

    public void updateQuestion(Question question , Long id);

    public void deleteQuestion(Long id);

    public List<Question> getQuestionsByUser(Long id);

}
