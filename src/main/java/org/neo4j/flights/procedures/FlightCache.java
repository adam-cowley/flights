package org.neo4j.flights.procedures;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Cache;
import org.neo4j.graphdb.Node;

import java.util.concurrent.TimeUnit;

public class FlightCache {

    public static class Flight {
        Node start;
        Node end;

        public Flight(Node start, Node end) {
            this.start = start;
            this.end = end;
        }
    }

    public static Cache<Flight, Double> create() {
        return Caffeine.newBuilder()
                .expireAfterWrite(20, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build();
    }

}
