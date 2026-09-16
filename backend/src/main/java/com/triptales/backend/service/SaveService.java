package com.triptales.backend.service;

import com.triptales.backend.entity.Save;
import com.triptales.backend.repository.SaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaveService {

    private final SaveRepository saveRepository;

    public SaveService(SaveRepository saveRepository) {
        this.saveRepository = saveRepository;
    }

    public Save createSave(Save save) {
        if (saveRepository.existsByUserIdAndPostId(
                save.getUserId(), save.getPostId())) {
            throw new RuntimeException("User has already saved this post");
        }

        return saveRepository.save(save);
    }

    public List<Save> getAllSaves() {
        return saveRepository.findAll();
    }

    public Optional<Save> getSaveById(Long saveId) {
        return saveRepository.findById(saveId);
    }

    public List<Save> getSavesByPost(Long postId) {
        return saveRepository.findByPostId(postId);
    }

    public List<Save> getSavesByUser(Long userId) {
        return saveRepository.findByUserId(userId);
    }

    public boolean hasUserSavedPost(Long userId, Long postId) {
        return saveRepository.existsByUserIdAndPostId(userId, postId);
    }

    public void deleteSave(Long saveId) {
        saveRepository.deleteById(saveId);
    }

    public void removeSave(Long userId, Long postId) {
        Save save = saveRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new RuntimeException("Save not found"));

        saveRepository.delete(save);
    }
}