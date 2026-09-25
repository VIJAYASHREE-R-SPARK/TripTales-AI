package com.triptales.backend.repository;

import com.triptales.backend.entity.UserPrivacySetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPrivacySettingRepository
        extends JpaRepository<UserPrivacySetting, Long> {

    Optional<UserPrivacySetting> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}