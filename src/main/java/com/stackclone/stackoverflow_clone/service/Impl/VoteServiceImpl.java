package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Answer;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.entity.Vote;
import com.stackclone.stackoverflow_clone.enums.VoteType;
import com.stackclone.stackoverflow_clone.repository.VoteRepository;
import com.stackclone.stackoverflow_clone.service.AnswerService;
import com.stackclone.stackoverflow_clone.service.QuestionService;
import com.stackclone.stackoverflow_clone.service.UserService;
import com.stackclone.stackoverflow_clone.service.VoteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    private void updateReputation (User user, VoteType voteType) {
        int delta = 0;

        if (voteType == VoteType.UP) {
            delta = 10;
        } else if (voteType == VoteType.DOWN) {
            delta = -5;
        }

        user.setReputation(user.getReputation() + delta);
    }

    @Override
    @Transactional
    public void voteQuestion(Long questionId, VoteType voteType) {
        User currentUser = userService.getLoggedInUser();
        Question question = questionService.getQuestionById(questionId);

        Optional<Vote> existingVote = voteRepository.findByUserAndQuestion(currentUser, question);
        if (existingVote.isPresent()) {
            throw new IllegalStateException("You have already voted on this question.");
        }

        Vote vote = Vote.builder()
                .user(currentUser)
                .question(question)
                .voteType(voteType)
                .build();

        voteRepository.save(vote);

        updateReputation(question.getUser(), voteType);
    }

    @Override
    public void voteAnswer(Long answerId, VoteType voteType) {
        User currentUser = userService.getLoggedInUser();
        Answer answer = answerService.getAnswerById(answerId);

        Optional<Vote> existingVote = voteRepository.findByUserAndAnswer(currentUser, answer);
        if (existingVote.isPresent()) {
            throw new IllegalStateException("You have already voted on this answer.");
        }

        Vote vote = Vote.builder()
                .user(currentUser)
                .answer(answer)
                .voteType(voteType)
                .build();

        voteRepository.save(vote);

        updateReputation(answer.getUser(), voteType);
    }
}
