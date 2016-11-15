# Track Days Server

REST API server for the Track Days app.

## Requirements

1. Java 8
2. Mongo DB

### Mongo DB

The server expects Mongo DB to be available at `$MONGO_HOST:27017`, defaulting to `localhost:27017` if the env var is not set.

#### Mongo DB in Docker

If you're using Windows or OS X, first create a Docker VM using Docker Machine and export its IP as the `MONGO_HOST` var:

    docker-machine create -d virtualbox default
    export MONGO_HOST=$(docker-machine ip default)

Then start the Mongo DB container:

    docker run --name mongo -d -p 27017:27017 mongo

### Building

    gradlew build

### Running

    gradlew run

Server binds to port 8080 on all interfaces by default. To override this, export `TRACKAPP_SERVER_HOST` and/or `TRACKAPP_SERVER_PORT` env vars from your shell.
