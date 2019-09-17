package org.neo4j.flights.procedures.services.validroutes;

import org.neo4j.flights.procedures.RelationshipTypes;
import org.neo4j.flights.procedures.expanders.validpaths.ValidPathState;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.BranchState;

import java.util.Collections;

public class ValidPathExpander implements PathExpander<ValidPathState> {

    private final double maxDistance;

    public ValidPathExpander(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    private void setState(Path path, BranchState<ValidPathState> state) {
        if ( path.length() > 0 ) {
            double lastDistance = (double) path.lastRelationship().getProperty("distance");

            ValidPathState newState = state.getState().copy();
            newState.increaseDistance(lastDistance);

            state.setState( newState );
        }
    }

    @Override
    public Iterable<Relationship> expand(Path path, BranchState<ValidPathState> state) {
        // Update Path state
        setState(path, state);

        if ( state.getState().getDistance() > maxDistance ) {
            return Collections.EMPTY_LIST;
        }

        return path.endNode().getRelationships(RelationshipTypes.FLIES_TO, Direction.OUTGOING);
    }

    @Override
    public PathExpander<ValidPathState> reverse() {

        return null;
    }
}
