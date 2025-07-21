package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Answer;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.enums.VoteType;

public interface VoteService {
    void voteQuestion(Long questionId, VoteType voteType);

    void voteAnswer(Long answerId, VoteType voteType);

    int getAnswerScore(Answer answer);

    int getQuestionScore(Question currentQuestion);
}
