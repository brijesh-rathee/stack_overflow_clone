package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuestionService {

    List<Question> getAllQuestions();

    void createQuestion(Question question, List<Long> tagIds, MultipartFile file);

    Question getQuestionById(Long id);

    void updateQuestion(Question question, Long id, List<Long> tagIds);

    void deleteQuestion(Long id);

    List<Question> getQuestionsByUser(Long id);

    Page<Question> getPaginatedQuestions(int page, int size);

    void saveQuestion(Question question);

    Page<Question> getPaginatedQuestionsByTag(Tag tag, int page, int size);

    Page<Question> searchQuestions(String keyword, int page, int size);

    Page<Question> getFilteredAndSortedQuestions(int page, int size, Long tagId, String sort);
}
