package com.triptales.backend.controller;

import com.triptales.backend.entity.TripDestination;
import com.triptales.backend.service.TripDestinationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip-destinations")
@CrossOrigin(origins = "http://localhost:5173")
public class TripDestinationController {

    private final TripDestinationService tripDestinationService;

    public TripDestinationController(
            TripDestinationService tripDestinationService) {

        this.tripDestinationService =
                tripDestinationService;
    }

    @PostMapping
    public ResponseEntity<TripDestination> addDestination(
            @RequestBody TripDestination tripDestination) {

        return ResponseEntity.ok(
                tripDestinationService
                        .addDestination(tripDestination)
        );
    }

    @GetMapping
    public ResponseEntity<List<TripDestination>>
    getAllTripDestinations() {

        return ResponseEntity.ok(
                tripDestinationService
                        .getAllTripDestinations()
        );
    }

    @GetMapping("/{tripDestinationId}")
    public ResponseEntity<TripDestination> getById(
            @PathVariable Long tripDestinationId) {

        return tripDestinationService
                .getById(tripDestinationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<TripDestination>>
    getDestinationsByTrip(
            @PathVariable Long tripId) {

        return ResponseEntity.ok(
                tripDestinationService
                        .getDestinationsByTrip(tripId)
        );
    }

    @GetMapping("/destination/{destinationId}")
    public ResponseEntity<List<TripDestination>>
    getTripsByDestination(
            @PathVariable Long destinationId) {

        return ResponseEntity.ok(
                tripDestinationService
                        .getTripsByDestination(destinationId)
        );
    }

    @PutMapping("/{tripDestinationId}")
    public ResponseEntity<TripDestination>
    updateTripDestination(
            @PathVariable Long tripDestinationId,
            @RequestBody TripDestination tripDestination) {

        return ResponseEntity.ok(
                tripDestinationService.updateTripDestination(
                        tripDestinationId,
                        tripDestination
                )
        );
    }

    @DeleteMapping("/{tripDestinationId}")
    public ResponseEntity<Void> deleteTripDestination(
            @PathVariable Long tripDestinationId) {

        tripDestinationService.deleteTripDestination(
                tripDestinationId
        );

        return ResponseEntity.noContent().build();
    }
}