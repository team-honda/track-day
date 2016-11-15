package org.teamhonda.trackapp.server.scrapers;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Gordon Tyler
 */
public class Scrapers {

private static final Logger LOG = LoggerFactory.getLogger(Scrapers.class);

private static final List<Scraper> SCRAPERS = Arrays.asList(
        new RidersChoice()
);

public static void scrapeAll(MongoCollection<Document> events) {
    EventUpdater updater = new EventUpdater(events);

    for (Scraper scraper : SCRAPERS) {
        try {
            scraper.scrape(updater);
        }
        catch (IOException e) {
            LOG.error("Couldn't scrape " + scraper.getName(), e);
        }
    }

    updater.finish();
}

}
