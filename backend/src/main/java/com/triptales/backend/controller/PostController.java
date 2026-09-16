package com.triptales.backend.controller;

import com.triptales.backend.entity.Post;
import com.triptales.backend.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:5173")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.createPost(post));
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(
            @PathVariable Long postId) {

        return postService.getPostById(postId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                postService.getPostsByUser(userId)
        );
    }

    @GetMapping("/destination/{destinationId}")
    public ResponseEntity<List<Post>> getPostsByDestination(
            @PathVariable Long destinationId) {

        return ResponseEntity.ok(
                postService.getPostsByDestination(destinationId)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPosts(
            @RequestParam String title) {

        return ResponseEntity.ok(
                postService.searchPostsByTitle(title)
        );
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestBody Post post) {

        return ResponseEntity.ok(
                postService.updatePost(postId, post)
        );
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId) {

        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}