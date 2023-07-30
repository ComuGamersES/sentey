package com.comugamers.sentey.login.action.internal;

import com.comugamers.sentey.util.discord.DiscordWebhook;
import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.util.PlaceholderUtil;
import com.comugamers.sentey.Sentey;

import javax.inject.Inject;
import java.io.IOException;
import java.util.logging.Level;

public class WebhookLoginAction implements LoginAction {

    @Inject
    private YamlFile config;

    @Inject
    private Sentey plugin;

    @Override
    public void handle(LoginContext context, String detection) {
        if (config.getBoolean("config.login.actions.webhook.enabled")) {
            DiscordWebhook webhook = new DiscordWebhook(config.getString("config.login.actions.webhook.url"));

            try {
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
                plugin.getLogger().log(Level.SEVERE,
                        "Unable to send webhook message (is the webhook URL still valid?): ", e);
            }
        }
    }
}
