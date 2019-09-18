package org.neo4j.flights.procedures.services.flightsbetween;


import com.github.benmanes.caffeine.cache.Cache;
import org.neo4j.flights.procedures.FlightCache;
import org.neo4j.flights.procedures.services.flightsbetween.DiscoveryState;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.PathEvaluator;

import static org.neo4j.flights.procedures.Labels.Airport;

public class FlightEvaluator implements PathEvaluator<DiscoveryState> {

    private final Node destination;
    private final Long maxStopovers;
    private final Double maxPrice;
    private final Cache<FlightCache.Flight, Double> cache;

    public FlightEvaluator( Node destination, Double maxPrice, Long maxStopovers, Cache<FlightCache.Flight, Double> cache ) {
        this.destination = destination;
        this.maxPrice = maxPrice;
        this.maxStopovers = maxStopovers;
        this.cache = cache;
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

        // Check that this is the cheapest route between the start airport and here
        // TODO: This won't work.  Do this on a per-traversal basis
        if ( path.endNode().hasLabel( Airport ) ) {
            FlightCache.Flight key = new FlightCache.Flight(path.startNode(), path.endNode());

            Double cachedCost = cache.getIfPresent(key);

            if ( cachedCost != null ) {
                // If the price is higher than another that we've already found then exclude it
                if ( currentState.getPrice() > cachedCost ) {
                    return Evaluation.EXCLUDE_AND_PRUNE;
                }

                // It must be lower, add it to the cache
                cache.put(key, currentState.getPrice());
            }
        }

        if ( currentState.getPrice() > maxPrice || currentState.getStopovers() > maxStopovers ) {
            return Evaluation.EXCLUDE_AND_PRUNE;
        }
        else if ( path.endNode().equals( destination ) ) {
            return Evaluation.INCLUDE_AND_PRUNE;
        }

        return Evaluation.EXCLUDE_AND_CONTINUE;
    }

}
