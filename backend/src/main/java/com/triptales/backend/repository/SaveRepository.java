package com.triptales.backend.repository;

import com.triptales.backend.entity.Save;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SaveRepository extends JpaRepository<Save, Long> {

    List<Save> findByPostId(Long postId);

    List<Save> findByUserId(Long userId);

    Optional<Save> findByUserIdAndPostId(Long userId, Long postId);

    boolean existsByUserIdAndPostId(Long userId, Long postId);
}