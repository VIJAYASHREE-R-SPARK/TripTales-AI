package com.triptales.backend.service;

import com.triptales.backend.entity.TripDestination;
import com.triptales.backend.repository.TripDestinationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripDestinationService {

    private final TripDestinationRepository tripDestinationRepository;

    public TripDestinationService(
            TripDestinationRepository tripDestinationRepository) {

        this.tripDestinationRepository =
                tripDestinationRepository;
    }

    public TripDestination addDestination(
            TripDestination tripDestination) {

        if (tripDestinationRepository
                .existsByTripIdAndDestinationId(
                        tripDestination.getTripId(),
                        tripDestination.getDestinationId())) {

            throw new RuntimeException(
                    "Destination already exists in this trip");
        }

        return tripDestinationRepository.save(tripDestination);
    }

    public List<TripDestination> getAllTripDestinations() {
        return tripDestinationRepository.findAll();
    }

    public Optional<TripDestination> getById(
            Long tripDestinationId) {

        return tripDestinationRepository
                .findById(tripDestinationId);
    }

    public List<TripDestination> getDestinationsByTrip(
            Long tripId) {

        return tripDestinationRepository.findByTripId(tripId);
    }

    public List<TripDestination> getTripsByDestination(
            Long destinationId) {

        return tripDestinationRepository
                .findByDestinationId(destinationId);
    }

    public TripDestination updateTripDestination(
            Long tripDestinationId,
            TripDestination updated) {

        TripDestination existing =
                tripDestinationRepository
                        .findById(tripDestinationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Trip destination not found"));

        existing.setTripId(updated.getTripId());
        existing.setDestinationId(updated.getDestinationId());
        existing.setVisitOrder(updated.getVisitOrder());
        existing.setNotes(updated.getNotes());

        return tripDestinationRepository.save(existing);
    }

    public void deleteTripDestination(
            Long tripDestinationId) {

        tripDestinationRepository
                .deleteById(tripDestinationId);
    }
}