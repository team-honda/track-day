package org.teamhonda.trackapp;

import java.util.List;

public class Schedule {
    private final List<UserEventData> userEvents;
    private final List<TrackEventData> trackEvents;

    public Schedule(List<UserEventData> userEvents, List<TrackEventData> trackEvents) {
        this.userEvents = userEvents;
        this.trackEvents = trackEvents;
    }
}
