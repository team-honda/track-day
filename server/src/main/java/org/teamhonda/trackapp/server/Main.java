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

    String mongoHost = System.getenv("MONGO_HOST");
    if (mongoHost == null) mongoHost = "localhost";
    final MongoClient mongo = new MongoClient(mongoHost);
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

