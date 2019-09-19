package org.neo4j.flights.procedures.write;

public class WarmUpResult {
    public String status;
    public Number airports;
    public Number segments;

    public WarmUpResult(String status, int airports, int segments) {
        this.status = status;
        this.airports = airports;
        this.segments = segments;
    }
}
