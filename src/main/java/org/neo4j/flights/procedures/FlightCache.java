package org.neo4j.flights.procedures;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Cache;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class FlightCache {

    private final static Cache<String, Double> cache = Caffeine.newBuilder()
            .expireAfterWrite(20, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build();

    public static class Flight {
        Node start;
        Node end;
        RelationshipType departure;

        public Flight(Node start, Node end, RelationshipType departure) {
            this.start = start;
            this.end = end;
            this.departure = departure;
        }

        public String toString() {
            return "["+ start.getId() +" -> "+ end.getId() +" on "+ departure +"]";
        }
    }

    public FlightCache() {}

    /**
     * Check against the current flight in the cache and set if appropriate
     *
     * @return boolean
     */
    public boolean isCheapest(Flight key, double cost) {
        Double cachedCost = cache.getIfPresent(key.toString());

        if ( cachedCost == null || cachedCost >= cost ) {
            cache.put(key.toString(), cost);

            return true;
        }

        return false;
    }

    public static class FlightCost {
//        public Node start;
//        public Node end;
//        public String relationshipType;
        public String key;
        public Double cost;

        public FlightCost(String key, Double cost) {
            this.key = key;
            this.cost = cost;
        }
    }

    public void clear() {
        cache.invalidateAll();
    }

    public Stream<FlightCost> getCache() {
        return cache.asMap().entrySet().stream()
                .map(item -> new FlightCost(item.getKey(), item.getValue()));
    }

}
