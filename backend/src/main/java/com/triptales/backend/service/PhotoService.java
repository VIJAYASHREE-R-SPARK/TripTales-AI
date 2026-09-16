package com.triptales.backend.service;

import com.triptales.backend.entity.Photo;
import com.triptales.backend.repository.PhotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Photo createPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public Optional<Photo> getPhotoById(Long photoId) {
        return photoRepository.findById(photoId);
    }

    public List<Photo> getPhotosByPost(Long postId) {
        return photoRepository.findByPostId(postId);
    }

    public List<Photo> searchPhotosByCaption(String caption) {
        return photoRepository.findByCaptionContainingIgnoreCase(caption);
    }

    public Photo updatePhoto(Long photoId, Photo updatedPhoto) {

        Photo existingPhoto = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        existingPhoto.setPostId(updatedPhoto.getPostId());
        existingPhoto.setImageUrl(updatedPhoto.getImageUrl());
        existingPhoto.setCaption(updatedPhoto.getCaption());
        existingPhoto.setDisplayOrder(updatedPhoto.getDisplayOrder());

        return photoRepository.save(existingPhoto);
    }

    public void deletePhoto(Long photoId) {
        photoRepository.deleteById(photoId);
    }
}