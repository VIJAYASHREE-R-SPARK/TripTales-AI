package com.triptales.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_privacy_settings")
public class UserPrivacySetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long privacyId;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "profile_visibility", length = 20)
    private String profileVisibility = "PUBLIC";

    @Column(name = "location_visibility", length = 20)
    private String locationVisibility = "DESTINATION";

    @Column(name = "show_travel_map")
    private Boolean showTravelMap = true;

    @Column(name = "allow_follow_requests")
    private Boolean allowFollowRequests = true;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserPrivacySetting() {
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

    public Long getPrivacyId() {
        return privacyId;
    }

    public void setPrivacyId(Long privacyId) {
        this.privacyId = privacyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProfileVisibility() {
        return profileVisibility;
    }

    public void setProfileVisibility(String profileVisibility) {
        this.profileVisibility = profileVisibility;
    }

    public String getLocationVisibility() {
        return locationVisibility;
    }

    public void setLocationVisibility(String locationVisibility) {
        this.locationVisibility = locationVisibility;
    }

    public Boolean getShowTravelMap() {
        return showTravelMap;
    }

    public void setShowTravelMap(Boolean showTravelMap) {
        this.showTravelMap = showTravelMap;
    }

    public Boolean getAllowFollowRequests() {
        return allowFollowRequests;
    }

    public void setAllowFollowRequests(Boolean allowFollowRequests) {
        this.allowFollowRequests = allowFollowRequests;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}