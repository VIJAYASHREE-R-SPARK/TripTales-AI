package com.triptales.backend.controller;

import com.triptales.backend.entity.Photo;
import com.triptales.backend.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "http://localhost:5173")
public class PhotoController {

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    // Create photo metadata
    @PostMapping
    public ResponseEntity<Photo> createPhoto(
            @RequestBody Photo photo) {

        return ResponseEntity.ok(
                photoService.createPhoto(photo)
        );
    }

    // Upload actual image file
    @PostMapping("/upload")
    public ResponseEntity<Photo> uploadPhoto(
            @RequestParam("postId") Long postId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "displayOrder", required = false) Integer displayOrder) {

        return ResponseEntity.ok(
                photoService.uploadPhoto(
                        postId,
                        file,
                        caption,
                        displayOrder
                )
        );
    }

    // Get all photos
    @GetMapping
    public ResponseEntity<List<Photo>> getAllPhotos() {

        return ResponseEntity.ok(
                photoService.getAllPhotos()
        );
    }

    // Get photo by ID
    @GetMapping("/{photoId}")
    public ResponseEntity<Photo> getPhotoById(
            @PathVariable Long photoId) {

        return photoService.getPhotoById(photoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get photos by post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Photo>> getPhotosByPost(
            @PathVariable Long postId) {

        return ResponseEntity.ok(
                photoService.getPhotosByPost(postId)
        );
    }

    // Search photos by caption
    @GetMapping("/search")
    public ResponseEntity<List<Photo>> searchPhotos(
            @RequestParam String caption) {

        return ResponseEntity.ok(
                photoService.searchPhotosByCaption(caption)
        );
    }

    // Update photo
    @PutMapping("/{photoId}")
    public ResponseEntity<Photo> updatePhoto(
            @PathVariable Long photoId,
            @RequestBody Photo photo) {

        return ResponseEntity.ok(
                photoService.updatePhoto(photoId, photo)
        );
    }

    // Delete photo
    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(
            @PathVariable Long photoId) {

        photoService.deletePhoto(photoId);

        return ResponseEntity.noContent().build();
    }
}