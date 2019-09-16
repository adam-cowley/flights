package org.neo4j.flights.procedures;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.neo4j.cypher.internal.compiler.v3_1.commands.expressions.DistanceCalculator;
import org.neo4j.cypher.internal.v3_4.functions.Distance;
import org.neo4j.flights.procedures.evaluators.validpaths.ValidPathCollisionEvaluator;
import org.neo4j.flights.procedures.evaluators.validpaths.ValidPathEvaluator;
import org.neo4j.flights.procedures.expanders.SingleDirectionFlightExpander;
import org.neo4j.flights.procedures.expanders.validpaths.BidirectionalValidPathExpander;
import org.neo4j.flights.procedures.expanders.validpaths.ValidPathExpander;
import org.neo4j.flights.procedures.expanders.validpaths.ValidPathState;
import org.neo4j.flights.procedures.result.FlightResult;
import org.neo4j.flights.procedures.result.AirportRouteResult;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.spatial.Point;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.TerminationGuard;
import org.neo4j.values.storable.CRSCalculator;
import org.neo4j.values.storable.CoordinateReferenceSystem;
import org.neo4j.values.storable.PointValue;

public class GetFlights
{

//    private static final Label Airport = Label.label( "Airport" );
//    private static final Label Segment = Label.label( "Segment" );
//    private static final RelationshipType FLIES_TO = RelationshipType.withName("FLIES_TO");
//
//    private static final String PROPERTY_CODE = "code";
//    private static final String PROPERTY_COST = "price";
//    private static final String PROPERTY_COORDINATES = "coordinates";
//    private static final String PROPERTY_DISTANCE = "distance";
//
//    private static final PathExpander shortestPathExpander = PathExpanders.forTypeAndDirection(FLIES_TO, Direction.OUTGOING);
//
//    @Context
//    public GraphDatabaseService db;
//
//    @Context
//    public TerminationGuard guard;
//
//    @Procedure(name="flights.between")
//    public Stream<FlightResult> flightsBetween(
//            @Name("fromCode") String fromCode,
//            @Name("toCode") String toCode,
//            @Name("date") LocalDate date,
//            @Name(value = "maxStopovers", defaultValue = "3") Long maxStopovers,
//            @Name(value = "maxPrice", defaultValue = "500.00") Double maxPrice
//    ) {
//        ZonedDateTime startTime = date.atStartOfDay( ZoneOffset.UTC );
//        ZonedDateTime endTime = startTime.plusHours(24);
//        List<String> airports = new ArrayList<>(  );
//
//
//
//
//
//
////        ShortestPathExpander shortestPathExpander = new ShortestPathExpander(maxStopovers);
//
////        Stream<ShortestPathResult>
//
//        // Nodes
//        Node from = db.findNode(Airport, PROPERTY_CODE, fromCode);
//        Node to = db.findNode(Airport, PROPERTY_CODE, toCode);
//
//        if ( from == null || to == null ) {
//            return Stream.empty();
//        }
//
//        // Initial Branch State
//        InitialBranchState.State<DiscoveryState> ibs;
//        ibs = new InitialBranchState.State<>( new DiscoveryState(), new DiscoveryState(  ));
//
//        // Expander
//        SingleDirectionFlightExpander expander = new SingleDirectionFlightExpander(guard, startTime, endTime, airports);
//
//        // Traversal Description
//        TraversalDescription td = db.traversalDescription()
//            .uniqueness( Uniqueness.NODE_GLOBAL )
//            .depthFirst()
////            .breadthFirst()
//            .expand( expander, ibs )
//                .evaluator( Evaluators.toDepth( maxStopovers.intValue() * 2 ) )
//                .evaluator( Evaluators.endNodeIs( Evaluation.INCLUDE_AND_PRUNE, Evaluation.EXCLUDE_AND_CONTINUE, to ) );
//
//        return td.traverse( from )
//                .iterator()
//                .stream()
//                .map(path -> new FlightResult( path, from, to ));
//    }

}
