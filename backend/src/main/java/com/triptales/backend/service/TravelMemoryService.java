package com.triptales.backend.service;

import com.triptales.backend.entity.TravelMemory;
import com.triptales.backend.repository.TravelMemoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TravelMemoryService {

    private final TravelMemoryRepository travelMemoryRepository;

    public TravelMemoryService(
            TravelMemoryRepository travelMemoryRepository) {
        this.travelMemoryRepository = travelMemoryRepository;
    }

    public TravelMemory createMemory(TravelMemory memory) {
        return travelMemoryRepository.save(memory);
    }

    public List<TravelMemory> getAllMemories() {
        return travelMemoryRepository.findAll();
    }

    public Optional<TravelMemory> getMemoryById(Long memoryId) {
        return travelMemoryRepository.findById(memoryId);
    }

    public List<TravelMemory> getMemoriesByUser(Long userId) {
        return travelMemoryRepository.findByUserId(userId);
    }

    public List<TravelMemory> getMemoriesByDestination(Long destinationId) {
        return travelMemoryRepository.findByDestinationId(destinationId);
    }

    public List<TravelMemory> searchMemories(String title) {
        return travelMemoryRepository
                .findByTitleContainingIgnoreCase(title);
    }

    public TravelMemory updateMemory(
            Long memoryId,
            TravelMemory updatedMemory) {

        TravelMemory existingMemory =
                travelMemoryRepository.findById(memoryId)
                        .orElseThrow(() ->
                                new RuntimeException("Travel memory not found"));

        existingMemory.setUserId(updatedMemory.getUserId());
        existingMemory.setDestinationId(
                updatedMemory.getDestinationId()
        );
        existingMemory.setTitle(updatedMemory.getTitle());
        existingMemory.setDescription(
                updatedMemory.getDescription()
        );
        existingMemory.setMemoryDate(
                updatedMemory.getMemoryDate()
        );
        existingMemory.setCoverImage(
                updatedMemory.getCoverImage()
        );

        return travelMemoryRepository.save(existingMemory);
    }

    public void deleteMemory(Long memoryId) {
        travelMemoryRepository.deleteById(memoryId);
    }
}