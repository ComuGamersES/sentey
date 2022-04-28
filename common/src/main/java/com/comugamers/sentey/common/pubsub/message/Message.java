package com.comugamers.sentey.common.pubsub.message;

public class Message {

    private final String channel;
    private final String message;

    public Message(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }
}
