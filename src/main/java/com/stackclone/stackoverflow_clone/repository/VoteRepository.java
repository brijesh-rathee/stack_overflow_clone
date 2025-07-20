package com.stackclone.stackoverflow_clone.repository;

import com.stackclone.stackoverflow_clone.entity.Answer;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.entity.Vote;
import com.stackclone.stackoverflow_clone.enums.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserAndQuestion(User user, Question question);

    Optional<Vote> findByUserAndAnswer(User user, Answer answer);

    long countByQuestionAndVoteType(Question question, VoteType voteType);

    long countByAnswerAndVoteType(Answer answer, VoteType voteType);

}
