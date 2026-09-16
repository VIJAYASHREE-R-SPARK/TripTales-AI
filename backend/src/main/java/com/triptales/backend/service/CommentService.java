package com.triptales.backend.service;

import com.triptales.backend.entity.Comment;
import com.triptales.backend.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> getCommentsByUser(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    public List<Comment> searchComments(String content) {
        return commentRepository.findByContentContainingIgnoreCase(content);
    }

    public Comment updateComment(Long commentId, Comment updatedComment) {

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        existingComment.setPostId(updatedComment.getPostId());
        existingComment.setUserId(updatedComment.getUserId());
        existingComment.setContent(updatedComment.getContent());

        return commentRepository.save(existingComment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}