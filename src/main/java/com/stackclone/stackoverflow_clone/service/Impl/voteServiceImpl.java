package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Answer;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.entity.Vote;
import com.stackclone.stackoverflow_clone.enums.VoteType;
import com.stackclone.stackoverflow_clone.repository.VoteRepository;
import com.stackclone.stackoverflow_clone.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class voteServiceImpl implements VoteService {
    private final VoteRepository voteRepository;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;
    private final BadgeService badgeService;

    @Override
    @Transactional
    public void voteQuestion(Long questionId, VoteType newVoteType) {
        User currentUser = userService.getLoggedInUser();
        Question question = questionService.getQuestionById(questionId);

        Optional<Vote> existingVoteOpt = voteRepository.findByUserAndQuestion(currentUser, question);

        if (existingVoteOpt.isPresent()) {
            Vote existingVote = existingVoteOpt.get();

            if (existingVote.getVoteType() == newVoteType) {
                return;
            }

            if (existingVote.getVoteType() == VoteType.UP) {
                question.setVoteCount(question.getVoteCount() - 1);
                question.getUser().setReputation(question.getUser().getReputation() - 10);
            } else {
                question.setVoteCount(question.getVoteCount() + 1);
                question.getUser().setReputation(question.getUser().getReputation() + 5);
            }

            existingVote.setVoteType(newVoteType);
            voteRepository.save(existingVote);
        } else {
            Vote newVote = Vote.builder()
                    .user(currentUser)
                    .question(question)
                    .voteType(newVoteType)
                    .build();

            voteRepository.save(newVote);
        }

        if (newVoteType == VoteType.UP) {
            question.setVoteCount(question.getVoteCount() + 1);
            question.getUser().setReputation(question.getUser().getReputation() + 10);
        } else {
            question.setVoteCount(question.getVoteCount() - 1);
            question.getUser().setReputation(question.getUser().getReputation() - 5);
        }
        badgeService.checkAndAssignReputationBadges(question.getUser());
    }

    @Override
    @Transactional
    public void voteAnswer(Long answerId, VoteType newVoteType) {
        User currentUser = userService.getLoggedInUser();
        Answer answer = answerService.getAnswerById(answerId);

        Optional<Vote> existingVoteOpt = voteRepository.findByUserAndAnswer(currentUser, answer);

        if (existingVoteOpt.isPresent()) {
            Vote existingVote = existingVoteOpt.get();

            if (existingVote.getVoteType() == newVoteType) {
                return;
            }

            existingVote.setVoteType(newVoteType);
            voteRepository.save(existingVote);
        } else {
            Vote newVote = Vote.builder()
                    .user(currentUser)
                    .answer(answer)
                    .voteType(newVoteType)
                    .build();
            voteRepository.save(newVote);
        }
    }

    @Override
    public int getAnswerScore(Answer answer) {
        long upvotes = voteRepository.countByAnswerAndVoteType(answer, VoteType.UP);
        long downvotes = voteRepository.countByAnswerAndVoteType(answer, VoteType.DOWN);

        return (int) (upvotes - downvotes);
    }

    @Override
    public int getQuestionScore(Question currentQuestion) {
        long upvotes = voteRepository.countByQuestionAndVoteType(currentQuestion, VoteType.UP);
        long downvotes = voteRepository.countByQuestionAndVoteType(currentQuestion, VoteType.DOWN);

        return (int) (upvotes - downvotes);
    }
}