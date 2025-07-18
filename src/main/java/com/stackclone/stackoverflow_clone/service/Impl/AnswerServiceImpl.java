package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Answer;
import com.stackclone.stackoverflow_clone.repository.AnswerRepository;
import com.stackclone.stackoverflow_clone.service.AnswerService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

    public Answer getAnswerById(Long answerId) {
        Optional<Answer> answer = answerRepository.findById(answerId);

        return answer.orElseThrow(() -> new RuntimeException("Answer not found with ID: " + answerId));
    }
}
