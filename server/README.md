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

#### Importing test data-sets into Mongo DB

The `mongoimport` command can be used with the JSON files in `src/test/mongo/datasets` to import some data for testing purposes. The command looks something like this:

    mongoimport --host=$MONGO_HOST --db=trackapp --collection=events --jsonArray < src/test/mongo/datasets/events.json

If you're running Mongo DB in Docker, you can run this command in the Mongo DB container using Docker:

     docker exec -i mongo sh -c 'mongoimport --db=trackapp --collection=events --jsonArray' < src/test/mongo/datasets/events.json

_IMPORTANT:_ Mongo DB doesn't use traditional table schema with primary keys, so running the import command multiple times will create duplicate objects in the collection. If you want to clear the collection before importing the data, add the `--drop` option.

### Building

    gradlew build

### Running

    gradlew run

Server binds to port 8080 on all interfaces by default. To override this, export `TRACKAPP_SERVER_HOST` and/or `TRACKAPP_SERVER_PORT` env vars from your shell.
