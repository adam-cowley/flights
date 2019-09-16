package org.neo4j.flights.procedures;

import org.neo4j.flights.procedures.evaluators.validpaths.ValidPathCollisionEvaluator;
import org.neo4j.flights.procedures.expanders.validpaths.BidirectionalValidPathExpander;
import org.neo4j.flights.procedures.result.AirportRouteResult;
import org.neo4j.flights.procedures.result.FlightResult;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.TerminationGuard;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.neo4j.flights.procedures.Labels.Airport;
import static org.neo4j.flights.procedures.Properties.PROPERTY_CODE;

public class ValidRoutes {

    @Context
    public GraphDatabaseService db;

    @Context
    public TerminationGuard guard;

    // dijkstra - shortest weighted path - no good, it will only return one result
    // allshortestpaths - no good, a longer path may have a better price
    // single direction expander - takes ages to finish

    @Procedure( name = "flights.validRoutes" )
    public Stream<AirportRouteResult> validRoutesCypher(
            @Name("originCode") String originCode,
            @Name("destinationCode") String destinationCode,
            @Name(value = "maxStopovers", defaultValue = "3") Long maxStopovers,
            @Name(value = "maxPrice", defaultValue = "500.00") Double maxPrice
    ) {
        try ( Transaction tx = db.beginTx() ) {
            // TODO: Convert this back into Traversal API
            Map<String, Object> params = new HashMap<String, Object>() {{
                put("originCode", originCode);
                put("destinationCode", destinationCode);
            }};

            Result res = db.execute("MATCH route = (:Airport {code: $originCode})-[:FLIES_TO*1..3]->(:Airport {code: $destinationCode}) " +
                    "WITH route, reduce(acc = 0, rel IN relationships(route) | acc + rel.distance ) AS cost " +
                    "RETURN route ORDER BY cost DESC LIMIT 10", params);

            List<AirportRouteResult> output = new ArrayList<>();

            while (res.hasNext()) {
                Map<String, Object> row = res.next();

                output.add(new AirportRouteResult((Path) row.get("route")));
            }

            return output.stream();
        }
    }

    // TODO: Failed to invoke procedure `flights.validRoutes`: Caused by: org.neo4j.graphdb.NotFoundException: Node[150] not connected to this relationship[342]
    @Procedure( name = "flights.validRoutesTraverse" )
    public Stream<AirportRouteResult> validRoutes(
            @Name("originCode") String originCode,
            @Name("destinationCode") String destinationCode,
            @Name(value = "maxStopovers", defaultValue = "3") Long maxStopovers,
            @Name(value = "maxPrice", defaultValue = "500.00") Double maxPrice
    ) {
        try ( Transaction tx = db.beginTx() ) {
            // Get airport nodes
            Node origin = db.findNode(Airport, PROPERTY_CODE, originCode);
            Node destination = db.findNode(Airport, PROPERTY_CODE, destinationCode);

            InitialBranchState.State<Double> initialState = new InitialBranchState.State<Double>(0d, 0d);
            BidirectionalValidPathExpander bidirectionalValidPathExpander = new BidirectionalValidPathExpander();

            TraversalDescription td = db.traversalDescription()
                    .breadthFirst()
                    .expand(bidirectionalValidPathExpander, initialState)
                    .uniqueness(Uniqueness.NODE_PATH)
                    .evaluator(Evaluators.toDepth(maxStopovers.intValue()));

            BidirectionalTraversalDescription btd = db.bidirectionalTraversalDescription()
                    .mirroredSides(td)
                    .collisionEvaluator(new ValidPathCollisionEvaluator());

            return btd.traverse(origin, destination)
                    .stream()
                    .map(AirportRouteResult::new);
        }
    }


    @Procedure( name = "flights.between" )
    public Stream<FlightResult> getFlightsBetween(
            @Name("originCode") String originCode,
            @Name("destinationCode") String destinationCode,
            @Name("date") LocalDate date,
            @Name(value = "maxStopovers", defaultValue = "3") Long maxStopovers,
            @Name(value = "maxPrice", defaultValue = "500.00") Double maxPrice
    ) {
        ZonedDateTime startTime = date.atStartOfDay( ZoneOffset.UTC );
        ZonedDateTime endTime = startTime.plusHours(24);

        Stream<AirportRouteResult> validRoutes = validRoutesCypher(originCode, destinationCode, maxStopovers, maxPrice);

        return Stream.empty();
    }

}
