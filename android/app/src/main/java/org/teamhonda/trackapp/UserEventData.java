package org.teamhonda.trackapp;

import java.util.Date;

public class UserEventData implements TimeBasedEvent {
    private String title;
    private String description;
    private Date start;
    private Date end;
    private String timezone;
    private String location;

    public UserEventData(String title, String description, Date start, Date end, String timezone, String location) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.timezone = timezone;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getLocation() {
        return location;
    }
}
