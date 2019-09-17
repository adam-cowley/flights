package org.neo4j.flights.procedures.services.validroutes;

public class ValidRouteState {

    private Double price = 0D;
    private Double distance = 0D;

    public ValidRouteState increasePrice(Double price) {
        this.price += price;

        return this;
    }

    public ValidRouteState increaseDistance(Double distance) {
        this.distance += distance;

        return this;
    }

    public Double getPrice() {
        return price;
    }

    public Double getDistance() {
        return distance;
    }
}
