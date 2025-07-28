package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Notification;
import com.stackclone.stackoverflow_clone.entity.User;

import java.util.List;

public interface NotificationService {
    List<Notification> getAndMarkNotificationsAsRead(User user);

    long countUnreadNotifications(User user);
}
