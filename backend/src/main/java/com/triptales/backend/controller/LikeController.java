package com.triptales.backend.controller;

import com.triptales.backend.entity.Like;
import com.triptales.backend.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "http://localhost:5173")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ResponseEntity<Like> createLike(
            @RequestBody Like like) {

        return ResponseEntity.ok(
                likeService.createLike(like)
        );
    }

    @GetMapping
    public ResponseEntity<List<Like>> getAllLikes() {
        return ResponseEntity.ok(
                likeService.getAllLikes()
        );
    }

    @GetMapping("/{likeId}")
    public ResponseEntity<Like> getLikeById(
            @PathVariable Long likeId) {

        return likeService.getLikeById(likeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Like>> getLikesByPost(
            @PathVariable Long postId) {

        return ResponseEntity.ok(
                likeService.getLikesByPost(postId)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Like>> getLikesByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                likeService.getLikesByUser(userId)
        );
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkLike(
            @RequestParam Long userId,
            @RequestParam Long postId) {

        return ResponseEntity.ok(
                likeService.hasUserLikedPost(userId, postId)
        );
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<Void> deleteLike(
            @PathVariable Long likeId) {

        likeService.deleteLike(likeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeLike(
            @RequestParam Long userId,
            @RequestParam Long postId) {

        likeService.removeLike(userId, postId);
        return ResponseEntity.noContent().build();
    }
}