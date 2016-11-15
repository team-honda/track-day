package org.teamhonda.trackapp.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teamhonda.trackapp.server.json.Event;
import org.teamhonda.trackapp.server.scrapers.Scrapers;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

/**
 * @author Gordon Tyler
 */
public class Main {

private static final Logger logger = LoggerFactory.getLogger(Main.class);
private static final Charset UTF8 = StandardCharsets.UTF_8;
private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

public static void main(String[] args) {
    final Gson gson = new GsonBuilder().create();

    String mongoHost = System.getenv("MONGO_HOST");
    if (mongoHost == null) mongoHost = "localhost";
    final MongoClient mongo = new MongoClient(mongoHost);
    final MongoDatabase db = mongo.getDatabase("trackapp");

    final AtomicBoolean scraping = new AtomicBoolean(false);

    RoutingHandler rootHandler = Handlers.routing()
            .get("/events", new DispatchHandler(exchange -> {
                Map<String, Deque<String>> qp = exchange.getQueryParameters();

                Bson eventsFilter = new Document(); // no filter
                if (qp.containsKey("start") && qp.containsKey("end")) {
                    eventsFilter = and(
                            gte("date", qp.get("start").getFirst()),
                            lte("date", qp.get("end").getFirst())
                    );
                }
                else if (qp.containsKey("start")) {
                    eventsFilter = gte("date", qp.get("start").getFirst());
                }
                else if (qp.containsKey("end")) {
                    eventsFilter = lte("date", qp.get("end").getFirst());
                }

                ArrayList<Document> eventDocs = db.getCollection("events")
                        .find()
                        .filter(eventsFilter)
                        .into(new ArrayList<>());
                Set<String> trackNames = eventDocs.stream()
                        .map(d -> d.getString("track"))
                        .distinct()
                        .collect(Collectors.toSet());
                Map<String,Document> trackDocs = db.getCollection("tracks")
                        .find(in("name", trackNames))
                        .into(new ArrayList<>())
                        .stream()
                        .collect(Collectors.toMap(d -> d.getString("name"), d -> d));

                List<Event> results = eventDocs.stream()
                        .map(ed -> Event.fromDocs(ed, trackDocs.get(ed.getString("track"))))
                        .collect(Collectors.toList());

                exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, CONTENT_TYPE_JSON);
                exchange.getResponseSender().send(gson.toJson(results), UTF8);
            }))
            .post("/scrape", new DispatchHandler(exchange -> {
                if (scraping.compareAndSet(false, true)) {
                    try {
                        Scrapers.scrapeAll(db.getCollection("events"));
                    }
                    finally {
                        scraping.compareAndSet(true, false);
                    }
                }
                else {
                    exchange.setStatusCode(StatusCodes.BAD_REQUEST);
                    exchange.getResponseSender().send("{\"error\":\"scraping already running\"}");
                }
            }));

    String serverHost = System.getenv("TRACKAPP_SERVER_HOST");
    if (serverHost == null) serverHost = "0.0.0.0";

    int serverPort = 8080;
    if (System.getenv().containsKey("TRACKAPP_SERVER_PORT")) {
        serverPort = Integer.parseInt(System.getenv("TRACKAPP_SERVER_PORT"));
    }

    final Undertow server = Undertow.builder()
            .addHttpListener(serverPort, serverHost, rootHandler)
            .build();

    try {
        server.start();
    }
    catch (Exception e) {
        logger.error("Could not start server", e);
        server.stop();
        mongo.close();
    }
}

}

