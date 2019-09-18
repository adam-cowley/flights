package org.neo4j.flights.procedures.write;

public class WarmUpResult {
    public String status;
    public Number size;

    public WarmUpResult(String status, int size) {
        this.status = status;
        this.size = size;
    }
}
