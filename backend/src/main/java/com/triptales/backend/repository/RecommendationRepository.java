package com.triptales.backend.repository;

import com.triptales.backend.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository
        extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByUserId(Long userId);

    List<Recommendation> findByDestinationId(Long destinationId);

    List<Recommendation> findByUserIdOrderByScoreDesc(Long userId);
}