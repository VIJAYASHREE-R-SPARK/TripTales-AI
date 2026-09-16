package com.triptales.backend.repository;

import com.triptales.backend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserId(Long userId);

    List<Post> findByDestinationId(Long destinationId);

    List<Post> findByTitleContainingIgnoreCase(String title);
}