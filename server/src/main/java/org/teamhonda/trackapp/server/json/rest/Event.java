package org.teamhonda.trackapp.server.json.rest;

import org.bson.Document;

/**
 * @author Gordon Tyler
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Event {

public final String organizer;
public final String organizerWebsiteUrl;
public final String name;
public final String date;
public final String track;
public final String layout;
public final Double latitude;
public final Double longitude;

public Event(String organizer, String organizerWebsiteUrl, String name, String date,
        String track, String layout, Double latitude, Double longitude)
{
    this.organizer = organizer;
    this.organizerWebsiteUrl = organizerWebsiteUrl;
    this.name = name;
    this.date = date;
    this.track = track;
    this.layout = layout;
    this.latitude = latitude;
    this.longitude = longitude;
}

public static Event fromDocs(Document eventDoc, Document trackDoc) {
    Double latitude = null;
    Double longitude = null;

    if (trackDoc != null) {
        latitude = trackDoc.getDouble("latitude");
        longitude = trackDoc.getDouble("longitude");
    }

    return new Event(
            eventDoc.getString("organizer"),
            eventDoc.getString("organizer_url"),
            eventDoc.getString("name"),
            eventDoc.getString("date"),
            eventDoc.getString("track"),
            eventDoc.getString("layout"),
            latitude, longitude);
}

}
