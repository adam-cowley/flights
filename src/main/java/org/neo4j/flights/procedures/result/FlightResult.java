package org.neo4j.flights.procedures.result;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;

import static org.neo4j.flights.procedures.Labels.Segment;
import static org.neo4j.flights.procedures.Properties.PRICE;

public class FlightResult {
    public Path path;
    public Node origin;
    public Node destination;

//    private TemporalAmount duration;
    public Double distance;
    public Double cost;

    public LocalDateTime startedAt;
    public LocalDateTime routeAvailableAt;
    public LocalDateTime resultAvailableAt;

    public FlightResult() {}

    public FlightResult(Path path, Node origin, Node destination, LocalDateTime routeAvailableAt) {
        this.path = path;
        this.origin  = origin;
        this.destination = destination;

        ZonedDateTime startTime;
        ZonedDateTime endTime;

        Double cost = 0D;
        Double distance = 0D;
        Double duration = 0D;

        for ( Node node : path.nodes() ) {
            if ( node.hasLabel( Segment ) ) {
                cost += (Double) node.getProperty(PRICE);
            }
        }

        this.cost = cost;

        this.routeAvailableAt = routeAvailableAt;
        resultAvailableAt = LocalDateTime.now();
    }

    public FlightResult setStartedAt(LocalDateTime at) {
        startedAt = at;

        return this;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        return path.toString().equals( ((FlightResult) obj).getPath().toString() );
    }

}
