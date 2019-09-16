package org.neo4j.flights;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.flights.procedures.GetFlights;
import org.neo4j.flights.procedures.ValidRoutes;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.harness.junit.Neo4jRule;

import java.io.File;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;

public class ValidRoutesTest {

    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withProcedure( ValidRoutes.class )
            .withFixture("CREATE CONSTRAINT ON (node:Airport) ASSERT (node.code) IS UNIQUE")
            .withFixture("CREATE CONSTRAINT ON (node:`UNIQUE IMPORT LABEL`) ASSERT (node.`UNIQUE IMPORT ID`) IS UNIQUE")
            .withFixture(
                    new File(
                        getClass().getClassLoader().getResource("cypher/fliesto.cypher").getFile()
                    )
            );


    @Test
    public void shouldFindValidRoutes() {
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

//                List<Node> ap = (List<Node>) row.get("airports");

                System.out.println("");
                System.out.println("--");
                System.out.println(row.get("route"));

//                for ( Node a : ap ) {
//                    System.out.println(a.getProperty("code"));
//                }
            }

            System.out.println(routes);
        }
    }
}
