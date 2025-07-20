package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.User;

public interface QuestionViewService {

    void recordView(User user, Question question);
}
