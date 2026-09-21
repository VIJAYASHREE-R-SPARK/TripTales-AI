package com.triptales.backend.service;

import com.triptales.backend.entity.Trip;
import com.triptales.backend.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip createTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> getTripById(Long tripId) {
        return tripRepository.findById(tripId);
    }

    public List<Trip> getTripsByUser(Long userId) {
        return tripRepository.findByUserId(userId);
    }

    public List<Trip> getTripsByStatus(String status) {
        return tripRepository.findByStatus(status);
    }

    public List<Trip> searchTrips(String title) {
        return tripRepository.findByTitleContainingIgnoreCase(title);
    }

    public Trip updateTrip(Long tripId, Trip updatedTrip) {

        Trip existingTrip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new RuntimeException("Trip not found"));

        existingTrip.setUserId(updatedTrip.getUserId());
        existingTrip.setTitle(updatedTrip.getTitle());
        existingTrip.setDescription(updatedTrip.getDescription());
        existingTrip.setStartDate(updatedTrip.getStartDate());
        existingTrip.setEndDate(updatedTrip.getEndDate());
        existingTrip.setStatus(updatedTrip.getStatus());

        return tripRepository.save(existingTrip);
    }

    public void deleteTrip(Long tripId) {
        tripRepository.deleteById(tripId);
    }
}