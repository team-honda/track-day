package org.teamhonda.trackapp.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teamhonda.trackapp.server.json.Tracks;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Gordon Tyler
 */
public class Main {

private static Logger logger = LoggerFactory.getLogger(Main.class);

public static void main(String[] args) {
    final Gson gson = new GsonBuilder().create();
    final MongoClient mongo = new MongoClient("192.168.99.100");
    final MongoDatabase db = mongo.getDatabase("trackapp");

    RoutingHandler rootHandler = Handlers.routing()
            .get("/tracks", new DispatchHandler(exchange -> {
                MongoCollection<Document> coll = db.getCollection("tracks");
                ArrayList<Document> docs = coll.find().into(new ArrayList<>());
                Tracks result = new Tracks(docs.stream()
                        .map(d -> d.getString("name"))
                        .collect(Collectors.toList()));

                exchange.getResponseSender().send(gson.toJson(result));

            }));

    final Undertow server = Undertow.builder()
            .addHttpListener(8080, "0.0.0.0", rootHandler)
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

