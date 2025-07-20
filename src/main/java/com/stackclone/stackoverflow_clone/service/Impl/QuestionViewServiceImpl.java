package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.QuestionView;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.repository.QuestionRepository;
import com.stackclone.stackoverflow_clone.repository.QuestionViewRepository;
import com.stackclone.stackoverflow_clone.service.QuestionViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionViewServiceImpl implements QuestionViewService {
    private final QuestionViewRepository questionViewRepository;
    private final QuestionRepository questionRepository;

    @Override
    public void recordView(User user, Question question) {
        boolean alreadyViewed = questionViewRepository.existsByUserAndQuestion(user, question);

        if (!alreadyViewed) {
            QuestionView view = QuestionView.builder()
                    .user(user)
                    .question(question)
                    .build();

            questionViewRepository.save(view);

            question.setViewCount(question.getViewCount() + 1);
            questionRepository.save(question);
        }
    }
}
