package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Answer;

import com.stackclone.stackoverflow_clone.entity.Question;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface AnswerService {

    void createAnswer(Answer answer, MultipartFile file, Question question);

    void deleteAnswer(Long answerId);

    List<Answer> getAllAnswersByQuestionId(Long questionId);

    void updateAnswer(Answer answer, Long answerId);

    Answer getAnswerById(Long answerId);

    List<Answer> getAnswerByUserId(Long userId);

    int getAnswerCountByQuestionId(Long questionId);
}
