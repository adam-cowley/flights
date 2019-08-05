package org.neo4j.flights.procedures;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.neo4j.flights.procedures.expanders.SingleDirectionFlightExpander;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.InitialBranchState;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Uniqueness;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.TerminationGuard;

public class GetFlights
{

    private static Label Airport = Label.label( "Airport" );
    private static Label Segment = Label.label( "Segment" );

    private static String PROPERTY_CODE = "code";

    @Context
    public GraphDatabaseService db;

    @Context
    public TerminationGuard guard;

    @Procedure(name="flights.between")
    public Stream<FlightResult> getFlights(
            @Name("fromCode") String fromCode,
            @Name("toCode") String toCode,
            @Name("date") LocalDate date,
            @Name(value = "maxStopovers", defaultValue = "3") Long maxStopovers,
            @Name(value = "maxPrice", defaultValue = "500.00") Float maxPrice
    ) {
        ZonedDateTime startTime = date.atStartOfDay( ZoneOffset.UTC );
        ZonedDateTime endTime = startTime.plusHours(24);
        List<String> airports = new ArrayList<>(  );

        // Nodes
        Node from = db.findNode(Airport, PROPERTY_CODE, fromCode);
        Node to = db.findNode(Airport, PROPERTY_CODE, toCode);

        if ( from == null || to == null ) {
            return Stream.empty();
        }

        // Initial Branch State
        InitialBranchState.State<DiscoveryState> ibs;
        ibs = new InitialBranchState.State<>( new DiscoveryState(), new DiscoveryState(  ));

        // Expander
        SingleDirectionFlightExpander expander = new SingleDirectionFlightExpander(guard, startTime, endTime, airports);

        // Traversal Description
        TraversalDescription td = db.traversalDescription()
            .uniqueness( Uniqueness.NODE_PATH )
//            .depthFirst()
            .breadthFirst()
            .expand( expander, ibs )
                .evaluator( Evaluators.toDepth( maxStopovers.intValue() * 2 ) )
                .evaluator( Evaluators.endNodeIs( Evaluation.INCLUDE_AND_PRUNE, Evaluation.EXCLUDE_AND_CONTINUE, to ) );

        return td.traverse( from )
                .iterator()
                .stream()
                .map(path -> new FlightResult( path, from, to ));
    }


    public static class FlightResult {
        public Path path;
        public Node origin;
        public Node destination;
//        Float time;
//        Float cost;

        public FlightResult() {}

        public FlightResult(Path path, Node origin, Node destination) {
            this.path = path;
            this.origin  = origin;
            this.destination = destination;
        }

    }
}
