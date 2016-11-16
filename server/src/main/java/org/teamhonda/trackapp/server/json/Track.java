package org.teamhonda.trackapp.server.json;

/**
 * Created 2016-11-16 8:57 AM by gordon.
 */
public class Track {

public final String name;
public final Double latitude;
public final Double longitude;
public final String url;

public Track(String name, Double latitude, Double longitude, String url) {
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
    this.url = url;
}

}
