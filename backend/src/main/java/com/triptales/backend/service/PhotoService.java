package com.triptales.backend.service;

import com.triptales.backend.entity.Photo;
import com.triptales.backend.repository.PhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    private final Path uploadDirectory =
            Paths.get("uploads").toAbsolutePath().normalize();

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;

        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
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
            throw new RuntimeException("Please select an image file");
        }

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            throw new RuntimeException("Invalid file name");
        }

        String extension = "";

        int lastDot = originalFileName.lastIndexOf(".");

        if (lastDot >= 0) {
            extension = originalFileName.substring(lastDot);
        }

        String fileName =
                UUID.randomUUID() + extension;

        Path targetLocation =
                uploadDirectory.resolve(fileName);

        try {

            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not save image file",
                    e
            );
        }

        Photo photo = new Photo();

        photo.setPostId(postId);

        photo.setImageUrl(
                "/uploads/" + fileName
        );

        photo.setCaption(caption);

        photo.setDisplayOrder(
                displayOrder != null ? displayOrder : 1
        );

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
                .orElseThrow(
                        () -> new RuntimeException("Photo not found")
                );

        existingPhoto.setPostId(
                updatedPhoto.getPostId()
        );

        existingPhoto.setImageUrl(
                updatedPhoto.getImageUrl()
        );

        existingPhoto.setCaption(
                updatedPhoto.getCaption()
        );

        existingPhoto.setDisplayOrder(
                updatedPhoto.getDisplayOrder()
        );

        return photoRepository.save(existingPhoto);
    }

    public void deletePhoto(Long photoId) {

        photoRepository.deleteById(photoId);
    }
}