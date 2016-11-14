# Track Days Server

REST API server for the Track Days app.

## Requirements

1. Java 8
2. Mongo DB

### Mongo DB

The server expects Mongo DB to be available at `$MONGO_HOST:27017`, defaulting to `localhost:27017` if the env var is not set.

### Building

```
gradlew build
```

### Running

```
gradlew run
```

Server binds to `0.0.0.0:8080` by default. To override this, export `TRACKAPP_SERVER_HOST` and/or `TRACKAPP_SERVER_PORT` env vars from your shell.
