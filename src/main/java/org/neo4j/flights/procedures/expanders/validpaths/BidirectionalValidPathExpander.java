package org.neo4j.flights.procedures.expanders.validpaths;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.BranchState;

import static org.neo4j.flights.procedures.RelationshipTypes.FLIES_TO;

public class BidirectionalValidPathExpander implements PathExpander<Double> {
    @Override
    public Iterable<Relationship> expand(Path path, BranchState<Double> state) {
        System.out.println( "Out: "+ state.getState() + "//"+ path.endNode().getProperty("code") + " == "+ path.endNode().getDegree(FLIES_TO, Direction.OUTGOING) );

        return path.endNode().getRelationships(FLIES_TO, Direction.OUTGOING);

    }

    @Override
    public PathExpander<Double> reverse() {
        return new PathExpander<Double>() {

            @Override
            public Iterable<Relationship> expand(Path path, BranchState<Double> state) {
                System.out.println( "In: "+ state.getState() + "//"+ path.startNode().getProperty("code") + " == "+ path.startNode().getDegree(FLIES_TO, Direction.INCOMING) );

                return path.startNode().getRelationships(FLIES_TO, Direction.INCOMING);
            }

            @Override
            public PathExpander<Double> reverse() {
                return null;
            }
        };
    }
}
