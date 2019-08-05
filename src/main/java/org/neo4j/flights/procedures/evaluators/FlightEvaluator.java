package org.neo4j.flights.procedures.evaluators;

import org.neo4j.flights.procedures.DiscoveryState;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.PathEvaluator;

public class FlightEvaluator implements PathEvaluator<DiscoveryState>
{

    @Override
    public Evaluation evaluate( Path path )
    {
        return null;
    }

    public Evaluation evaluate( Path path, BranchState<DiscoveryState> state )
    {
        return null;
    }
}
