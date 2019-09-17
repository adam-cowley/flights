package org.neo4j.flights.procedures.expanders;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.neo4j.flights.procedures.services.flightsbetween.DiscoveryState;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.procedure.TerminationGuard;

public class SingleDirectionFlightExpander implements PathExpander<DiscoveryState>
{

    private final TerminationGuard guard;
    private final ZonedDateTime start;
    private final ZonedDateTime end;
    private final List<String> airports;

    private final static DateTimeFormatter relationshipFormat = DateTimeFormatter.ofPattern( "yyyyMMddHH" );

    private final long minStopoverHours = 1;
    private final long maxStopoverHours = 6;


    public SingleDirectionFlightExpander( TerminationGuard guard, ZonedDateTime start, ZonedDateTime end, List<String> airports ) {
        this.guard = guard;
        this.start = start;
        this.end = end;
        this.airports = airports;
    }

    public void checkGuard() {
        guard.check();
    }

    @Override
    public Iterable<Relationship> expand( Path path, BranchState<DiscoveryState> state )
    {
        // Check for a terminated query before expanding
        checkGuard();

        // Expand to Segment
        if ( path.length() % 2 == 0 ) {
            return expandToSegment(path, state);
        }
        else if ( path.length() % 2  == 1 ) {
            return expandToAirport(path, state);
        }

        return Collections.EMPTY_LIST;
    }

    private Iterable<Relationship> expandToSegment( Path path, BranchState<DiscoveryState> state ) {
//        ZonedDateTime expansionDate = start;
//        ZonedDateTime endExpansion = end;
//
//        Node start = path.startNode();
//
//        // (:Airport)-[:DATED_REL]->(:Segment)-[:DATED_REL]->()
//
//        if ( path.length() > 0 ) {
//            expansionDate = DateUtils.relationshipTypeToZonedDateTime( path.lastRelationship().getType() ).plusHours( minStopoverHours );
//            endExpansion = expansionDate.plusHours( maxStopoverHours );
//
//            start = path.lastRelationship().getEndNode();
//        }
//
//        List<Relationship> relationships = new ArrayList<>(  );
//
//        while ( expansionDate.isBefore( endExpansion ) ) {
//
//            RelationshipType type = RelationshipType.withName( expansionDate.format( relationshipFormat ) );
//            System.out.println(type.toString());
//            for ( Relationship rel : start.getRelationships(type, Direction.OUTGOING ) ) {
//                relationships.add(rel);
//            }
//
//            expansionDate = expansionDate.plusHours( 1 );
//        }
//
//        System.out.println(relationships.size());
//
//        return relationships;

        return Collections.EMPTY_LIST;
    }

    private Iterable<Relationship> expandToAirport( Path path, BranchState<DiscoveryState> state ) {
        // Increment State
        DiscoveryState discoveryState = state.getState();
        discoveryState.increasePrice( (Double) path.endNode().getProperty( "price" ) );

        state.setState( discoveryState );

        // There will only be one relationship going out to the airport
        // (:Segment)-[:DATED_REL]->(:Airport)
        return path.endNode().getRelationships( Direction.OUTGOING );
    }



    @Override
    public PathExpander reverse() {
        return null;
    }
}
