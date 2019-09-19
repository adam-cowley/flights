package org.neo4j.flights.procedures;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.neo4j.graphdb.RelationshipType;

public class DateUtils
{
    public final static DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern( "yyyyMMdd" );
    public final static DateTimeFormatter relationshipFormat = DateTimeFormatter.ofPattern( "yyyyMMddHH" );

    public static ZonedDateTime relationshipTypeToZonedDateTime( RelationshipType type ) {
        LocalDateTime loc = LocalDateTime.parse(type.toString(), relationshipFormat);

        return loc.atZone( ZoneOffset.UTC );
    }

    public static RelationshipType zonedDateTimeToDay( ZonedDateTime time ) {
        return RelationshipType.withName( time.format(dayFormat) );
    }

    public static RelationshipType zonedDateTimeToHour( ZonedDateTime time ) {
        return RelationshipType.withName( time.format(relationshipFormat) );
    }

}
