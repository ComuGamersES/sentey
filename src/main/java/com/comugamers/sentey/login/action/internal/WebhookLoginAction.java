package com.comugamers.sentey.login.action.internal;

import com.comugamers.sentey.util.discord.DiscordWebhook;
import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.util.PlaceholderUtil;
import com.comugamers.sentey.Sentey;
import com.google.inject.Inject;

import java.io.IOException;

public class WebhookLoginAction implements LoginAction {

    @Inject
    private YamlFile config;

    @Inject
    private Sentey plugin;

    @Override
    public void handle(LoginContext context, String detection) {
        // Check if we should send a message to a webhook
        if(config.getBoolean("config.login.actions.webhook.enabled")) {
            // If so, create a new webhook
            DiscordWebhook webhook = new DiscordWebhook(config.getString("config.login.actions.webhook.url"));

            try {
                // Update the TTS option, the content of the message and then send it
                webhook.setTTS(
                        config.getBoolean("config.login.actions.webhook.tts", false)
                ).setContent(
                        PlaceholderUtil.applyPlaceholdersFromLoginContext(
                                config.getString("config.login.actions.webhook.message"),
                                detection,
                                context
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
