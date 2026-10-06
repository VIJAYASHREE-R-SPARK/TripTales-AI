package com.triptales.backend.service;

import com.triptales.backend.entity.Like;
import com.triptales.backend.entity.Notification;
import com.triptales.backend.entity.Post;
import com.triptales.backend.repository.LikeRepository;
import com.triptales.backend.repository.NotificationRepository;
import com.triptales.backend.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;

    public LikeService(
            LikeRepository likeRepository,
            PostRepository postRepository,
            NotificationRepository notificationRepository) {

        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.notificationRepository = notificationRepository;
    }

    public Like createLike(Like like) {

        if (likeRepository.existsByUserIdAndPostId(
                like.getUserId(),
                like.getPostId())) {

            throw new RuntimeException(
                    "User has already liked this post"
            );
        }

        Like savedLike = likeRepository.save(like);

        // Find the post that was liked
        Optional<Post> postOptional =
                postRepository.findById(like.getPostId());

        if (postOptional.isPresent()) {

            Post post = postOptional.get();

            Long postOwnerId = post.getUserId();
            Long likingUserId = like.getUserId();

            // Do not notify users when they like their own post
            if (!postOwnerId.equals(likingUserId)) {

                Notification notification =
                        new Notification();

                notification.setUserId(postOwnerId);
                notification.setType("LIKE");

                notification.setMessage(
                        "Someone liked your travel post."
                );

                notification.setReferenceId(
                        like.getPostId()
                );

                notification.setIsRead(false);

                notificationRepository.save(notification);
            }
        }

        return savedLike;
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

    public boolean hasUserLikedPost(
            Long userId,
            Long postId) {

        return likeRepository.existsByUserIdAndPostId(
                userId,
                postId
        );
    }

    public void deleteLike(Long likeId) {
        likeRepository.deleteById(likeId);
    }

    public void removeLike(
            Long userId,
            Long postId) {

        Like like =
                likeRepository
                        .findByUserIdAndPostId(
                                userId,
                                postId
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Like not found"
                                )
                        );

        likeRepository.delete(like);
    }
}