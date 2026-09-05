package com.triptales.backend.service;

import com.triptales.backend.entity.Destination;
import com.triptales.backend.repository.DestinationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    public Destination createDestination(Destination destination) {
        return destinationRepository.save(destination);
    }

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Optional<Destination> getDestinationById(Long destinationId) {
        return destinationRepository.findById(destinationId);
    }

    public List<Destination> searchByName(String name) {
        return destinationRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Destination> getByCity(String city) {
        return destinationRepository.findByCity(city);
    }

    public List<Destination> getByCountry(String country) {
        return destinationRepository.findByCountry(country);
    }

    public Destination updateDestination(Long destinationId, Destination updatedDestination) {
        Destination existing = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        existing.setName(updatedDestination.getName());
        existing.setCity(updatedDestination.getCity());
        existing.setState(updatedDestination.getState());
        existing.setCountry(updatedDestination.getCountry());
        existing.setLatitude(updatedDestination.getLatitude());
        existing.setLongitude(updatedDestination.getLongitude());
        existing.setDescription(updatedDestination.getDescription());
        existing.setCoverImage(updatedDestination.getCoverImage());

        return destinationRepository.save(existing);
    }

    public void deleteDestination(Long destinationId) {
        destinationRepository.deleteById(destinationId);
    }
}