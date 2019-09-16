package org.neo4j.flights.procedures.evaluators.validpaths;

import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.PathEvaluator;

public class ValidPathCollisionEvaluator implements Evaluator {

    @Override
    public Evaluation evaluate(Path path) {
        System.out.println("Evaluate: "+ path.toString());
        return Evaluation.INCLUDE_AND_PRUNE;
    }

}
