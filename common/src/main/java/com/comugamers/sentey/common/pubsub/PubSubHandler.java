package com.comugamers.sentey.common.pubsub;

import com.comugamers.sentey.common.Service;
import com.comugamers.sentey.common.pubsub.message.handler.MessageHandler;

public interface PubSubHandler extends Service {

    void sendMessage(String channel, String message);

    void subscribeToChannel(String... channels);

    void setMessageHandler(MessageHandler messageHandler);

}
