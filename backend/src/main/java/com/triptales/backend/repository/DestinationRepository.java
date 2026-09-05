package com.triptales.backend.repository;

import com.triptales.backend.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

    List<Destination> findByCountry(String country);

    List<Destination> findByCity(String city);

    List<Destination> findByNameContainingIgnoreCase(String name);
}