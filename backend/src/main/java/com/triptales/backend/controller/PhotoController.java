package com.triptales.backend.controller;

import com.triptales.backend.entity.Photo;
import com.triptales.backend.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "http://localhost:5173")
public class PhotoController {

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping
    public ResponseEntity<Photo> createPhoto(
            @RequestBody Photo photo) {

        return ResponseEntity.ok(
                photoService.createPhoto(photo)
        );
    }

    @GetMapping
    public ResponseEntity<List<Photo>> getAllPhotos() {

        return ResponseEntity.ok(
                photoService.getAllPhotos()
        );
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<Photo> getPhotoById(
            @PathVariable Long photoId) {

        return photoService.getPhotoById(photoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Photo>> getPhotosByPost(
            @PathVariable Long postId) {

        return ResponseEntity.ok(
                photoService.getPhotosByPost(postId)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<Photo>> searchPhotos(
            @RequestParam String caption) {

        return ResponseEntity.ok(
                photoService.searchPhotosByCaption(caption)
        );
    }

    @PutMapping("/{photoId}")
    public ResponseEntity<Photo> updatePhoto(
            @PathVariable Long photoId,
            @RequestBody Photo photo) {

        return ResponseEntity.ok(
                photoService.updatePhoto(photoId, photo)
        );
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(
            @PathVariable Long photoId) {

        photoService.deletePhoto(photoId);

        return ResponseEntity.noContent().build();
    }
}