package org.neo4j.flights.procedures.result;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;

import java.util.ArrayList;
import java.util.List;

public class AirportRouteResult {

    public Path route;
    public List<Node> airports;
    public double cost;
    public double distance;

    public AirportRouteResult(Path route, List<Node> airports, double distance, double cost) {
        this.route = route;
        this.airports = airports;
        this.distance = distance;
        this.cost = cost;
    }

    public AirportRouteResult(Path route) {
        airports = new ArrayList<>();

        for ( Node airport : route.nodes() ) {
            airports.add(airport);
        }

        double distance = 0d;
        double cost = 0d;

        for ( Relationship rel : route.relationships() ) {
            distance += (double) rel.getProperty("distance");
            cost += ((Number) rel.getProperty("price")).doubleValue();
        }

        this.route = route;
        this.distance = distance;
        this.cost = cost;
    }

}