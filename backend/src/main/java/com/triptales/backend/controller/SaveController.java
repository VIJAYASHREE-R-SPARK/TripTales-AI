package com.triptales.backend.controller;

import com.triptales.backend.entity.Save;
import com.triptales.backend.service.SaveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saves")
@CrossOrigin(origins = "http://localhost:5173")
public class SaveController {

    private final SaveService saveService;

    public SaveController(SaveService saveService) {
        this.saveService = saveService;
    }

    @PostMapping
    public ResponseEntity<Save> createSave(
            @RequestBody Save save) {

        return ResponseEntity.ok(
                saveService.createSave(save)
        );
    }

    @GetMapping
    public ResponseEntity<List<Save>> getAllSaves() {
        return ResponseEntity.ok(
                saveService.getAllSaves()
        );
    }

    @GetMapping("/{saveId}")
    public ResponseEntity<Save> getSaveById(
            @PathVariable Long saveId) {

        return saveService.getSaveById(saveId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Save>> getSavesByPost(
            @PathVariable Long postId) {

        return ResponseEntity.ok(
                saveService.getSavesByPost(postId)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Save>> getSavesByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                saveService.getSavesByUser(userId)
        );
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkSave(
            @RequestParam Long userId,
            @RequestParam Long postId) {

        return ResponseEntity.ok(
                saveService.hasUserSavedPost(userId, postId)
        );
    }

    @DeleteMapping("/{saveId}")
    public ResponseEntity<Void> deleteSave(
            @PathVariable Long saveId) {

        saveService.deleteSave(saveId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeSave(
            @RequestParam Long userId,
            @RequestParam Long postId) {

        saveService.removeSave(userId, postId);
        return ResponseEntity.noContent().build();
    }
}