package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.enums.VoteType;

public interface VoteService {
    void voteQuestion(Long questionId, VoteType voteType);

    void voteAnswer(Long answerId, VoteType voteType);
}
