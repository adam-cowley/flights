package org.neo4j.flights.procedures.write;

import org.neo4j.graphdb.Node;

public class ImportResult {

    public Node node;

    public ImportResult(Node node) {
        this.node = node;
    }

}
