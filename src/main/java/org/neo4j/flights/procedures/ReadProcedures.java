package org.neo4j.flights.procedures;

import org.neo4j.flights.procedures.services.validroutes.ValidRouteResult;
import org.neo4j.flights.procedures.result.FlightResult;
import org.neo4j.flights.procedures.services.flightsbetween.FlightsBetweenService;
import org.neo4j.flights.procedures.services.validroutes.ValidRoutesService;
import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.TerminationGuard;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.neo4j.flights.procedures.Labels.Airport;
import static org.neo4j.flights.procedures.Properties.CODE;

public class ReadProcedures {

    @Context
    public GraphDatabaseService db;

    @Context
    public TerminationGuard guard;

    @Context
    public Log log;

    // dijkstra - shortest weighted path - no good, it will only return one result
    // allshortestpaths - no good, a longer path may have a better price
    // single direction expander - takes ages to finish

    private final static double maxDistanceModifier = 1.2D;


    @Procedure( name = "flights.validRoutes" )
    public Stream<ValidRouteResult> validRoutes(
            @Name("originCode") String originCode,
            @Name("destinationCode") String destinationCode,
            @Name(value = "maxStopovers", defaultValue = "3") Long maxStopovers,
            @Name(value = "maxPrice", defaultValue = "500.00") Double maxPrice,
            @Name(value = "limit", defaultValue = "10") Long limit
    ) {
        // Get airport nodes
        Node origin = db.findNode(Airport, CODE, originCode);
        Node destination = db.findNode(Airport, CODE, destinationCode);

        ValidRoutesService service = new ValidRoutesService(db, maxStopovers, maxPrice, maxDistanceModifier);

        return service.between(origin, destination, limit);
    }


    @Procedure( name = "flights.between" )
    public Stream<FlightResult> getFlightsBetween(
            @Name("originCode") String originCode,
            @Name("destinationCode") String destinationCode,
            @Name("date") LocalDate date,
            @Name(value = "maxStopovers", defaultValue = "3") Long maxStopovers,
            @Name(value = "maxPrice", defaultValue = "500.00") Double maxPrice
    ) {
        Node origin = db.findNode( Airport, CODE, originCode );
        Node destination = db.findNode( Airport, CODE, destinationCode );

        Stream<ValidRouteResult> validRoutes = validRoutes(originCode, destinationCode, maxStopovers, maxPrice, 10L);

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
