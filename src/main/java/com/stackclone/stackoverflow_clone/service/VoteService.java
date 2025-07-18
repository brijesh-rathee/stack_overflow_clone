package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.User;

public interface VoteService {

    void upvoteAnswer(Long answerId, User user);

    void downvoteAnswer(Long answerId, User user);

    long getQuestionIdByAnswerId(Long answerId);
}
