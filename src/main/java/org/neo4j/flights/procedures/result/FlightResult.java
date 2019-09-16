package org.neo4j.flights.procedures.result;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;

import static org.neo4j.flights.procedures.Labels.Airport;
import static org.neo4j.flights.procedures.Labels.Segment;
import static org.neo4j.flights.procedures.Properties.PROPERTY_COST;

public class FlightResult {
    public Path path;
    public Node origin;
    public Node destination;

//    private TemporalAmount duration;
    public Double distance;
    public Double cost;

    public FlightResult() {}

    public FlightResult(Path path, Node origin, Node destination) {
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
                cost += (Double) node.getProperty( PROPERTY_COST );


            }
        }

        this.cost = cost;

    }

}
