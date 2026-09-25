package com.triptales.backend.controller;

import com.triptales.backend.entity.Badge;
import com.triptales.backend.service.BadgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
@CrossOrigin(origins = "http://localhost:5173")
public class BadgeController {

    private final BadgeService badgeService;

    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @PostMapping
    public ResponseEntity<Badge> createBadge(
            @RequestBody Badge badge) {

        return ResponseEntity.ok(
                badgeService.createBadge(badge)
        );
    }

    @GetMapping
    public ResponseEntity<List<Badge>> getAllBadges() {

        return ResponseEntity.ok(
                badgeService.getAllBadges()
        );
    }

    @GetMapping("/{badgeId}")
    public ResponseEntity<Badge> getBadgeById(
            @PathVariable Long badgeId) {

        return badgeService.getBadgeById(badgeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Badge> getBadgeByName(
            @PathVariable String name) {

        return badgeService.getBadgeByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{badgeId}")
    public ResponseEntity<Badge> updateBadge(
            @PathVariable Long badgeId,
            @RequestBody Badge badge) {

        return ResponseEntity.ok(
                badgeService.updateBadge(
                        badgeId,
                        badge
                )
        );
    }

    @DeleteMapping("/{badgeId}")
    public ResponseEntity<Void> deleteBadge(
            @PathVariable Long badgeId) {

        badgeService.deleteBadge(badgeId);

        return ResponseEntity.noContent().build();
    }
}