package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Badge;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.User;

import java.util.List;

public interface BadgeService {
    void awardBadgeToUser(User user, String badgeName);
    List<Badge> getUserBadges(User user);
    void checkAndAssignViewBadges(Question question);
}
