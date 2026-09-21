package com.triptales.backend.service;

import com.triptales.backend.entity.Recommendation;
import com.triptales.backend.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public RecommendationService(
            RecommendationRepository recommendationRepository) {

        this.recommendationRepository =
                recommendationRepository;
    }

    public Recommendation createRecommendation(
            Recommendation recommendation) {

        return recommendationRepository.save(recommendation);
    }

    public List<Recommendation> getAllRecommendations() {

        return recommendationRepository.findAll();
    }

    public Optional<Recommendation> getRecommendationById(
            Long recommendationId) {

        return recommendationRepository
                .findById(recommendationId);
    }

    public List<Recommendation> getRecommendationsByUser(
            Long userId) {

        return recommendationRepository
                .findByUserId(userId);
    }

    public List<Recommendation> getRecommendationsByDestination(
            Long destinationId) {

        return recommendationRepository
                .findByDestinationId(destinationId);
    }

    public List<Recommendation> getTopRecommendationsForUser(
            Long userId) {

        return recommendationRepository
                .findByUserIdOrderByScoreDesc(userId);
    }

    public Recommendation updateRecommendation(
            Long recommendationId,
            Recommendation updatedRecommendation) {

        Recommendation existingRecommendation =
                recommendationRepository
                        .findById(recommendationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recommendation not found"));

        existingRecommendation.setUserId(
                updatedRecommendation.getUserId());

        existingRecommendation.setDestinationId(
                updatedRecommendation.getDestinationId());

        existingRecommendation.setReason(
                updatedRecommendation.getReason());

        existingRecommendation.setScore(
                updatedRecommendation.getScore());

        return recommendationRepository.save(
                existingRecommendation);
    }

    public void deleteRecommendation(
            Long recommendationId) {

        recommendationRepository.deleteById(
                recommendationId);
    }
}