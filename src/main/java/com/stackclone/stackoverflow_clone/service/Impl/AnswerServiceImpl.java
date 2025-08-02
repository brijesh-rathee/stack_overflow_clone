package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Answer;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.repository.AnswerRepository;
import com.stackclone.stackoverflow_clone.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionService questionService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    @Override
    public void createAnswer(Answer answer, MultipartFile file, Question question) {
        if(file != null && !file.isEmpty()) {
            String imageurl = cloudinaryService.uploadImageToCloudinary(file);
            answer.setUrl(imageurl);
        }
        answerRepository.save(answer);
    }

    @Override
    public void deleteAnswer(Long answerId) {
        answerRepository.deleteById(answerId);
    }

    @Override
    public List<Answer> getAllAnswersByQuestionId(Long questionId) {
        Question question = questionService.getQuestionById(questionId);

        return question.getAnswers();
    }

    public int getAnswerCountByQuestionId(Long questionId) {
        return answerRepository.countByQuestionId(questionId);
    }

    @Override
    public void updateAnswer(Answer answer, Long answerId) {
        Answer existingAnswer = answerRepository.findById(answerId).orElseThrow();
        answer.setId(answerId);
        answer.setQuestion(existingAnswer.getQuestion());
        answer.setUser(existingAnswer.getUser());
        existingAnswer.setContent(answer.getContent());
        answerRepository.save(existingAnswer);
    }

    @Override
    public Answer getAnswerById(Long answerId) {

        return answerRepository.findById(answerId).orElseThrow();
    }

    @Override
    public List<Answer> getAnswerByUserId(Long userId) {
        User user = userService.getUserById(userId);

        return user.getAnswers();
    }
}