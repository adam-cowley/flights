package org.neo4j.flights.procedures.services.validroutes;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.PathEvaluator;

public class ValidRouteEvaluator implements PathEvaluator<Double> {

    private final Node destination;
    private final Long maxStopovers;
    private final Double maxPrice;
    private final Double maxDistance;

    public ValidRouteEvaluator(Node destination, Long maxStopovers, Double maxPrice, Double maxDistance) {
        this.destination = destination;
        this.maxStopovers = maxStopovers;
        this.maxPrice = maxPrice;
        this.maxDistance = maxDistance;
    }

    @Override
    public Evaluation evaluate(Path path, BranchState<Double> state) {
        Double distance = state.getState();

        if ( distance > maxDistance ) {
            return Evaluation.EXCLUDE_AND_PRUNE;
        }

        if ( path.length() > maxStopovers ) {
            return Evaluation.EXCLUDE_AND_PRUNE;
        }

        if ( path.endNode().equals( destination ) ) {
            return Evaluation.INCLUDE_AND_PRUNE;
        }

        return Evaluation.EXCLUDE_AND_CONTINUE;
    }

    @Override
    public Evaluation evaluate(Path path) {
        return null;
    }
}
