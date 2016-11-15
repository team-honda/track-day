package org.teamhonda.trackapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class ServerAdapter {
    private final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    // TODO replace with actual read from REST interface
    private String readJson() {
        return "[{ \n" +
                "  \"date\": \"2016-11-23\",\n" +
                "  \"track\": \"Shannonville\",\n" +
                "  \"layout\": \"Long Track\",\n" +
                "  \"layoutMapUrl\": \"http://www.dirtygirlmotorracing.com/tracks/shannonville.html#long\",\n" +
                "  \"latitude\": 44.2254 ,\n" +
                "  \"longitude\": -77.16,\n" +
                "  \"organizer\": \"Riders Choice\",\n" +
                "  \"organizerWebsiteUrl\": \"http://riderschoice.ca\",\n" +
                "  \"normalCost\":170,\n" +
                "  \"registered\":false\n" +
                "}," +
                "{ \n" +
                "  \"date\": \"2016-11-25\",\n" +
                "  \"track\": \"Shannonville\",\n" +
                "  \"layout\": \"Long Track\",\n" +
                "  \"layoutMapUrl\": \"http://www.dirtygirlmotorracing.com/tracks/shannonville.html#long\",\n" +
                "  \"latitude\": 44.2254 ,\n" +
                "  \"longitude\": -77.16,\n" +
                "  \"organizer\": \"Riders Choice\",\n" +
                "  \"organizerWebsiteUrl\": \"http://riderschoice.ca\",\n" +
                "  \"normalCost\":170,\n" +
                "  \"registered\":false\n" +
                "}]";
    }

    public EventData[] getEventData(Date from, Date to) {
        return GSON.fromJson(readJson(), EventData[].class);
    }

}
