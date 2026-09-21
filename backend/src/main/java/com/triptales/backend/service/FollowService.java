package com.triptales.backend.service;

import com.triptales.backend.entity.Follow;
import com.triptales.backend.repository.FollowRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public Follow createFollow(Follow follow) {

        if (follow.getFollowerId().equals(follow.getFollowingId())) {
            throw new RuntimeException("User cannot follow themselves");
        }

        if (followRepository.existsByFollowerIdAndFollowingId(
                follow.getFollowerId(),
                follow.getFollowingId())) {
            throw new RuntimeException("User is already following this user");
        }

        return followRepository.save(follow);
    }

    public List<Follow> getAllFollows() {
        return followRepository.findAll();
    }

    public Optional<Follow> getFollowById(Long followId) {
        return followRepository.findById(followId);
    }

    public List<Follow> getFollowers(Long userId) {
        return followRepository.findByFollowingId(userId);
    }

    public List<Follow> getFollowing(Long userId) {
        return followRepository.findByFollowerId(userId);
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(
                followerId,
                followingId
        );
    }

    public void deleteFollow(Long followId) {
        followRepository.deleteById(followId);
    }

    public void removeFollow(Long followerId, Long followingId) {

        Follow follow = followRepository
                .findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new RuntimeException("Follow relationship not found"));

        followRepository.delete(follow);
    }
}