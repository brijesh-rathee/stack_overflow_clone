package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Badge;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.repository.BadgeRepository;
import com.stackclone.stackoverflow_clone.repository.UserRepository;
import com.stackclone.stackoverflow_clone.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {
    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;

    @Override
    public void awardBadgeToUser(User user, String badgeName) {
        boolean alreadyHasBadge = user.getBadges().stream()
                .anyMatch(b -> b.getName().equalsIgnoreCase(badgeName));

        if (!alreadyHasBadge) {
            Badge badge = badgeRepository.findByName(badgeName)
                    .orElseThrow(() -> new RuntimeException("Badge not found: " + badgeName));

            user.getBadges().add(badge);
            userRepository.save(user);
        }
    }

    @Override
    public List<Badge> getUserBadges(User user) {
        return List.copyOf(user.getBadges());
    }

    @Override
    public void checkAndAssignViewBadges(Question question) {
        int views = question.getViewCount();
        User user = question.getUser();

        if (views >= 500) {
            awardBadgeToUser(user, "GOLD");
        } else if (views >= 200) {
            awardBadgeToUser(user, "SILVER");
        } else if (views >= 100) {
            awardBadgeToUser(user, "BRONZE");
        }
    }
}
