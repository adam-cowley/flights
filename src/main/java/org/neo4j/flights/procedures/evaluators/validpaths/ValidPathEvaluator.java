package org.neo4j.flights.procedures.evaluators.validpaths;

import org.neo4j.flights.procedures.DiscoveryState;
import org.neo4j.flights.procedures.expanders.validpaths.ValidPathState;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.PathEvaluator;

public class ValidPathEvaluator implements PathEvaluator<ValidPathState> {

    private final double maxDistance;
    private final Node destination;

    public ValidPathEvaluator(Node destination, double maxDistance) {
        this.destination = destination;
        this.maxDistance = maxDistance;
    }

    @Override
    public Evaluation evaluate(Path path, BranchState<ValidPathState> state) {
        if ( path.length() == 0 ) {
            return Evaluation.EXCLUDE_AND_CONTINUE;
        }

        if ( state.getState().getDistance() > maxDistance ) {
            return Evaluation.EXCLUDE_AND_PRUNE;
        }

//        if ( path.endNode().equals( destination ) ) {
//            return Evaluation.INCLUDE_AND_PRUNE;
//        }

        return Evaluation.EXCLUDE_AND_CONTINUE;
    }

    @Override
    public Evaluation evaluate(Path path) {
        return null;
    }
}
