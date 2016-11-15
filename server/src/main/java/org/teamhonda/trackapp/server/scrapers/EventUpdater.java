package org.teamhonda.trackapp.server.scrapers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertOneModel;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gordon Tyler
 */
public class EventUpdater {

private final MongoCollection<Document> events;
private final List<Document> newEvents = new ArrayList<>();

public EventUpdater(MongoCollection<Document> events) {
    this.events = events;
}

public void update(String organizer, String organizerWebsiteUrl, String name, String date,
        String track, String layout) {
    newEvents.add(new Document()
            .append("organizer", organizer)
            .append("organizer_url", organizerWebsiteUrl)
            .append("name", name)
            .append("date", date)
            .append("track", track)
            .append("layout", layout)
    );
}


public void finish() {
    events.drop();
    events.bulkWrite(newEvents.stream().map(InsertOneModel::new).collect(Collectors.toList()));
}

}
