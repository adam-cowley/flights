package org.neo4j.flights.procedures.services.flightsbetween;


import org.neo4j.flights.procedures.services.flightsbetween.DiscoveryState;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.PathEvaluator;

public class FlightEvaluator implements PathEvaluator<DiscoveryState> {

    private final Node destination;
    private final Long maxStopovers;
    private final Double maxPrice;

    public FlightEvaluator( Node destination, Double maxPrice, Long maxStopovers ) {
        this.destination = destination;
        this.maxPrice = maxPrice;
        this.maxStopovers = maxStopovers;
    }

    @Override
    public Evaluation evaluate(Path path) {
        return Evaluation.EXCLUDE_AND_PRUNE;
    }

    @Override
    public Evaluation evaluate(Path path, BranchState<DiscoveryState> state) {
        if ( path.length() == 0 ) {
            return Evaluation.EXCLUDE_AND_CONTINUE;
        }

        DiscoveryState currentState = state.getState();

        // Apply the last segment node?
//        if ( path.endNode().equals( destination ) ) {
//            currentState = currentState.applySegment( path.lastRelationship().getStartNode() );
//        }

//        System.out.println("");
//        System.out.println("--");
//        System.out.println(path.toString());
//        System.out.println("stopovers: " + currentState.getStopovers() + " of: "+ maxStopovers);
//        System.out.println("price:     " + currentState.getPrice() + " of: "+ maxPrice);
//        System.out.println("n=end:     " + path.endNode().getProperty( "code" ));

        if ( currentState.getPrice() > maxPrice || currentState.getStopovers() > maxStopovers ) {
            return Evaluation.EXCLUDE_AND_PRUNE;
        }
        else if ( path.endNode().equals( destination ) ) {
            return Evaluation.INCLUDE_AND_PRUNE;
        }

        return Evaluation.EXCLUDE_AND_CONTINUE;
    }

}
