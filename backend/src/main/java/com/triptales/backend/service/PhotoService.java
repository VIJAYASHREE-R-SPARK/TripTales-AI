
package com.triptales.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.triptales.backend.entity.Photo;
import com.triptales.backend.repository.PhotoRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final Cloudinary cloudinary;

    public PhotoService(
            PhotoRepository photoRepository,
            Cloudinary cloudinary) {

        this.photoRepository = photoRepository;
        this.cloudinary = cloudinary;
    }

    public Photo createPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public Photo uploadPhoto(
            Long postId,
            MultipartFile file,
            String caption,
            Integer displayOrder) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException(
                    "Please select an image file"
            );
        }

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null
                || originalFileName.isBlank()) {

            throw new RuntimeException(
                    "Invalid file name"
            );
        }

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "triptales",
                            "resource_type", "image"
                    )
            );

            String imageUrl =
                    uploadResult.get("secure_url").toString();

            Photo photo = new Photo();

            photo.setPostId(postId);
            photo.setImageUrl(imageUrl);
            photo.setCaption(caption);

            photo.setDisplayOrder(
                    displayOrder != null ? displayOrder : 1
            );

            return photoRepository.save(photo);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not upload image to Cloudinary",
                    e
            );
        }
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
        return photoRepository
                .findByCaptionContainingIgnoreCase(caption);
    }

    public Photo updatePhoto(
            Long photoId,
            Photo updatedPhoto) {

        Photo existingPhoto = photoRepository.findById(photoId)
                .orElseThrow(
                        () -> new RuntimeException("Photo not found")
                );

        existingPhoto.setPostId(updatedPhoto.getPostId());
        existingPhoto.setImageUrl(updatedPhoto.getImageUrl());
        existingPhoto.setCaption(updatedPhoto.getCaption());
        existingPhoto.setDisplayOrder(
                updatedPhoto.getDisplayOrder()
        );

        return photoRepository.save(existingPhoto);
    }

    public void deletePhoto(Long photoId) {
        photoRepository.deleteById(photoId);
    }
}
