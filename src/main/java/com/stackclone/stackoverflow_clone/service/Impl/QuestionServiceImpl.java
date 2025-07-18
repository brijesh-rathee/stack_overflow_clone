package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.repository.QuestionRepository;
import com.stackclone.stackoverflow_clone.service.QuestionService;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void updateQuestion(Long questionId, Question question){
        Question existingQuestion = questionRepository.findById(questionId).orElseThrow();
        existingQuestion.setContent(question.getContent());
        existingQuestion.setTitle(question.getTitle());
        existingQuestion.setTags(question.getTags());
        questionRepository.save(question);
    }

    public void createQuestion(Question question){
        questionRepository.save(question);
    }
}
