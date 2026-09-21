package com.triptales.backend.repository;

import com.triptales.backend.entity.TravelMemory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelMemoryRepository
        extends JpaRepository<TravelMemory, Long> {

    List<TravelMemory> findByUserId(Long userId);

    List<TravelMemory> findByDestinationId(Long destinationId);

    List<TravelMemory> findByTitleContainingIgnoreCase(String title);
}