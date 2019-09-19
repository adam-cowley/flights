package org.neo4j.flights.procedures.write;

import org.neo4j.graphdb.Node;

public class ImportResult {


    public final static int STATUS_IGNORED_VIA_CACHE = 1;
    public final static int STATUS_IGNORED_VIA_NODE = 2;
    public final static int STATUS_UPDATED_VIA_CACHE = 3;
    public final static int STATUS_UPDATED_VIA_NODE = 4;
    public final static int STATUS_CREATED = 5;

    public String code;
    public Number status;

    public ImportResult(String code, int status) {
        this.code = code;
        this.status = status;
    }

}
