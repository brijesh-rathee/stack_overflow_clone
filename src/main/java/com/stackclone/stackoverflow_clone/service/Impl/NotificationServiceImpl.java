package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Notification;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.repository.NotificationRepository;
import com.stackclone.stackoverflow_clone.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> getAndMarkNotificationsAsRead(User user) {
        List<Notification> notifications = notificationRepository.findByRecipient(user);

        for (Notification notification : notifications) {
            notification.setRead(true);
        }

        notificationRepository.saveAll(notifications);

        return notifications;
    }

    @Override
    public long countUnreadNotifications(User user) {
        return notificationRepository.countByRecipientAndIsReadFalse(user);
    }
}
