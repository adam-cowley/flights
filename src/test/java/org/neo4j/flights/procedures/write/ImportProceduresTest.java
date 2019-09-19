package org.neo4j.flights.procedures.write;


import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
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
import static org.neo4j.flights.procedures.write.ImportResult.*;

public class ImportProceduresTest {

    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withProcedure(ImportProcedures.class)
            .withFixture(
                new File(
                    getClass().getClassLoader().getResource("cypher/segments.cypher").getFile()
                )
            );

    @AfterClass
    public static void cleanUp() {
        File segmentStore = new File(System.getProperty("user.home") + "/segments.dat");

        segmentStore.delete();
    }

    @Test
    public void shouldWarmUp() {
        GraphDatabaseService db = neo4j.getGraphDatabaseService();

        try ( Transaction tx = db.beginTx() ) {
            Result res = db.execute("CALL flights.import.warmup()");

            while ( res.hasNext() ) {
                Map<String, Object> row = res.next();

                assertEquals("ok", row.get("status"));
            }
        }
    }

    @Test
    public void shouldCreateNewNode() {
        GraphDatabaseService db = neo4j.getGraphDatabaseService();

        try ( Transaction tx = db.beginTx() ) {
            /*
            [
              {
                origCoordinates: { x: 2.82493, y: 41.98311 },
                arrivalTimestamp: 1571985900000,
                code: 'GROEIN201910250430201910250645',
                arrivalDay: '2019102506',
                origin: 'GRO',
                destination: 'EIN',
                numStopovers: 0,
                departureDay: '2019102504',
                updateTimestamp: 1566977734364,
                searchProviders: [ 'TF' ],
                destCoordinates: { x: 5.469999, y: 51.439998 },
                provider: 'TF',
                price: 62.99,
                departureTimestamp: 1571978400000,
                departureDateTime: '201910250440',
                arrivalDateTime: '201910250645'
              }
            ]
             */
            Map<String, Object> params = new HashMap<String, Object>() {{
                put("code", "CREATEITIJKLMNOPQRSTUVWXYZ9999");
                put("provider", "AB");
                put("price", 22.00);
                put("origin", "A1");
                put("destination", "A2");
                put("numStopovers", 0L);
                put("arrivalTimestamp", 1571985900000L);
                put("departureTimestamp", 1571985900000L);

            }};

            Result res = db.execute("CALL flights.import.viaCache([ {" +
                    "code: $code, " +
                    "provider: $provider, " +
                    "price: $price," +
                    "origin: $origin," +
                    "destination: $destination," +
                    "numStopovers: $numStopovers," +
                    "arrives: datetime({epochMillis: $arrivalTimestamp})," +
                    "departs: datetime({epochMillis: $departureTimestamp})" +
                    "} ])", params);

            while ( res.hasNext() ) {
                Map<String, Object> row = res.next();


                assertEquals(row.get("code"), params.get("code"));
                assertEquals(STATUS_CREATED, row.get("status"));
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

            Result res = db.execute("CALL flights.import.viaCache([ {code: $code, provider: $provider, price: $price, updatedAt: $updatedAt} ])", params);

            while ( res.hasNext() ) {
                Map<String, Object> row = res.next();

                assertEquals(params.get("code"), row.get("code"));
                assertEquals(STATUS_UPDATED_VIA_CACHE, row.get("status"));
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

            Result res = db.execute("CALL flights.import.viaCache([ {code: $code, provider: $provider, price: $price, updatedAt: $updatedAt} ])", params);

            while ( res.hasNext() ) {
                Map<String, Object> row = res.next();

                assertEquals(params.get("code"), row.get("code"));
                assertEquals(STATUS_IGNORED_VIA_CACHE, row.get("status"));
            }
        }
    }

}
