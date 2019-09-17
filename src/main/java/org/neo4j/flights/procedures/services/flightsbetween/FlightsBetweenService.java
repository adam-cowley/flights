package org.neo4j.flights.procedures.services.flightsbetween;

import org.neo4j.flights.procedures.result.FlightResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.InitialBranchState;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Uniqueness;
import org.neo4j.procedure.TerminationGuard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Stream;

public class FlightsBetweenService {

    private final GraphDatabaseService db;
    private final TerminationGuard guard;

    private final static long MAX_SECONDS = 3;
    private final LocalDateTime stopAt = LocalDateTime.now().plusSeconds(MAX_SECONDS);

    public FlightsBetweenService(GraphDatabaseService db, TerminationGuard guard) {
        this.db = db;
        this.guard = guard;
    }

    public Stream<FlightResult> between(Node origin, Node destination, LocalDate date, Double maxPrice, Long maxStopovers, Set<Node> airports) {
        ZonedDateTime startTime = date.atStartOfDay( ZoneOffset.UTC );

        // Initial State
        InitialBranchState.State<DiscoveryState> initialState = new InitialBranchState.State<>( new DiscoveryState(), new DiscoveryState() );

        // Expander
        FlightExpander expander = new FlightExpander( guard, startTime, airports, maxPrice, maxStopovers, stopAt );

        // Evaluator
        FlightEvaluator evaluator = new FlightEvaluator(destination, maxPrice, maxStopovers);

        TraversalDescription td = db.traversalDescription()
                .depthFirst()
                .uniqueness( Uniqueness.NODE_PATH )
                .expand( expander, initialState )
                .evaluator( evaluator );

        return td.traverse( origin )
                .stream()
                .map( path -> new FlightResult( path, origin, destination ) );
    }


}
