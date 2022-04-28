package com.comugamers.sentey.common.pubsub.type.nats;

import com.comugamers.sentey.common.connection.Connection;
import com.comugamers.sentey.common.pubsub.PubSubHandler;
import com.comugamers.sentey.common.pubsub.message.Message;
import com.comugamers.sentey.common.pubsub.message.handler.MessageHandler;
import io.nats.client.Dispatcher;
import io.nats.client.impl.NatsMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class NatsPubSubHandler implements PubSubHandler {

    private final Plugin plugin;
    private final Connection<io.nats.client.Connection> natsConnection;
    private final Set<String> subscribedChannels;

    private MessageHandler messageHandler;
    private Dispatcher subscriber;
    private BukkitTask subscriberTask;

    public NatsPubSubHandler(Plugin plugin, Connection<io.nats.client.Connection> natsConnection) {
        this.natsConnection = natsConnection;
        this.plugin = plugin;
        this.subscribedChannels = new HashSet<>();
        this.start();

    }

    public NatsPubSubHandler(Plugin plugin, Connection<io.nats.client.Connection> natsConnection, MessageHandler messageHandler) {
        this.natsConnection = natsConnection;
        this.plugin = plugin;
        this.subscribedChannels = new HashSet<>();
        this.setMessageHandler(messageHandler);
        this.start();
    }

    @Override
    public void sendMessage(String channel, String message) {
        natsConnection.get().publish(
                NatsMessage.builder()
                        .subject(channel)
                        .data(message, StandardCharsets.UTF_8)
                        .build()
        );
    }

    @Override
    public void subscribeToChannel(String... channels) {
        for (String channel : channels) {
            subscriber.subscribe(channel);
            this.subscribedChannels.add(channel);
        }
    }

    @Override
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void start() {
        subscriberTask = Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            subscriber = natsConnection.get().createDispatcher(message -> {
                String response = new String(message.getData(), StandardCharsets.UTF_8);
                messageHandler.onMessage(
                        new Message(
                                message.getSubject(),
                                response
                        )
                );
            });
        });
    }

    @Override
    public void stop() {
        subscriberTask.cancel();
        this.subscribedChannels.forEach(s -> subscriber.unsubscribe(s));
    }
}
