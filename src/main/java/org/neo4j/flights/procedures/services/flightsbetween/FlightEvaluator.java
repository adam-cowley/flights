package org.neo4j.flights.procedures.services.flightsbetween;


import com.github.benmanes.caffeine.cache.Cache;
import org.neo4j.flights.procedures.FlightCache;
import org.neo4j.flights.procedures.services.flightsbetween.DiscoveryState;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.PathEvaluator;

import java.time.ZonedDateTime;

import static org.neo4j.flights.procedures.Labels.Airport;
import static org.neo4j.flights.procedures.Properties.DEPARTS;

public class FlightEvaluator implements PathEvaluator<DiscoveryState> {

    private final Node destination;
    private final Long maxStopovers;
    private final Double maxPrice;
    private final FlightCache cache;
    private final Boolean useCache;

    public FlightEvaluator( Node destination, Double maxPrice, Long maxStopovers, FlightCache cache, Boolean useCache ) {
        this.destination = destination;
        this.maxPrice = maxPrice;
        this.maxStopovers = maxStopovers;
        this.cache = cache;
        this.useCache = useCache;
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

        // Is the cost higher than the max price, or max number of stopovers
        if ( currentState.getPrice() > maxPrice || currentState.getStopovers() > maxStopovers ) {
            return Evaluation.EXCLUDE_AND_PRUNE;
        }

        // Is this the cheapest route for this departure time, between these two airports?
        if ( useCache && path.endNode().hasLabel( Airport ) ) {
            // TODO: Move rel type
            FlightCache.Flight key = new FlightCache.Flight(path.startNode(), path.endNode(), path.relationships().iterator().next().getType());

            if ( !cache.isCheapest(key, currentState.getPrice()) ) {
                return Evaluation.EXCLUDE_AND_PRUNE;
            }
        }

        // Is this the destination airport?
        if ( path.endNode().equals( destination ) ) {
            return Evaluation.INCLUDE_AND_PRUNE;
        }

        // Carry on
        return Evaluation.EXCLUDE_AND_CONTINUE;
    }

}
