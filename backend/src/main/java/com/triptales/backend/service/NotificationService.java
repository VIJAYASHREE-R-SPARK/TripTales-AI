package com.triptales.backend.service;

import com.triptales.backend.entity.Notification;
import com.triptales.backend.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getNotificationsByUserAndReadStatus(
            Long userId, Boolean isRead) {
        return notificationRepository.findByUserIdAndIsRead(userId, isRead);
    }

    public Notification updateNotification(
            Long id, Notification updatedNotification) {

        Notification existing = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        existing.setUserId(updatedNotification.getUserId());
        existing.setType(updatedNotification.getType());
        existing.setMessage(updatedNotification.getMessage());
        existing.setReferenceId(updatedNotification.getReferenceId());
        existing.setIsRead(updatedNotification.getIsRead());

        return notificationRepository.save(existing);
    }

    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found");
        }

        notificationRepository.deleteById(id);
    }
}