package com.comugamers.sentey.core.ping.action.internal;

import com.comugamers.sentey.common.util.discord.DiscordWebhook;
import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.core.Sentey;
import com.comugamers.sentey.core.ping.action.PingAction;
import com.comugamers.sentey.core.util.PlaceholderUtil;
import com.google.inject.Inject;

import java.io.IOException;
import java.net.InetAddress;

public class WebhookPingAction implements PingAction {

    @Inject
    private YamlFile config;

    @Inject
    private Sentey plugin;

    @Override
    public void handle(InetAddress address) {
        // Check if we should send a message to a webhook
        if(config.getBoolean("config.server-list-ping.actions.webhook.enabled")) {
            // If so, create a new webhook
            DiscordWebhook webhook = new DiscordWebhook(
                    config.getString("config.server-list-ping.actions.webhook.url")
            );

            try {
                // Update the TTS option, the content of the message and then send it
                webhook.setTTS(
                        config.getBoolean("config.server-list-ping.actions.webhook.tts", false)
                ).setContent(
                        PlaceholderUtil.applyPlaceholdersFromPingContext(
                                config.getString("config.server-list-ping.actions.webhook.message"), address
                        )
                ).execute();
            } catch (IOException e) {
                // If an exception is thrown, log it:
                plugin.getLogger().severe("Unable to send webhook message (is the webhook URL still valid?): ");
                e.printStackTrace();
            }
        }
    }
}
