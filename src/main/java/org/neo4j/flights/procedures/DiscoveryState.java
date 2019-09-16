package org.neo4j.flights.procedures;

import java.time.Period;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import static org.neo4j.flights.procedures.Properties.PROPERTY_COST;
import static org.neo4j.flights.procedures.Properties.PROPERTY_STOPOVERS;

public class DiscoveryState {

    public TemporalAmount duration;
    public Long stopovers;
    public Double price;

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
        increasePrice( (Double) segment.getProperty(PROPERTY_COST) );
        increaseStopovers( (Long) segment.getProperty(PROPERTY_STOPOVERS) );

        return this;
    }

}
