package com.stackclone.stackoverflow_clone.repository;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.QuestionView;
import com.stackclone.stackoverflow_clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionViewRepository extends JpaRepository<QuestionView, Long> {

    boolean existsByUserAndQuestion(User user, Question question);
}
