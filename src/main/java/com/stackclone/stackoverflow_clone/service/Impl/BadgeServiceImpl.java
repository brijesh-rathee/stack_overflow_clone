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
            Badge badge = badgeRepository.findByNameIgnoreCase(badgeName).orElseThrow();
            user.getBadges().add(badge);
            userRepository.save(user);
        }
    }

    @Override
    public List<Badge> getUserBadges(User user) {
        return List.copyOf(user.getBadges());
    }

    @Override
    public void checkAndAssignReputationBadges(User user) {
        int rep = user.getReputation();

        removeReputationBadges(user);

        if (rep >= 30) {
            awardBadgeToUser(user, "Legend");
            awardBadgeToUser(user, "Expert");
            awardBadgeToUser(user, "Contributor");
        } else if (rep >= 20) {
            awardBadgeToUser(user, "Expert");
            awardBadgeToUser(user, "Contributor");
        } else if (rep >= 10) {
            awardBadgeToUser(user, "Contributor");
        }
    }

    @Override
    public void removeReputationBadges(User user) {
        user.getBadges().removeIf(badge ->
                badge.getName().equals("Legend") ||
                        badge.getName().equals("Expert") ||
                        badge.getName().equals("Contributor"));
        userRepository.save(user);
    }
}
