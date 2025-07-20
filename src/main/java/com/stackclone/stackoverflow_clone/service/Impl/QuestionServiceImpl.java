package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.Tag;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.repository.QuestionRepository;
import com.stackclone.stackoverflow_clone.service.QuestionService;

import com.stackclone.stackoverflow_clone.service.TagService;
import com.stackclone.stackoverflow_clone.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final UserService userService;
    private final TagService tagService;

    private final QuestionRepository questionRepository;

    public void updateQuestion(Question question,Long questionId) {
        Question existingQuestion = questionRepository.findById(questionId).orElseThrow();
        existingQuestion.setContent(question.getContent());
        existingQuestion.setTitle(question.getTitle());
        existingQuestion.setTags(question.getTags());

        questionRepository.save(question);
    }

    public void createQuestion(Question question,List<Long> tagIds) {
        User loggedInUser = userService.getLoggedInUser();
        List<Tag> tags = tagService.getTagsByIds(tagIds);
        question.setUser(loggedInUser);
        question.setTags(new HashSet<>(tags));

        questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    public List<Question> getAllQuestions() {

        return questionRepository.findAll();
    }

    @Override
    public Question getQuestionById(Long id) {
        Optional<Question> optionalQuestion = questionRepository.findById(id);

        return optionalQuestion.get();
    }

    @Override
    public List<Question> getQuestionsByUser(Long id) {
        User user = userService.getUserById(id);
        List<Question> questions = user.getQuestions();

        return questions;
    }

    @Override
    public Page<Question> getPaginatedQuestions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return questionRepository.findAll(pageable);
    }

}



