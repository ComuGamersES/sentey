package com.comugamers.sentey.core.login.action.internal;

import com.comugamers.sentey.common.discord.DiscordWebhook;
import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.core.login.action.LoginAction;
import com.comugamers.sentey.core.login.context.LoginContext;
import com.comugamers.sentey.core.util.PlaceholderUtil;
import com.comugamers.sentey.core.Sentey;
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

            // Set the TTS option
            webhook.setTTS(
                    config.getBoolean("config.login.actions.webhook.tts", false)
            );

            // Set the message
            webhook.setContent(
                    PlaceholderUtil.applyPlaceholdersFromContext(
                            config.getString("config.login.actions.webhook.message"),
                            detection,
                            context
                    )
            );

            // And send it in a try/catch block
            try {
                webhook.execute();
            } catch (IOException e) {
                plugin.getLogger().severe("Unable to send webhook message (is the webhook URL still valid?): ");
                e.printStackTrace();
            }
        }
    }
}
