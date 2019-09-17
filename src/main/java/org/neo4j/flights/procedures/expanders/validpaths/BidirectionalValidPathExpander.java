package org.neo4j.flights.procedures.expanders.validpaths;

import org.neo4j.flights.procedures.services.validroutes.ValidRouteState;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.BranchState;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.neo4j.flights.procedures.RelationshipTypes.FLIES_TO;

public class BidirectionalValidPathExpander implements PathExpander<Double> {

    private final LocalDateTime stopAt;
    private final Long maxStopovers;

    public BidirectionalValidPathExpander(LocalDateTime stopAt, Long maxStopovers) {
        this.stopAt = stopAt;
        this.maxStopovers = maxStopovers;
    }

    @Override
    public Iterable<Relationship> expand(Path path, BranchState<Double> state) {
        // Don't run forever
        if ( LocalDateTime.now().isAfter( stopAt ) || path.length() > maxStopovers ) {
            return Collections.EMPTY_LIST;
        }

        return path.endNode().getRelationships(FLIES_TO, Direction.OUTGOING);
    }

    @Override
    public PathExpander<Double> reverse() {
        return new PathExpander<Double>() {

            @Override
            public Iterable<Relationship> expand(Path path, BranchState<Double> state) {
                // Don't run forever
                if ( LocalDateTime.now().isAfter( stopAt ) || path.length() > maxStopovers ) {
                    return Collections.EMPTY_LIST;
                }

                return path.startNode().getRelationships(FLIES_TO, Direction.INCOMING);
            }

            @Override
            public PathExpander<Double> reverse() {
                return null;
            }
        };
    }
}
