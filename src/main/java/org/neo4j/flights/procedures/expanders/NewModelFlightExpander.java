package org.neo4j.flights.procedures.expanders;

import org.neo4j.flights.procedures.DiscoveryState;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.procedure.TerminationGuard;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class NewModelFlightExpander extends SingleDirectionFlightExpander implements PathExpander<DiscoveryState> {

    public NewModelFlightExpander(TerminationGuard guard, ZonedDateTime start, ZonedDateTime end, List<String> airports ) {
        super(guard, start, end, airports);
    }

    @Override
    public Iterable<Relationship> expand(Path path, BranchState<DiscoveryState> state )
    {

//        // MATCH p = (o:Airport)-->(oad:AirportDay)-->(oadd:AirportDestination)-->(s:Segment)-->(d:Airport)
//        //              (:AirportDay)-->(:AirportDestination)-->(:Segment)--(:Airport)
//
//        // Check for a terminated query before expanding
//        checkGuard();
//
//        if ( path.length() == 0 ) {
//            return expandToAirportDay(path, state);
//        }
//
//        // Expand to Segment
//        if ( path.length() % 2 == 0 ) {
//            return expandToSegment(path, state);
//        }
//        else if ( path.length() % 2  == 1 ) {
//            return expandToAirport(path, state);
//        }

        return Collections.EMPTY_LIST;
    }

    private Iterable<Relationship> expandToAirportDay( Path path, BranchState<DiscoveryState> state ) {
        return null;
    }

}
