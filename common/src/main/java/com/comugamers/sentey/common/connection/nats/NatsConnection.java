package com.comugamers.sentey.common.connection.nats;

import com.comugamers.sentey.common.connection.Connection;
import io.nats.client.AuthHandler;
import io.nats.client.JetStreamOptions;
import io.nats.client.Nats;

import java.io.IOException;

public class NatsConnection implements Connection<io.nats.client.Connection> {

    private final String uri;
    private final String authFileLocation;
    private io.nats.client.Connection connection;

    public NatsConnection(String uri, String authFileLocation) {
        this.uri = uri;
        this.authFileLocation = authFileLocation;
    }

    @Override
    public void connect() {

        try {
            if (authFileLocation != null) {
                AuthHandler authHandler = Nats.credentials(authFileLocation);
                connection = Nats.connect(uri, authHandler);
            }

            connection = Nats.connect(uri);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public io.nats.client.Connection get() {

        if (connection == null) {
            this.connect();
        }

        return connection;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
