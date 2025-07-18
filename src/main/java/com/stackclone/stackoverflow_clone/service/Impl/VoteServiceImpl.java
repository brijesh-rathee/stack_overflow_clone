package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Answer;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.entity.Vote;
import com.stackclone.stackoverflow_clone.enums.VoteType;
import com.stackclone.stackoverflow_clone.repository.AnswerRepository;
import com.stackclone.stackoverflow_clone.repository.VoteRepository;
import com.stackclone.stackoverflow_clone.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final AnswerRepository answerRepository;
    private final VoteRepository voteRepository;

    public void upvoteAnswer(Long answerId, User user) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        Optional<Vote> existingVote = voteRepository.findByUserAndAnswer(user, answer);
        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();
            if (vote.getVoteType() == VoteType.DOWN) {
                vote.setVoteType(VoteType.UP);
                voteRepository.save(vote);
            }
        } else {
            Vote vote = new Vote();
            vote.setUser(user);
            vote.setAnswer(answer);
            vote.setVoteType(VoteType.UP);
            voteRepository.save(vote);
        }
    }

    public void downvoteAnswer(Long answerId, User user) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        Optional<Vote> existingVote = voteRepository.findByUserAndAnswer(user, answer);
        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();
            if (vote.getVoteType() == VoteType.UP) {
                vote.setVoteType(VoteType.DOWN);
                voteRepository.save(vote);
            }
        } else {
            Vote vote = new Vote();
            vote.setUser(user);
            vote.setAnswer(answer);
            vote.setVoteType(VoteType.DOWN);
            voteRepository.save(vote);
        }
    }

    public long getQuestionIdByAnswerId(Long answerId) {
        return answerRepository.findById(answerId)
                .map(a -> a.getQuestion().getId())
                .orElseThrow(() -> new RuntimeException("Answer or Question not found"));
    }
}
