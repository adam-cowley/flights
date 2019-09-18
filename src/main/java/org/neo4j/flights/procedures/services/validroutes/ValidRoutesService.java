package org.neo4j.flights.procedures.services.validroutes;

import org.neo4j.flights.procedures.evaluators.validpaths.ValidPathCollisionEvaluator;
import org.neo4j.flights.procedures.expanders.validpaths.BidirectionalValidPathExpander;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.values.storable.CRSCalculator;
import org.neo4j.values.storable.CoordinateReferenceSystem;
import org.neo4j.values.storable.PointValue;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.neo4j.flights.procedures.Properties.COORDINATES;

public class ValidRoutesService {

    private final GraphDatabaseService db;
    private final Double maxDistanceModifier;
    private final Long maxStopovers;
    private final Double maxPrice;

    private final static long MAX_SECONDS = 1;
    private final LocalDateTime stopAt = LocalDateTime.now().plusSeconds(MAX_SECONDS);

    public ValidRoutesService(GraphDatabaseService db, Long maxStopovers, Double maxPrice, Double maxDistanceModifier) {
        this.db = db;
        this.maxStopovers = maxStopovers;
        this.maxPrice = maxPrice;
        this.maxDistanceModifier = maxDistanceModifier;
    }

    public Stream<ValidRouteResult> between(Node origin, Node destination, Long limit) {
        PointValue originCoordinates = (PointValue) origin.getProperty(COORDINATES);
        PointValue destinationCoordinates = (PointValue) destination.getProperty(COORDINATES);

        CRSCalculator calculator = CoordinateReferenceSystem.WGS84.getCalculator();

        double distance = calculator.distance(originCoordinates, destinationCoordinates);
        double maxDistance = distance * maxDistanceModifier;

        InitialBranchState.State<Double> initialState = new InitialBranchState.State<Double>(0d, 0d);
        BidirectionalValidPathExpander bidirectionalValidPathExpander = new BidirectionalValidPathExpander(stopAt, maxStopovers);

        ValidRouteEvaluator validRouteEvaluator = new ValidRouteEvaluator(destination, maxStopovers, maxPrice, maxDistance);

        TraversalDescription td = db.traversalDescription()
                .breadthFirst()
                .expand(bidirectionalValidPathExpander, initialState)
                .uniqueness(Uniqueness.RELATIONSHIP_PATH)
                .evaluator( Evaluators.toDepth( maxStopovers.intValue() ) )
                .evaluator( validRouteEvaluator );

        BidirectionalTraversalDescription btd = db.bidirectionalTraversalDescription()
                .mirroredSides(td)
                .collisionEvaluator(new ValidPathCollisionEvaluator());

        return btd.traverse(origin, destination)
                .stream()
                .map(ValidRouteResult::new)
                .sorted( Comparator.comparing(ValidRouteResult::getDistance) )
                .limit(limit);
    }


}
