package com.triptales.backend.service;

import com.triptales.backend.entity.Like;
import com.triptales.backend.repository.LikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public Like createLike(Like like) {
        if (likeRepository.existsByUserIdAndPostId(
                like.getUserId(), like.getPostId())) {
            throw new RuntimeException("User has already liked this post");
        }

        return likeRepository.save(like);
    }

    public List<Like> getAllLikes() {
        return likeRepository.findAll();
    }

    public Optional<Like> getLikeById(Long likeId) {
        return likeRepository.findById(likeId);
    }

    public List<Like> getLikesByPost(Long postId) {
        return likeRepository.findByPostId(postId);
    }

    public List<Like> getLikesByUser(Long userId) {
        return likeRepository.findByUserId(userId);
    }

    public boolean hasUserLikedPost(Long userId, Long postId) {
        return likeRepository.existsByUserIdAndPostId(userId, postId);
    }

    public void deleteLike(Long likeId) {
        likeRepository.deleteById(likeId);
    }

    public void removeLike(Long userId, Long postId) {
        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        likeRepository.delete(like);
    }
}