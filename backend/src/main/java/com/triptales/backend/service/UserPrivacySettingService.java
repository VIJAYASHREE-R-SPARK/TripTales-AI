package com.triptales.backend.service;

import com.triptales.backend.entity.UserPrivacySetting;
import com.triptales.backend.repository.UserPrivacySettingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserPrivacySettingService {

    private final UserPrivacySettingRepository privacyRepository;

    public UserPrivacySettingService(
            UserPrivacySettingRepository privacyRepository) {
        this.privacyRepository = privacyRepository;
    }

    public UserPrivacySetting createPrivacySetting(
            UserPrivacySetting setting) {

        return privacyRepository.save(setting);
    }

    public List<UserPrivacySetting> getAllPrivacySettings() {
        return privacyRepository.findAll();
    }

    public Optional<UserPrivacySetting> getPrivacySettingById(Long id) {
        return privacyRepository.findById(id);
    }

    public Optional<UserPrivacySetting> getPrivacySettingByUserId(Long userId) {
        return privacyRepository.findByUserId(userId);
    }

    public UserPrivacySetting updatePrivacySetting(
            Long id,
            UserPrivacySetting updatedSetting) {

        UserPrivacySetting existing = privacyRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Privacy setting not found")
                );

        existing.setUserId(updatedSetting.getUserId());
        existing.setProfileVisibility(
                updatedSetting.getProfileVisibility()
        );
        existing.setLocationVisibility(
                updatedSetting.getLocationVisibility()
        );
        existing.setShowTravelMap(
                updatedSetting.getShowTravelMap()
        );
        existing.setAllowFollowRequests(
                updatedSetting.getAllowFollowRequests()
        );

        return privacyRepository.save(existing);
    }

    public void deletePrivacySetting(Long id) {

        if (!privacyRepository.existsById(id)) {
            throw new RuntimeException("Privacy setting not found");
        }

        privacyRepository.deleteById(id);
    }
}