package org.neo4j.flights;

import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.neo4j.flights.procedures.GetFlights;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.harness.junit.Neo4jRule;

public class ProcTest
{

    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withProcedure( GetFlights.class )
            .withFixture( "CREATE (a1:Airport {code: 'IBZ'}) " +
                    "CREATE (a2:Airport {code: 'BCN'}) " +
                    "CREATE (a3:Airport {code: 'JFK'}) " +
                    "CREATE (a1)-[:`2019070100`]->(:Segment {id: 1,price:50.0})-[:`2019070102`]->(a2) " +
                    "CREATE (a2)-[:`2019070103`]->(:Segment {id: 2,price:50.0})-[:`2019070107`]->(a3) " +
                    "CREATE (a2)-[:`2019070100`]->(:Segment {id: 3,price:50.0})-[:`2019070105`]->(a3)" +
                    "CREATE (a2)-[:`2019070112`]->(:Segment {id: 3,price:200.00})-[:`2019070105`]->(a3)"
            )
//            .copyFrom( new File("/Users/adam/Library/Application Support/Neo4j Desktop/Application/neo4jDatabases/database-5e861f6f-3354-4c95-9b9f-996ecfc5ae83/installation-3.5.7/data") )
//            .withConfig( "dbms.active_database", "small.db" )
            ;


//    @Test
//    public void doSomething() {
//        GraphDatabaseService db = neo4j.getGraphDatabaseService();
//
//        try ( Transaction tx = db.beginTx() ) {
//            Result res = db.execute( "MATCH (n) RETURN count(n) AS count" );
//
//            System.out.println( res.next().get("count") );
//        }
//    }

    @Test
    public void shouldFindFlightsAtOneHop() {
        GraphDatabaseService db = neo4j.getGraphDatabaseService();

        try ( Transaction tx = db.beginTx() ) {
            Result res = db.execute( "CALL flights.between('IBZ', 'BCN', date('2019-07-01')) YIELD path RETURN path" );

            while ( res.hasNext() ) {
                Map<String, Object> row = res.next();

                System.out.println("Output: "+ row);
            }
        }
    }

    @Test
    public void shouldFindFlightsAtTwoHops() {
        GraphDatabaseService db = neo4j.getGraphDatabaseService();

        try ( Transaction tx = db.beginTx() ) {
            Result res = db.execute( "CALL flights.between('IBZ', 'JFK', date('2019-07-01')) YIELD path RETURN path" );

            while ( res.hasNext() ) {
                Map<String, Object> row = res.next();

                System.out.println("Output: "+ row.toString());
            }
        }
    }

}
