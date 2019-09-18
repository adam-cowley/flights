package org.neo4j.flights.procedures.write;

import org.neo4j.graphdb.Node;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.neo4j.flights.procedures.Properties.*;

class CachedValue implements Serializable {
    public long id;
    public double price;
    public LocalDateTime updatedAt;

    public CachedValue(long id, double price, LocalDateTime updatedAt) {
        this.id = id;
        this.price = price;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static CachedValue fromNode(Node node) {
        return new CachedValue(
                node.getId(),
                (double) node.getProperty(PRICE),
                (LocalDateTime) node.getProperty(UPDATED_AT)
        );
    }
}
