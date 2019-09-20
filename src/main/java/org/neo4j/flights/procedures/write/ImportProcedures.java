package org.neo4j.flights.procedures.write;

import net.openhft.chronicle.map.ChronicleMap;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.io.File;
import java.io.IOException;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.neo4j.flights.procedures.DateUtils.dayFormat;
import static org.neo4j.flights.procedures.DateUtils.zonedDateTimeToHour;
import static org.neo4j.flights.procedures.Labels.*;
import static org.neo4j.flights.procedures.Properties.*;

public class ImportProcedures {

    @Context
    public GraphDatabaseService db;

    @Context
    public TerminationGuard guard;

    @Context
    public Log log;

    private final static int TOUCH_AFTER_MINUTES = 60;

    private static ChronicleMap<String, CachedValue> segments;
    private static ChronicleMap<String, Long> airports;

    public ImportProcedures() throws IOException {
        segments = ChronicleMap.of(String.class, CachedValue.class)
                .name("segments")
                .entries(1000)
                .averageKeySize(30)
                .averageValue(new CachedValue(0L, 20D, LocalDateTime.now()))
                .createOrRecoverPersistedTo(new File(System.getProperty("user.home") + "/segments.dat"), false);

        airports = ChronicleMap.of(String.class, Long.class)
                .name("airports")
                .entries(10_000)
                .averageKeySize(3)
//                .averageValueSize(20)
                .create();
    }

    @Procedure(name="flights.import.warmup")
    public Stream<WarmUpResult> warmUp() {
        db.findNodes(Airport).stream()
                .forEach(node -> airports.put((String) node.getProperty("code"), node.getId()));

        db.findNodes(Segment).stream()
                .forEach(node -> {
                    segments.put((String) node.getProperty("code"), CachedValue.fromNode(node));
                });

        return Stream.of( new WarmUpResult("ok", airports.size(), segments.size()) );
    }

    @Procedure(name="flights.import.clearCache")
    public Stream<WarmUpResult> clearCache() {
        segments.clear();

        return Stream.of( new WarmUpResult("ok", airports.size(), segments.size()) );
    }

    @Procedure(name="flights.import.viaCache", mode = Mode.WRITE)
    public Stream<ImportResult> importBatchViaCache( @Name("batch") List< Map<String, Object> > batch ) {
        warmUp();

        return batch.stream()
                .map(this::importSegmentViaCache);
    }

    @Procedure(name="flights.import.segment", mode = Mode.WRITE)
    public Stream<ImportResult> importSegment(@Name("row") Map<String, Object> row) {
        String code = (String) row.get(CODE);
        double price = (double) row.get(PRICE);
        LocalDateTime updatedAt = (LocalDateTime) row.getOrDefault(UPDATED_AT, LocalDateTime.now());

        // Try to find in DB
        Node node = db.findNode(Segment, CODE, code);

        // Check Value
        if (node != null) {
            // Update the property if node is stale
            if ((double) node.getProperty(PRICE) != price) {
                node.setProperty(PRICE, price);
//                node.setProperty(UPDATED_AT, updatedAt);

                return Stream.of( new ImportResult(code, ImportResult.STATUS_UPDATED_VIA_NODE) );
            }
        } else {
            // Node needs to be created
            node = createSegment(row);

            return Stream.of( new ImportResult(code, ImportResult.STATUS_CREATED) );
        }

        return Stream.of( new ImportResult(code, ImportResult.STATUS_IGNORED_VIA_NODE) );
    }

    private ImportResult importSegmentViaCache(Map<String, Object> row) {
        Node node = null;

        String code = (String) row.get(CODE);

        // Is it cached?
        CachedValue cachedValue = segments.get(code);

        if (cachedValue != null) {
            double price = (double) row.get(PRICE);
            LocalDateTime updatedAt = (LocalDateTime) row.getOrDefault(UPDATED_AT, LocalDateTime.now());

            // Node already exists and is cached - check to see if the price is stale or the last update
            // was more than `TOUCH_AFTER_MINUTES` minutes ago
            if (
                    cachedValue.getPrice() != price
                    || cachedValue.getUpdatedAt().plusMinutes(TOUCH_AFTER_MINUTES).isBefore(updatedAt)
            ) {
                node = db.getNodeById(cachedValue.getId());

                node.setProperty(PRICE, price);
                node.setProperty(UPDATED_AT, updatedAt);

                cachedValue = CachedValue.fromNode(node);

                segments.put(code, cachedValue);

                return new ImportResult(code, ImportResult.STATUS_UPDATED_VIA_CACHE);
            }

            return new ImportResult(code, ImportResult.STATUS_IGNORED_VIA_CACHE);
        }

        // Import segment
        Optional<ImportResult> first = importSegment(row).findFirst();

        return first.get();
    }

    private Node mergeAirport(String code) {
        if ( airports.containsKey(code) ) {
            return db.getNodeById( airports.get(code) );
        }

        Node airport = db.findNode(Airport, CODE, code);

        // If it doesn't exist then create it
        if ( airport == null ) {
            airport = db.createNode(Airport);

            airport.setProperty(CODE, code);
        }

        // Add it to the cache
        airports.put( code, airport.getId() );

        return airport;
    }

    private Node createSegment(Map<String, Object> row) {
        Node airportDay = mergeAirportDay(row);
        Node airportDestination = mergeAirportDestination(airportDay, row);

        Node destination = mergeAirport( (String) row.get("destination") );

        // Create Segment Node
        Node segment = db.createNode(Segment);

        segment.setProperty(CODE, (String) row.get(CODE));
        segment.setProperty(PRICE, (Double) row.get(PRICE));
        segment.setProperty(STOPOVERS, (Long) row.get(STOPOVERS));
        segment.setProperty(DEPARTS, (ZonedDateTime) row.get(DEPARTS) );
        segment.setProperty(ARRIVES, (ZonedDateTime) row.get(ARRIVES) );
        segment.setProperty(UPDATED_AT, (LocalDateTime) row.getOrDefault(UPDATED_AT, LocalDateTime.now()));

        // (:AirportDestination)-[:YYYYMMDDHH]->(:Segment)
        airportDestination.createRelationshipTo(segment, zonedDateTimeToHour( (ZonedDateTime) row.get("departs") ));

        // (:Segment)-[:YYYYMMDDHH]->(:Airport)
        segment.createRelationshipTo(destination, zonedDateTimeToHour( (ZonedDateTime) row.get("arrives") ));

        return segment;
    }

    private Node mergeAirportDay(Map<String, Object> row) {
        ZonedDateTime departs = (ZonedDateTime) row.get(DEPARTS);
        String departureDay = departs.format(dayFormat);

        String code = row.get("origin") +"-"+ departureDay;

        Node airportDay = db.findNode(AirportDay, CODE, code);

        if ( airportDay == null ) {
            airportDay = db.createNode(AirportDay);

            airportDay.setProperty(CODE, code);

            Node airport = mergeAirport( (String) row.get("origin" ));
            airport.createRelationshipTo(airportDay, RelationshipType.withName( departureDay ) ) ;
        }

        return airportDay;
    }

    private Node mergeAirportDestination(Node airportDay, Map<String, Object> row) {
        String airportDayCode = (String) airportDay.getProperty(CODE);
        String destination = (String) row.get("destination");
        ZonedDateTime departs = (ZonedDateTime) row.get(DEPARTS);

        String code = airportDayCode +"-"+ destination;

        Node airportDestination = db.findNode(AirportDestination, CODE, code);

        if ( airportDestination == null ) {
            airportDestination = db.createNode(AirportDestination);
            airportDestination.setProperty(CODE, code);

            airportDay.createRelationshipTo(airportDestination, RelationshipType.withName(destination));
        }

        return airportDestination;
    }


}
