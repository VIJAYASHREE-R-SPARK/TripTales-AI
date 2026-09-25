package com.triptales.backend.service;

import com.triptales.backend.entity.Badge;
import com.triptales.backend.repository.BadgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;

    public BadgeService(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    public Badge createBadge(Badge badge) {
        return badgeRepository.save(badge);
    }

    public List<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }

    public Optional<Badge> getBadgeById(Long badgeId) {
        return badgeRepository.findById(badgeId);
    }

    public Optional<Badge> getBadgeByName(String name) {
        return badgeRepository.findByName(name);
    }

    public Badge updateBadge(Long badgeId, Badge updatedBadge) {

        Badge existingBadge = badgeRepository.findById(badgeId)
                .orElseThrow(() ->
                        new RuntimeException("Badge not found"));

        existingBadge.setName(updatedBadge.getName());
        existingBadge.setDescription(updatedBadge.getDescription());
        existingBadge.setIcon(updatedBadge.getIcon());

        return badgeRepository.save(existingBadge);
    }

    public void deleteBadge(Long badgeId) {
        badgeRepository.deleteById(badgeId);
    }
}