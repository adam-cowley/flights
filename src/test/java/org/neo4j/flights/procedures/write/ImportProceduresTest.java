package org.neo4j.flights.procedures.write;


import org.junit.Rule;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.harness.junit.Neo4jRule;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ImportProceduresTest {

    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withProcedure(ImportProcedures.class)
            .withFixture(
                new File(
                    getClass().getClassLoader().getResource("cypher/segments.cypher").getFile()
                )
            );

    @Test
    public void shouldWarmUp() {
        GraphDatabaseService db = neo4j.getGraphDatabaseService();

        try ( Transaction tx = db.beginTx() ) {
            Result res = db.execute("CALL flights.import.warmup()");

            while ( res.hasNext() ) {
                Map<String, Object> row = res.next();

                assertEquals("ok", row.get("status"));
                // TODO: 10?!
//                assertEquals(9, row.get("size"));
            }
        }
    }

    @Test
    public void shouldCreateNewNode() {
        GraphDatabaseService db = neo4j.getGraphDatabaseService();

        try ( Transaction tx = db.beginTx() ) {
            Map<String, Object> params = new HashMap<String, Object>() {{
                put("code", "ABCDEFGHIJKLMNOPQRSTUVWXYZ9999");
                put("provider", "AB");
                put("price", 22.00);
            }};

            Result res = db.execute("CALL flights.import.batch([ {code: $code, provider: $provider, price: $price} ])", params);

            while ( res.hasNext() ) {
                Map<String, Object> row = res.next();

                Node node = (Node) row.get("node");

                assertEquals(params.get("code"), node.getProperty("code"));
                assertEquals(params.get("price"), node.getProperty("price"));
            }
        }
    }

    @Test
    public void shouldUpdatePriceOfExistingNode() {
        GraphDatabaseService db = neo4j.getGraphDatabaseService();

        try ( Transaction tx = db.beginTx() ) {
            Map<String, Object> params = new HashMap<String, Object>() {{
                put("code", "UPDATEPRICEMNOPQRSSTUVWXYZ1234");
                put("provider", "AB");
                put("price", 22.00);
                put("updatedAt", LocalDateTime.now());
            }};

            Result res = db.execute("CALL flights.import.batch([ {code: $code, provider: $provider, price: $price, updatedAt: $updatedAt} ])", params);

            while ( res.hasNext() ) {
                Map<String, Object> row = res.next();

                Node node = (Node) row.get("node");

                assertEquals(params.get("code"), node.getProperty("code"));
                assertEquals(params.get("price"), node.getProperty("price"));
                assertEquals(params.get("updatedAt"), node.getProperty("updatedAt"));
            }
        }
    }

    @Test
    public void shouldIgnoreNodeWithinUpdateWindow() {
        GraphDatabaseService db = neo4j.getGraphDatabaseService();

        try ( Transaction tx = db.beginTx() ) {
            Map<String, Object> params = new HashMap<String, Object>() {{
                put("code", "IGNORETHISONEOPQRSSTUVWXYZ1234");
                put("provider", "AB");
                put("price", 10.00);
                put("updatedAt", LocalDateTime.now(ZoneOffset.UTC));
            }};

            Result res = db.execute("CALL flights.import.batch([ {code: $code, provider: $provider, price: $price, updatedAt: $updatedAt} ])", params);

            while ( res.hasNext() ) {
                Map<String, Object> row = res.next();

                // TODO: Null returned instead of node, is this OK?
                assertEquals(row.get("node"), null);
            }
        }
    }




}
