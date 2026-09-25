package com.triptales.backend.controller;

import com.triptales.backend.entity.UserNotificationSetting;
import com.triptales.backend.service.UserNotificationSettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification-settings")
public class UserNotificationSettingController {

    private final UserNotificationSettingService notificationService;

    public UserNotificationSettingController(
            UserNotificationSettingService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public UserNotificationSetting createNotificationSetting(
            @RequestBody UserNotificationSetting setting) {
        return notificationService.createNotificationSetting(setting);
    }

    @GetMapping
    public List<UserNotificationSetting> getAllNotificationSettings() {
        return notificationService.getAllNotificationSettings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserNotificationSetting> getNotificationSettingById(
            @PathVariable Long id) {

        return notificationService.getNotificationSettingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserNotificationSetting> getNotificationSettingByUserId(
            @PathVariable Long userId) {

        return notificationService
                .getNotificationSettingByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserNotificationSetting> updateNotificationSetting(
            @PathVariable Long id,
            @RequestBody UserNotificationSetting setting) {

        try {
            return ResponseEntity.ok(
                    notificationService.updateNotificationSetting(id, setting)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificationSetting(
            @PathVariable Long id) {

        try {
            notificationService.deleteNotificationSetting(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}