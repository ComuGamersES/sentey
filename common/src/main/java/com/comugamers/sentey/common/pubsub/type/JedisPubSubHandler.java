package com.comugamers.sentey.common.pubsub.type;

import com.comugamers.sentey.common.connection.Connection;
import com.comugamers.sentey.common.pubsub.PubSubHandler;
import com.comugamers.sentey.common.pubsub.message.Message;
import com.comugamers.sentey.common.pubsub.message.handler.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class JedisPubSubHandler implements PubSubHandler {

    private final Plugin plugin;
    private final Connection<Jedis> jedisConnection;
    private MessageHandler messageHandler;
    private JedisPubSub subscriber;
    private BukkitTask subscriberTask;

    public JedisPubSubHandler(Plugin plugin, Connection<Jedis> jedisConnection) {
        this.jedisConnection = jedisConnection;
        this.plugin = plugin;
        this.start();
    }

    public JedisPubSubHandler(Plugin plugin, Connection<Jedis> jedisConnection, MessageHandler messageHandler) {
        this.jedisConnection = jedisConnection;
        this.plugin = plugin;
        this.setMessageHandler(messageHandler);
        this.start();
    }

    @Override
    public void sendMessage(String channel, String message) {
        jedisConnection.get().publish(channel, message);
    }

    @Override
    public void subscribeToChannel(String... channels) {
        try (Jedis jedis = jedisConnection.get()) {
            jedis.subscribe(subscriber, channels);
        }
    }

    @Override
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void start() {
        subscriberTask = Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            subscriber = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    messageHandler.onMessage(new Message(channel, message));
                }
            };
        });
    }

    @Override
    public void stop() {
        subscriberTask.cancel();
        subscriber.unsubscribe();
    }
}
