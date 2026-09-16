package com.triptales.backend.repository;

import com.triptales.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long postId);

    List<Comment> findByUserId(Long userId);

    List<Comment> findByContentContainingIgnoreCase(String content);
}