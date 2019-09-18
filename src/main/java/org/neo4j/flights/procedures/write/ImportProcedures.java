package org.neo4j.flights.procedures.write;

import net.openhft.chronicle.map.ChronicleMap;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.neo4j.flights.procedures.Labels.Segment;
import static org.neo4j.flights.procedures.Properties.*;

public class ImportProcedures {

    @Context
    public GraphDatabaseService db;

    @Context
    public TerminationGuard guard;

    @Context
    public Log log;

    private final static int TOUCH_AFTER_MINUTES = 60;

    private final static ChronicleMap<String, CachedValue> segmentMap = ChronicleMap
            .of(String.class, CachedValue.class)
            .name("segments")
            .entries(50)    // TODO: ??
            .averageKeySize(30)
            .averageValue( new CachedValue( 1L,20D, LocalDateTime.now()) )
            .create();

    @Procedure(name="flights.import.batch", mode = Mode.WRITE)
    public Stream<ImportResult> importBatch( @Name("batch") List< Map<String, Object> > batch ) {
        warmUp();

        return batch.stream()
                .map(this::importSegment);
    }

    public ImportResult importSegment(Map<String, Object> row) {
        Node node = null;

        String code = (String) row.get(CODE);
        double price = (double) row.get(PRICE);
        LocalDateTime updatedAt = (LocalDateTime) row.getOrDefault(UPDATED_AT, LocalDateTime.now());

        CachedValue cachedValue = segmentMap.get( code );

        if ( cachedValue != null ) {
            // Node already exists and is cached - check to see if the price is stale or the last update
            // was more than `TOUCH_AFTER_MINUTES` minutes ago
            if (
                cachedValue.getPrice() != price
                || cachedValue.getUpdatedAt().plusMinutes(TOUCH_AFTER_MINUTES).isBefore( updatedAt )
            ) {
                node = db.getNodeById( cachedValue.getId() );

                node.setProperty(PRICE, price);
                node.setProperty(UPDATED_AT, updatedAt);

                cachedValue = CachedValue.fromNode(node);
            }
        }
        else {
            // Try to find in DB
            node = db.findNode(Segment, CODE, code);

            // Check Value
            if (node != null) {
                // Update the property if node is stale
                if ((double) node.getProperty(PRICE) != (double) price) {
                    node.setProperty(PRICE, price);
                    node.setProperty(UPDATED_AT, updatedAt);
                }
            } else {
                // Node needs to be created
                node = db.createNode(Segment);

                node.setProperty(CODE, code);
                node.setProperty(PRICE, price);
                node.setProperty(UPDATED_AT, updatedAt);
            }

            // Create Cached Value
            cachedValue = CachedValue.fromNode(node);
        }

        // Update in map
        segmentMap.put(code, cachedValue);

        return new ImportResult(node);
    }

    @Procedure(name="flights.import.warmup")
    public Stream<WarmUpResult> warmUp() {
        db.findNodes(Segment).stream()
            .forEach(node -> {
                segmentMap.put((String) node.getProperty("code"), CachedValue.fromNode(node));
            });

        return Stream.of( new WarmUpResult("ok", segmentMap.size()) );
    }
}
