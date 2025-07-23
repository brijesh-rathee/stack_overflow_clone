package com.stackclone.stackoverflow_clone.repository;

import com.stackclone.stackoverflow_clone.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
    int countByQuestionId(Long questionId);
}
