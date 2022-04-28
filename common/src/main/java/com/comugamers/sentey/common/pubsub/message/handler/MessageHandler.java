package com.comugamers.sentey.common.pubsub.message.handler;

import com.comugamers.sentey.common.pubsub.message.Message;

public interface MessageHandler {

    void onMessage(Message message);

}
