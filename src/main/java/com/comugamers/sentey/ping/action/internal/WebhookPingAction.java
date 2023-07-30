package com.comugamers.sentey.ping.action.internal;

import com.comugamers.sentey.util.discord.DiscordWebhook;
import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.ping.action.PingAction;
import com.comugamers.sentey.util.PlaceholderUtil;

import javax.inject.Inject;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;

public class WebhookPingAction implements PingAction {

    @Inject
    private YamlFile config;

    @Inject
    private Sentey plugin;

    @Override
    public void handle(InetAddress address) {
        if (config.getBoolean("config.server-list-ping.actions.webhook.enabled")) {
            DiscordWebhook webhook = new DiscordWebhook(
                    config.getString("config.server-list-ping.actions.webhook.url")
            );

            try {
                webhook.setTTS(
                        config.getBoolean("config.server-list-ping.actions.webhook.tts", false)
                ).setContent(
                        PlaceholderUtil.applyPlaceholdersFromPingAddress(
                                config.getString("config.server-list-ping.actions.webhook.message"), address
                        )
                ).execute();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE,
                        "Unable to send webhook message (is the webhook URL still valid?):", e);
            }
        }
    }
}
