package org.neo4j.flights.procedures.services.flightsbetween;

import java.time.Period;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;

import org.neo4j.graphdb.Node;

import static org.neo4j.flights.procedures.Properties.PRICE;
import static org.neo4j.flights.procedures.Properties.STOPOVERS;

public class DiscoveryState {

    public TemporalAmount duration;
    public Long stopovers;
    private Double price;

    public ZonedDateTime minimumDeparture;

    public DiscoveryState() {
        this.duration = Period.of(0, 0, 0);
        this.stopovers = 0L;
        this.price = 0.0d;
    }

    public DiscoveryState(DiscoveryState discoveryState) {
        this.duration = discoveryState.duration;
        this.stopovers = discoveryState.stopovers;
        this.price = discoveryState.price;
    }

    public DiscoveryState increasePrice(Double price) {
        this.price = this.price + price;

        return this;
    }

    public DiscoveryState increaseStopovers(Long segments) {
        this.stopovers = this.stopovers + segments + 1;

        return this;
    }

    public ZonedDateTime getMinimumDeparture() {
        return this.minimumDeparture;
    }

    public Long getStopovers() {
        return this.stopovers;
    }

    public Double getPrice() {
        return this.price;
    }

    public DiscoveryState setMinimumDeparture(ZonedDateTime time) {
        this.minimumDeparture = time;

        return this;
    }

    public DiscoveryState copy() {
        return new DiscoveryState( this );
    }

    public DiscoveryState applySegment( Node segment ) {
        increasePrice( (Double) segment.getProperty(PRICE) );
        increaseStopovers( (Long) segment.getProperty(STOPOVERS) );

        return this;
    }

}
