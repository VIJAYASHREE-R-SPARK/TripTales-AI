package com.triptales.backend.controller;

import com.triptales.backend.entity.Trip;
import com.triptales.backend.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "http://localhost:5173")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<Trip> createTrip(
            @RequestBody Trip trip) {

        return ResponseEntity.ok(
                tripService.createTrip(trip)
        );
    }

    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {

        return ResponseEntity.ok(
                tripService.getAllTrips()
        );
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripById(
            @PathVariable Long tripId) {

        return tripService
                .getTripById(tripId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Trip>> getTripsByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                tripService.getTripsByUser(userId)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Trip>> getTripsByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                tripService.getTripsByStatus(status)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<Trip>> searchTrips(
            @RequestParam String title) {

        return ResponseEntity.ok(
                tripService.searchTrips(title)
        );
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<Trip> updateTrip(
            @PathVariable Long tripId,
            @RequestBody Trip trip) {

        return ResponseEntity.ok(
                tripService.updateTrip(tripId, trip)
        );
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(
            @PathVariable Long tripId) {

        tripService.deleteTrip(tripId);

        return ResponseEntity.noContent().build();
    }
}