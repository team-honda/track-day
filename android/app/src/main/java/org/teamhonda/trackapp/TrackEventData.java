package org.teamhonda.trackapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TrackEventData implements TimeBasedEvent {
    private final SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private String date;
    private String track;
    private String layout;
    private String layoutMapUrl;
    private double latitude;
    private double longitude;
    private String organizer;
    private String organizerWebsiteUrl;
    private String preRegCutoffDate;
    private double preRegCost;
    private double normalCost;
    private boolean registered;
    private String notes;

    public Date getStart() {
        try {
            return dateFmt.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Can't parse date " + date, e);
        }
    }

    public Date getEnd() {
        return new Date(getStart().getTime() + 1);
    }

    public String getTrack() {
        return track;
    }

    public String getLayout() {
        return layout;
    }

    public String getLayoutMapUrl() {
        return layoutMapUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getOrganizerWebsiteUrl() {
        return organizerWebsiteUrl;
    }

    public String getPreRegCutoffDate() {
        return preRegCutoffDate;
    }

    public double getPreRegCost() {
        return preRegCost;
    }

    public double getNormalCost() {
        return normalCost;
    }

    public boolean isRegistered() {
        return registered;
    }

    public String getNotes() {
        return notes;
    }
}
