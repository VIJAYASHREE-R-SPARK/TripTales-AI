package com.triptales.backend.controller;

import com.triptales.backend.entity.TravelMemory;
import com.triptales.backend.service.TravelMemoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travel-memories")
@CrossOrigin(origins = "http://localhost:5173")
public class TravelMemoryController {

    private final TravelMemoryService travelMemoryService;

    public TravelMemoryController(
            TravelMemoryService travelMemoryService) {
        this.travelMemoryService = travelMemoryService;
    }

    @PostMapping
    public ResponseEntity<TravelMemory> createMemory(
            @RequestBody TravelMemory memory) {

        return ResponseEntity.ok(
                travelMemoryService.createMemory(memory)
        );
    }

    @GetMapping
    public ResponseEntity<List<TravelMemory>> getAllMemories() {

        return ResponseEntity.ok(
                travelMemoryService.getAllMemories()
        );
    }

    @GetMapping("/{memoryId}")
    public ResponseEntity<TravelMemory> getMemoryById(
            @PathVariable Long memoryId) {

        return travelMemoryService
                .getMemoryById(memoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TravelMemory>> getMemoriesByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                travelMemoryService.getMemoriesByUser(userId)
        );
    }

    @GetMapping("/destination/{destinationId}")
    public ResponseEntity<List<TravelMemory>> getMemoriesByDestination(
            @PathVariable Long destinationId) {

        return ResponseEntity.ok(
                travelMemoryService.getMemoriesByDestination(destinationId)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<TravelMemory>> searchMemories(
            @RequestParam String title) {

        return ResponseEntity.ok(
                travelMemoryService.searchMemories(title)
        );
    }

    @PutMapping("/{memoryId}")
    public ResponseEntity<TravelMemory> updateMemory(
            @PathVariable Long memoryId,
            @RequestBody TravelMemory memory) {

        return ResponseEntity.ok(
                travelMemoryService.updateMemory(
                        memoryId,
                        memory
                )
        );
    }

    @DeleteMapping("/{memoryId}")
    public ResponseEntity<Void> deleteMemory(
            @PathVariable Long memoryId) {

        travelMemoryService.deleteMemory(memoryId);

        return ResponseEntity.noContent().build();
    }
}