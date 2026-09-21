package com.triptales.backend.repository;

import com.triptales.backend.entity.TripDestination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripDestinationRepository
        extends JpaRepository<TripDestination, Long> {

    List<TripDestination> findByTripId(Long tripId);

    List<TripDestination> findByDestinationId(Long destinationId);

    Optional<TripDestination> findByTripIdAndDestinationId(
            Long tripId,
            Long destinationId
    );

    boolean existsByTripIdAndDestinationId(
            Long tripId,
            Long destinationId
    );
}