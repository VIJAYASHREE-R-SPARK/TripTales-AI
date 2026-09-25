package com.triptales.backend.controller;

import com.triptales.backend.entity.UserPrivacySetting;
import com.triptales.backend.service.UserPrivacySettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/privacy-settings")
public class UserPrivacySettingController {

    private final UserPrivacySettingService privacyService;

    public UserPrivacySettingController(
            UserPrivacySettingService privacyService) {
        this.privacyService = privacyService;
    }

    @PostMapping
    public UserPrivacySetting createPrivacySetting(
            @RequestBody UserPrivacySetting setting) {

        return privacyService.createPrivacySetting(setting);
    }

    @GetMapping
    public List<UserPrivacySetting> getAllPrivacySettings() {

        return privacyService.getAllPrivacySettings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPrivacySetting> getPrivacySettingById(
            @PathVariable Long id) {

        return privacyService.getPrivacySettingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserPrivacySetting> getPrivacySettingByUserId(
            @PathVariable Long userId) {

        return privacyService.getPrivacySettingByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserPrivacySetting> updatePrivacySetting(
            @PathVariable Long id,
            @RequestBody UserPrivacySetting setting) {

        try {
            return ResponseEntity.ok(
                    privacyService.updatePrivacySetting(id, setting)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrivacySetting(
            @PathVariable Long id) {

        try {
            privacyService.deletePrivacySetting(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}