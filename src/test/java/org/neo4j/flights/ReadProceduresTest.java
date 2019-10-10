package org.neo4j.flights;

import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.flights.procedures.ReadProcedures;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.harness.junit.Neo4jRule;

import java.io.File;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ReadProceduresTest {

    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withProcedure( ReadProcedures.class )
            .withFixture("CREATE CONSTRAINT ON (node:Airport) ASSERT (node.code) IS UNIQUE")
            .withFixture("CREATE CONSTRAINT ON (node:AirportDay) ASSERT (node.code) IS UNIQUE")
            .withFixture("CREATE CONSTRAINT ON (node:AirportDestination) ASSERT (node.code) IS UNIQUE")
            .withFixture("CREATE CONSTRAINT ON (node:Segment) ASSERT (node.code) IS UNIQUE")
            .withFixture("CREATE CONSTRAINT ON (node:`UNIQUE IMPORT LABEL`) ASSERT (node.`UNIQUE IMPORT ID`) IS UNIQUE")
            .withFixture(
                    new File(
                            getClass().getClassLoader().getResource("cypher/all.cypher").getFile()
                    )
            )
            .withFixture(
                    new File(
                            getClass().getClassLoader().getResource("cypher/fliesto.cypher").getFile()
                    )
            )
            //.copyFrom( new File("/Users/adam/Library/Application Support/Neo4j Desktop/Application/neo4jDatabases/database-f6b4d488-a224-4bd8-9604-db6da4a0f483/installation-3.5.8/data") )
            ;


    @Test
    public void shouldFindValidRoutesWithNoDirectRoutes() {
        GraphDatabaseService db = neo4j.getGraphDatabaseService();

        try ( Transaction tx = db.beginTx() ) {
            Result res = db.execute( "CALL flights.validRoutes('IBZ', 'JFK')" );

            System.out.println("");
            System.out.println("--");

            assertTrue(res.hasNext());

            int routes = 0;

            while ( res.hasNext() ) {
                Map<String, Object> row = res.next();

                routes ++;

                System.out.println("");
                System.out.println("--");
                System.out.println(row.get("route") + " == "+ row.get("cost"));
            }

            System.out.println(routes);
        }
    }

    public void assertFlightResults(String query, int expectedResults) {
        GraphDatabaseService db = neo4j.getGraphDatabaseService();

        try ( Transaction tx = db.beginTx() ) {
            Result res = db.execute("CALL flights.between('IBZ', 'MAD', date('2019-09-22'))");

            System.out.println("");
            System.out.println("");

            int results = 0;

            while ( res.hasNext() ) {
                results++;

                Map<String,Object> row = res.next();

                System.out.println("");
                System.out.println("--");
                System.out.println(row.get("path") + " == "+ row.get("cost"));
            }

            assertEquals(expectedResults, results);
        }
    }

    @Test
    public void shouldFindFlightsBetweenAirportDirectRoutes() {
        assertFlightResults("CALL flights.between('IBZ', 'MAD', date('2019-09-22'))", 1);
    }

    @Test
    public void shouldFindFlightsBetweenAirportWithOneStopovers() {
        assertFlightResults("CALL flights.between('IBZ', 'JFK', date('2019-09-22'))", 1);
    }

    @Test
    public void shouldBeQuickerIfItDoesntNeedToWorkOutBetween() {
        GraphDatabaseService db = neo4j.getGraphDatabaseService();

        try ( Transaction tx = db.beginTx() ) {
            Result res = db.execute(
             "MATCH (a:Airport) WHERE a.code IN [\"BCN\", \"MAD\", \"MRS\", \"JFK\", \"LED\", \"GDN\", \"AMS\", \"KTW\", \"DXB\", \"CGN\", \"ABZ\"] " +
                    "WITH collect(a) AS airports " +
                    "MATCH (o:Airport {code: 'IBZ'}) " +
                    "MATCH (d:Airport {code: 'JFK'}) " +
                    "CALL flights.betweenWithAirports(o, d, date('2019-09-22'), airports, 3, 2000.00) YIELD path, distance, cost RETURN *" );

            System.out.println("");
            System.out.println("");

            while ( res.hasNext() ) {
                Map<String,Object> row = res.next();

                System.out.println("");
                System.out.println("--");
                System.out.println("Res:" + row);
            }
        }
    }

    @Test
    public void shouldGetValidRoutesStartingAtOneHop() {
        GraphDatabaseService db = neo4j.getGraphDatabaseService();

        try ( Transaction tx = db.beginTx() ) {
            Result res = db.execute( "CALL flights.validRoutes('IBZ', 'MAD')" );

            System.out.println("");
            System.out.println("--");

            assertTrue(res.hasNext());

            int routes = 0;

            while ( res.hasNext() ) {
                Map<String, Object> row = res.next();

                routes ++;

                System.out.println("");
                System.out.println("--");
                System.out.println("RES: "+ row.get("route"));
            }

            System.out.println(routes);
        }
    }

    @Test
    public void shouldGetValidRoutesStartingAtTwoHops() {
        GraphDatabaseService db = neo4j.getGraphDatabaseService();

        try ( Transaction tx = db.beginTx() ) {
            Result res = db.execute( "CALL flights.validRoutes('IBZ', 'JFK')" );

            System.out.println("");
            System.out.println("--");

            assertTrue(res.hasNext());

            int routes = 0;

            while ( res.hasNext() ) {
                Map<String, Object> row = res.next();

                routes ++;

                System.out.println("");
                System.out.println("--");
                System.out.println("RES: "+ row.get("route"));
            }

            System.out.println(routes);
        }
    }

}
