package com.triptales.backend.service;

import com.triptales.backend.entity.UserNotificationSetting;
import com.triptales.backend.repository.UserNotificationSettingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserNotificationSettingService {

    private final UserNotificationSettingRepository notificationRepository;

    public UserNotificationSettingService(
            UserNotificationSettingRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public UserNotificationSetting createNotificationSetting(
            UserNotificationSetting setting) {
        return notificationRepository.save(setting);
    }

    public List<UserNotificationSetting> getAllNotificationSettings() {
        return notificationRepository.findAll();
    }

    public Optional<UserNotificationSetting> getNotificationSettingById(Long id) {
        return notificationRepository.findById(id);
    }

    public Optional<UserNotificationSetting> getNotificationSettingByUserId(
            Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public UserNotificationSetting updateNotificationSetting(
            Long id,
            UserNotificationSetting updatedSetting) {

        UserNotificationSetting existing =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification setting not found"));

        existing.setUserId(updatedSetting.getUserId());

        existing.setLikeNotifications(
                updatedSetting.getLikeNotifications());

        existing.setCommentNotifications(
                updatedSetting.getCommentNotifications());

        existing.setFollowNotifications(
                updatedSetting.getFollowNotifications());

        existing.setRecommendationNotifications(
                updatedSetting.getRecommendationNotifications());

        return notificationRepository.save(existing);
    }

    public void deleteNotificationSetting(Long id) {

        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException(
                    "Notification setting not found");
        }

        notificationRepository.deleteById(id);
    }
}