package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.Tag;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.repository.QuestionRepository;
import com.stackclone.stackoverflow_clone.service.CloudinaryService;
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

import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final UserService userService;
    private final TagService tagService;
    private final CloudinaryService cloudinaryService;

    private final QuestionRepository questionRepository;

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
        } else {
            System.out.println("No file uploaded or file is empty");
        }

        String content = question.getContent() != null ? question.getContent() : "";
        question.setContent(content);

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


}



