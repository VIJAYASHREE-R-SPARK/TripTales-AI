package com.triptales.backend.controller;

import com.triptales.backend.entity.Comment;
import com.triptales.backend.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(
            @RequestBody Comment comment) {
        return ResponseEntity.ok(
                commentService.createComment(comment)
        );
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        return ResponseEntity.ok(
                commentService.getAllComments()
        );
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getCommentById(
            @PathVariable Long commentId) {

        return commentService.getCommentById(commentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(
            @PathVariable Long postId) {

        return ResponseEntity.ok(
                commentService.getCommentsByPost(postId)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                commentService.getCommentsByUser(userId)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<Comment>> searchComments(
            @RequestParam String content) {

        return ResponseEntity.ok(
                commentService.searchComments(content)
        );
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long commentId,
            @RequestBody Comment comment) {

        return ResponseEntity.ok(
                commentService.updateComment(commentId, comment)
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId) {

        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}