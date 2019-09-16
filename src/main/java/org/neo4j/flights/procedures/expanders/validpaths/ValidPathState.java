package org.neo4j.flights.procedures.expanders.validpaths;

public class ValidPathState {

    public double distance;

    public ValidPathState() {
        this.distance = 0d;
    }

    public ValidPathState(double distance) {
        this.distance = distance;
    }

    public void increaseDistance(double by) {
        this.distance = this.distance + by;
    }

    public double getDistance() {
        return distance;
    }

    public ValidPathState copy() {
        double distance = getDistance();

        return new ValidPathState( distance );
    }
}
