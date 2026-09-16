package com.triptales.backend.service;

import com.triptales.backend.entity.Post;
import com.triptales.backend.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }

    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId);
    }

    public List<Post> getPostsByDestination(Long destinationId) {
        return postRepository.findByDestinationId(destinationId);
    }

    public List<Post> searchPostsByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title);
    }

    public Post updatePost(Long postId, Post updatedPost) {

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        existingPost.setUserId(updatedPost.getUserId());
        existingPost.setDestinationId(updatedPost.getDestinationId());
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setDescription(updatedPost.getDescription());
        existingPost.setTravelDate(updatedPost.getTravelDate());

        return postRepository.save(existingPost);
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
}