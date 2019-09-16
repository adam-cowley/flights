package org.neo4j.flights.procedures.result;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;

public class FlightResult {
    public Path path;
    public Node origin;
    public Node destination;
//        Float time;
//        Float cost;

    public FlightResult() {}

    public FlightResult(Path path, Node origin, Node destination) {
        this.path = path;
        this.origin  = origin;
        this.destination = destination;
    }

}
