package com.triptales.backend.controller;

import com.triptales.backend.entity.Destination;
import com.triptales.backend.service.DestinationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
@CrossOrigin(origins = "http://localhost:5173")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @PostMapping
    public ResponseEntity<Destination> createDestination(
            @RequestBody Destination destination) {
        return ResponseEntity.ok(
                destinationService.createDestination(destination)
        );
    }

    @GetMapping
    public ResponseEntity<List<Destination>> getAllDestinations() {
        return ResponseEntity.ok(
                destinationService.getAllDestinations()
        );
    }

    @GetMapping("/{destinationId}")
    public ResponseEntity<Destination> getDestinationById(
            @PathVariable Long destinationId) {

        return destinationService.getDestinationById(destinationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Destination>> searchByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                destinationService.searchByName(name)
        );
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Destination>> getByCity(
            @PathVariable String city) {

        return ResponseEntity.ok(
                destinationService.getByCity(city)
        );
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<Destination>> getByCountry(
            @PathVariable String country) {

        return ResponseEntity.ok(
                destinationService.getByCountry(country)
        );
    }

    @PutMapping("/{destinationId}")
    public ResponseEntity<Destination> updateDestination(
            @PathVariable Long destinationId,
            @RequestBody Destination destination) {

        return ResponseEntity.ok(
                destinationService.updateDestination(
                        destinationId,
                        destination
                )
        );
    }

    @DeleteMapping("/{destinationId}")
    public ResponseEntity<Void> deleteDestination(
            @PathVariable Long destinationId) {

        destinationService.deleteDestination(destinationId);
        return ResponseEntity.noContent().build();
    }
}