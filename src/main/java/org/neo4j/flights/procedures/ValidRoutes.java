package org.neo4j.flights.procedures;

import org.neo4j.flights.procedures.evaluators.validpaths.ValidPathCollisionEvaluator;
import org.neo4j.flights.procedures.expanders.validpaths.BidirectionalValidPathExpander;
import org.neo4j.flights.procedures.services.validroutes.ValidRouteResult;
import org.neo4j.flights.procedures.result.FlightResult;
import org.neo4j.flights.procedures.services.flightsbetween.FlightsBetweenService;
import org.neo4j.flights.procedures.services.validroutes.ValidRoutesService;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.TerminationGuard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public Stream<ValidRouteResult> validRoutesCypher(
            @Name("originCode") String originCode,
            @Name("destinationCode") String destinationCode,
            @Name(value = "maxStopovers", defaultValue = "3") Long maxStopovers,
            @Name(value = "maxPrice", defaultValue = "500.00") Double maxPrice
    ) {
        // TODO: Convert this back into Traversal API
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("originCode", originCode);
            put("destinationCode", destinationCode);
        }};

        Result res = db.execute("MATCH route = (:Airport {code: $originCode})-[:FLIES_TO*1..3]->(:Airport {code: $destinationCode}) " +
                "WITH route, reduce(acc = 0, rel IN relationships(route) | acc + rel.distance ) AS cost " +
                "RETURN route ORDER BY cost ASC LIMIT 10", params);

        List<ValidRouteResult> output = new ArrayList<>();

        while (res.hasNext()) {
            Map<String, Object> row = res.next();

            output.add(new ValidRouteResult((Path) row.get("route")));
        }

        return output.stream();
    }

    // TODO: Failed to invoke procedure `flights.validRoutes`: Caused by: org.neo4j.graphdb.NotFoundException: Node[150] not connected to this relationship[342]
    @Procedure( name = "flights.validRoutesTraverse" )
    public Stream<ValidRouteResult> validRoutes(
            @Name("originCode") String originCode,
            @Name("destinationCode") String destinationCode,
            @Name(value = "maxPrice", defaultValue = "500.00") Double maxPrice,
            @Name(value = "maxStopovers", defaultValue = "3") Long maxStopovers
    ) {
        // Get airport nodes
        Node origin = db.findNode(Airport, PROPERTY_CODE, originCode);
        Node destination = db.findNode(Airport, PROPERTY_CODE, destinationCode);

        ValidRoutesService service = new ValidRoutesService(db, maxStopovers, maxPrice);

        return service.between(origin, destination);
    }


    @Procedure( name = "flights.between" )
    public Stream<FlightResult> getFlightsBetween(
            @Name("originCode") String originCode,
            @Name("destinationCode") String destinationCode,
            @Name("date") LocalDate date,
            @Name(value = "maxStopovers", defaultValue = "3") Long maxStopovers,
            @Name(value = "maxPrice", defaultValue = "500.00") Double maxPrice
    ) {
        Node origin = db.findNode( Airport, PROPERTY_CODE, originCode );
        Node destination = db.findNode( Airport, PROPERTY_CODE, destinationCode );

        Stream<ValidRouteResult> validRoutes = validRoutesCypher(originCode, destinationCode, maxStopovers, maxPrice);

        Set<Node> airports = new HashSet<>();

        validRoutes.forEach( result -> {
            airports.addAll( result.airports );
        });

        airports.remove( origin );

        FlightsBetweenService service = new FlightsBetweenService(db, guard);

        return service.between(origin, destination, date, maxPrice, maxStopovers, airports);
    }

    @Procedure(name = "flights.betweenWithAirports")
    public Stream<FlightResult> getFlightsBetweenWithAirports(
            @Name("origin") Node origin,
            @Name("destination") Node destination,
            @Name("date") LocalDate date,
            @Name("airports") List<Node> airports,
            @Name(value = "maxStopovers", defaultValue = "3") Long maxStopovers,
            @Name(value = "maxPrice", defaultValue = "500.00") Double maxPrice
    ) {
        FlightsBetweenService service = new FlightsBetweenService(db, guard);

        return service.between(origin, destination, date, maxPrice, maxStopovers, new HashSet<Node>(airports));
    }

}
