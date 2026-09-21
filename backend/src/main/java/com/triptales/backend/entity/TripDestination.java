package com.triptales.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "trip_destinations",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_trip_destination",
            columnNames = {"trip_id", "destination_id"}
        ),
        @UniqueConstraint(
            name = "unique_trip_visit_order",
            columnNames = {"trip_id", "visit_order"}
        )
    }
)
public class TripDestination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripDestinationId;

    @Column(name = "trip_id", nullable = false)
    private Long tripId;

    @Column(name = "destination_id", nullable = false)
    private Long destinationId;

    @Column(name = "visit_order", nullable = false)
    private Integer visitOrder;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public TripDestination() {
    }

    public Long getTripDestinationId() {
        return tripDestinationId;
    }

    public void setTripDestinationId(Long tripDestinationId) {
        this.tripDestinationId = tripDestinationId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public Integer getVisitOrder() {
        return visitOrder;
    }

    public void setVisitOrder(Integer visitOrder) {
        this.visitOrder = visitOrder;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}