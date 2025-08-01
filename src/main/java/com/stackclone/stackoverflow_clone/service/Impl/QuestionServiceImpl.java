package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.Tag;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.repository.QuestionRepository;
import com.stackclone.stackoverflow_clone.repository.UserRepository;
import com.stackclone.stackoverflow_clone.service.CloudinaryService;
import com.stackclone.stackoverflow_clone.service.QuestionService;

import com.stackclone.stackoverflow_clone.service.TagService;
import com.stackclone.stackoverflow_clone.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final UserService userService;
    private final TagService tagService;
    private final CloudinaryService cloudinaryService;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public void updateQuestion(Question question,Long questionId, List<Long> tagIds) {
        Question existingQuestion = questionRepository.findById(questionId).orElseThrow();
        existingQuestion.setContent(question.getContent());
        existingQuestion.setTitle(question.getTitle());

        if (tagIds != null && !tagIds.isEmpty()) {
            List<Tag> tags = tagService.getTagsByIds(tagIds);
            existingQuestion.setTags(new HashSet<>(tags));
        }

        questionRepository.save(existingQuestion);
    }

    public void createQuestion(Question question, List<Long> tagIds, MultipartFile file) {
        User loggedInUser = userService.getLoggedInUser();
        List<Tag> tags = tagService.getTagsByIds(tagIds);
        question.setUser(loggedInUser);
        question.setTags(new HashSet<>(tags));

        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadImageToCloudinary(file);
                question.setUrl(imageUrl);
            } catch (Exception e) {
                System.err.println("Failed to upload image: " + e.getMessage());
            }
        }
        String content = question.getContent() != null ? question.getContent() : "";
        question.setContent(content);

        questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    @Override
    public Question getQuestionById(Long id) {
        Question question = questionRepository.findById(id).orElse(null);

        return question;
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

    @Override
    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    @Override
    public Page<Question> getPaginatedQuestionsByTag(Tag tag, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return questionRepository.findByTagsContaining(tag, pageable);
    }

    @Override
    public Page<Question> searchQuestions(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return questionRepository.searchByTitleTagOrAnswer(keyword, pageable);
    }

    @Override
    public Page<Question> getFilteredAndSortedQuestions(int page, int size, Long tagId, String sort) {
        Pageable pageable;

        switch (sort) {
            case "views":
                pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "viewCount"));
                break;
            case "votes":
                pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "voteCount"));
                break;
            default:
                pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        if (tagId != null) {
            return questionRepository.findByTagsId(tagId, pageable);
        } else {
            return questionRepository.findAll(pageable);
        }
    }

    @Override
    public Page<Question> getQuestionsWithVoteLessThan(int voteCount, PageRequest pageRequest) {
        return questionRepository.findByVoteCountLessThan(voteCount, pageRequest);
    }

    @Override
    public Page<Question> getAllQuestionsPage(PageRequest pageRequest) {
        return questionRepository.findAll(pageRequest);
    }

    @Override
    @Transactional
    public void followQuestion(Long questionId, Long userId) {
        User user = userService.getUserById(userId);
        Question question = questionRepository.findById(questionId).orElseThrow();
        user.getFollowedQuestions().add(question);
        userService.registerUser(user);
    }

    @Override
    @Transactional
    public void unfollowQuestion(Long questionId, Long userId) {
        User user = userService.getUserById(userId);
        Question question = questionRepository.findById(questionId).orElseThrow();
        user.getFollowedQuestions().remove(question);
        userService.registerUser(user);
    }

    public boolean isFollowedByUser(Long questionId, Long userId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getFollowedQuestions().contains(question);
    }
}



