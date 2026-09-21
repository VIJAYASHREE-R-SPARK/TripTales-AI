package com.triptales.backend.controller;

import com.triptales.backend.entity.Recommendation;
import com.triptales.backend.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "http://localhost:5173")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(
            RecommendationService recommendationService) {

        this.recommendationService =
                recommendationService;
    }

    @PostMapping
    public ResponseEntity<Recommendation> createRecommendation(
            @RequestBody Recommendation recommendation) {

        return ResponseEntity.ok(
                recommendationService
                        .createRecommendation(recommendation)
        );
    }

    @GetMapping
    public ResponseEntity<List<Recommendation>>
    getAllRecommendations() {

        return ResponseEntity.ok(
                recommendationService
                        .getAllRecommendations()
        );
    }

    @GetMapping("/{recommendationId}")
    public ResponseEntity<Recommendation> getRecommendationById(
            @PathVariable Long recommendationId) {

        return recommendationService
                .getRecommendationById(recommendationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>>
    getRecommendationsByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                recommendationService
                        .getRecommendationsByUser(userId)
        );
    }

    @GetMapping("/destination/{destinationId}")
    public ResponseEntity<List<Recommendation>>
    getRecommendationsByDestination(
            @PathVariable Long destinationId) {

        return ResponseEntity.ok(
                recommendationService
                        .getRecommendationsByDestination(
                                destinationId
                        )
        );
    }

    @GetMapping("/user/{userId}/top")
    public ResponseEntity<List<Recommendation>>
    getTopRecommendationsForUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                recommendationService
                        .getTopRecommendationsForUser(userId)
        );
    }

    @PutMapping("/{recommendationId}")
    public ResponseEntity<Recommendation>
    updateRecommendation(
            @PathVariable Long recommendationId,
            @RequestBody Recommendation recommendation) {

        return ResponseEntity.ok(
                recommendationService.updateRecommendation(
                        recommendationId,
                        recommendation
                )
        );
    }

    @DeleteMapping("/{recommendationId}")
    public ResponseEntity<Void> deleteRecommendation(
            @PathVariable Long recommendationId) {

        recommendationService.deleteRecommendation(
                recommendationId
        );

        return ResponseEntity.noContent().build();
    }
}
