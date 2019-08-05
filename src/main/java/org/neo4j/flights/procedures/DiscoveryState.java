package org.neo4j.flights.procedures;

import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

public class DiscoveryState {

    public TemporalAmount duration;
    public Long stopovers;
    public Float price;

    public DiscoveryState() {
        this.duration = Period.of(0, 0, 0);
        this.stopovers = 0L;
        this.price = 0.0f;
    }

    public DiscoveryState(DiscoveryState discoveryState) {
        this.duration = duration;
        this.stopovers = stopovers;
    }

    public void increasePrice(Float price) {
        this.price = this.price + price;
    }

    public DiscoveryState copy() {
        return new DiscoveryState( this );
    }

}
