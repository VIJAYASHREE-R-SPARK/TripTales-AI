package com.triptales.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_notification_settings")
public class UserNotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_settings_id")
    private Long notificationSettingId;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "likes_enabled")
    private Boolean likeNotifications = true;

    @Column(name = "comments_enabled")
    private Boolean commentNotifications = true;

    @Column(name = "followers_enabled")
    private Boolean followNotifications = true;

    @Column(name = "recommendations_enabled")
    private Boolean recommendationNotifications = true;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserNotificationSetting() {
    }

    @PrePersist
    protected void onCreate() {
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getNotificationSettingId() {
        return notificationSettingId;
    }

    public void setNotificationSettingId(Long notificationSettingId) {
        this.notificationSettingId = notificationSettingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getLikeNotifications() {
        return likeNotifications;
    }

    public void setLikeNotifications(Boolean likeNotifications) {
        this.likeNotifications = likeNotifications;
    }

    public Boolean getCommentNotifications() {
        return commentNotifications;
    }

    public void setCommentNotifications(Boolean commentNotifications) {
        this.commentNotifications = commentNotifications;
    }

    public Boolean getFollowNotifications() {
        return followNotifications;
    }

    public void setFollowNotifications(Boolean followNotifications) {
        this.followNotifications = followNotifications;
    }

    public Boolean getRecommendationNotifications() {
        return recommendationNotifications;
    }

    public void setRecommendationNotifications(Boolean recommendationNotifications) {
        this.recommendationNotifications = recommendationNotifications;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}