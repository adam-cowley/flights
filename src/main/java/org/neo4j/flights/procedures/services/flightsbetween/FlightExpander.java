package org.neo4j.flights.procedures.services.flightsbetween;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.neo4j.flights.procedures.services.flightsbetween.DiscoveryState;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.procedure.TerminationGuard;

import static org.neo4j.flights.procedures.Labels.Airport;
import static org.neo4j.flights.procedures.Labels.AirportDay;
import static org.neo4j.flights.procedures.Labels.AirportDestination;
import static org.neo4j.flights.procedures.Labels.Segment;
import static org.neo4j.flights.procedures.Properties.PROPERTY_CODE;

public class FlightExpander implements PathExpander<DiscoveryState>
{

    private final TerminationGuard guard;

    private final ZonedDateTime start;

    private final Set<Node> airports;

    private final List<String> airportCodes = new ArrayList<>();

    private final Long maxStopovers;
    private final Double maxPrice;

    private final static DateTimeFormatter airportDayRelationshipFormat = DateTimeFormatter.ofPattern( "yyyyMMdd" );
    private final static DateTimeFormatter segmentRelationshipFormat = DateTimeFormatter.ofPattern( "yyyyMMddHH" );

    private final long minStopoverHours = 1;
    private final long maxStopoverHours = 6;

    private final LocalDateTime stopAt;

    public FlightExpander( TerminationGuard guard, ZonedDateTime start, Set<Node> airports, Double maxPrice, Long maxStopovers, LocalDateTime stopAt) {
        this.guard = guard;
        this.start = start;
        this.airports = airports;

        this.maxStopovers = maxStopovers;
        this.maxPrice = maxPrice;
        this.stopAt = stopAt;

        for ( Node airport: airports ) {
            airportCodes.add( (String) airport.getProperty(PROPERTY_CODE) );
        }
    }

    /**
     * Full path:
     *
     *   (:Airport)-[:YYYYMMDD]->(:AirportDay)-[:XXX]->(:AirportDestination)-[:YYYYMMDDHH]->(:Segment)-[:YYYYMMDDHH]->(:Airport)
     *
     * Then an infinite loop of:
     *             -[:YYYYMMDD]->(:AirportDay)-[:XXX]->(:AirportDestination)-[:YYYYMMDDHH]->(:Segment)-[:YYYYMMDDHH]->(:Airport)
     *
     * @param path
     * @param state
     * @return
     */
    @Override
    public Iterable<Relationship> expand( Path path, BranchState<DiscoveryState> state )
    {
        // Guarantee sub-second performance
        if ( stopAt.isBefore( LocalDateTime.now() ) ) {
            return Collections.EMPTY_LIST;
        }

        if ( path.length() == 0 ) {
            state.setState( state.getState().copy().setMinimumDeparture( start ) );
        }

        // TODO: Convert these if's to a calculation based on path length
        if ( path.endNode().hasLabel( Airport ) )  {
            return expandToAirportDay(path, state);
        }
        else if ( path.endNode().hasLabel( AirportDay ) ) {
            return expandToAirportDestination(path, state);
        }
        else if ( path.endNode().hasLabel( AirportDestination ) ) {
            return expandToSegment(path, state);
        }
        else if ( path.endNode().hasLabel( Segment) ) {
            return expandFromSegmentToAirport(path, state);
        }

        return Collections.EMPTY_LIST;
    }

    @Override
    public PathExpander<DiscoveryState> reverse()
    {
        return null;
    }

    /**
     * Expand from an Airport node to the Airport Day
     *
     * @param path
     * @param state
     * @return
     */
    public Iterable<Relationship> expandToAirportDay( Path path, BranchState<DiscoveryState> state ) {
        // (:Airport)-[:YYYYMMDD]->(:AirportDay)
        Node airport = path.endNode();

        ZonedDateTime expansionDate = state.getState().getMinimumDeparture();

        if ( expansionDate == null ) {
            expansionDate = start;
        }

        List<Relationship> output = new ArrayList<>();

        RelationshipType todayType = RelationshipType.withName( expansionDate.format( airportDayRelationshipFormat ) );
        for ( Relationship rel :  airport.getRelationships(todayType, Direction.OUTGOING ) ) {
            output.add( rel );
        }

        // Next airport day after layout?
        ZonedDateTime maxTravelDate = expansionDate.plusHours( maxStopoverHours );

        if ( maxTravelDate.getDayOfMonth() > expansionDate.getDayOfMonth() )  {
            RelationshipType tomorrowType = RelationshipType.withName( expansionDate.format( airportDayRelationshipFormat ) );

            for ( Relationship rel :  airport.getRelationships(tomorrowType, Direction.OUTGOING ) ) {
                output.add( rel );
            }
        }

        return output;
    }

    /**
     * Expand from the airport day to the airport destination node
     * @param path
     * @param state
     * @return
     */
    public Iterable<Relationship> expandToAirportDestination( Path path, BranchState<DiscoveryState> state ) {
        List<Relationship> output = new ArrayList<>(  );
        Node airportDay = path.endNode();

        for ( String code : airportCodes ) {
            RelationshipType type = RelationshipType.withName( code );

            for ( Relationship rel : airportDay.getRelationships( type, Direction.OUTGOING ) ) {
                output.add( rel );
            }
        }

        return output;
    }

    /**
     * On the first expansion, any flight from that day needs to be considered.  For subsequent segments,
     * the minimum and maximum stopover hours must be adhered to
     *
     * @param path
     * @param state
     * @return
     */
    private Iterable<Relationship> expandToSegment( Path path, BranchState<DiscoveryState> state ) {
        ZonedDateTime expansionStart = state.getState().getMinimumDeparture();

        Long hoursToAdd = 24L;

        // If it's the second segment, then use the max stopover hours otherwise it should be 24 hours
        if ( path.length() > 4 ) {
            hoursToAdd = maxStopoverHours;
        }

        ZonedDateTime expansionEnd = expansionStart.plusHours( hoursToAdd );

        Node endNode = path.endNode();

        // (:Airport)-[:DATED_REL]->(:Segment)
        List<Relationship> relationships = new ArrayList<>(  );

        while ( expansionStart.isBefore( expansionEnd ) ) {
            RelationshipType type = RelationshipType.withName( expansionStart.format( segmentRelationshipFormat ) );

            for ( Relationship rel : endNode.getRelationships(type, Direction.OUTGOING ) ) {
                relationships.add(rel);
            }

            expansionStart = expansionStart.plusHours( 1 );
        }

        return relationships;
    }

    /**
     * This one is easy, just follow the outgoing relationship to the Airport
     * @param path
     * @param state
     * @return
     */
    private Iterable<Relationship> expandFromSegmentToAirport( Path path, BranchState<DiscoveryState> state ) {
        // Update state
        ZonedDateTime nextMinimumDeparture = start;

        if ( path.length() > 0 ) {
            nextMinimumDeparture = ZonedDateTime.of( LocalDateTime.parse( path.lastRelationship().getType().toString(), segmentRelationshipFormat ), ZoneOffset.UTC ).plusHours( minStopoverHours );
        }

        DiscoveryState newState = state.getState().copy()
                .setMinimumDeparture( nextMinimumDeparture )
                .applySegment( path.endNode() );

        state.setState( newState );

        return path.endNode().getRelationships( Direction.OUTGOING );
    }

}
