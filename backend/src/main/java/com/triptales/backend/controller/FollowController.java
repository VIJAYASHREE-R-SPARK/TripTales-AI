package com.triptales.backend.controller;

import com.triptales.backend.entity.Follow;
import com.triptales.backend.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@CrossOrigin(origins = "http://localhost:5173")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping
    public ResponseEntity<Follow> createFollow(
            @RequestBody Follow follow) {

        return ResponseEntity.ok(
                followService.createFollow(follow)
        );
    }

    @GetMapping
    public ResponseEntity<List<Follow>> getAllFollows() {
        return ResponseEntity.ok(
                followService.getAllFollows()
        );
    }

    @GetMapping("/{followId}")
    public ResponseEntity<Follow> getFollowById(
            @PathVariable Long followId) {

        return followService.getFollowById(followId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<Follow>> getFollowers(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                followService.getFollowers(userId)
        );
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<List<Follow>> getFollowing(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                followService.getFollowing(userId)
        );
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFollowing(
            @RequestParam Long followerId,
            @RequestParam Long followingId) {

        return ResponseEntity.ok(
                followService.isFollowing(
                        followerId,
                        followingId
                )
        );
    }

    @DeleteMapping("/{followId}")
    public ResponseEntity<Void> deleteFollow(
            @PathVariable Long followId) {

        followService.deleteFollow(followId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFollow(
            @RequestParam Long followerId,
            @RequestParam Long followingId) {

        followService.removeFollow(
                followerId,
                followingId
        );

        return ResponseEntity.noContent().build();
    }
}