package com.comugamers.sentey.core.listeners;

import com.comugamers.sentey.common.discord.DiscordWebhook;
import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.common.login.LoginModifier;
import com.comugamers.sentey.common.login.context.LoginContext;
import com.comugamers.sentey.core.Sentey;
import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.IOException;
import java.util.Set;

public class PlayerLoginListener implements Listener {

    @Inject
    private YamlFile config;

    @Inject
    private Sentey plugin;

    @Inject
    private Set<LoginModifier> loginModifiers;

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        // Create a new login context
        LoginContext ctx = new LoginContext(
                event.getPlayer(),
                event.getAddress(),
                event.getRealAddress()
        );

        // Loop through each login modifier
        for(LoginModifier module : loginModifiers) {
            // Handle the login attempt
            boolean result = module.handle(ctx);

            // If the login attempt was denied, cancel the login event
            if(!result) {
                disallow(ctx, module.getName());
                break;
            }
        }
    }

    // TODO: make this more flexible as well
    private void disallow(LoginContext ctx, String detection) {
        Player player = ctx.getPlayer();

        // Check if we should run commands
        if(config.getBoolean("config.login.actions.commands.enabled")) {
            // If so, loop through the list of commands
            config.getStringList("config.login.actions.commands.list").forEach(
                    // And dispatch each one of them
                    command ->
                            plugin.getServer().dispatchCommand(
                                    plugin.getServer().getConsoleSender(), command
                                            .replace("%player%", player.getName())
                                            .replace("%uuid%", player.getUniqueId().toString())
                                            .replace("%proxyAddress%", ctx.getHandshakeAddress().getHostAddress())
                                            .replace("%address%", ctx.isValidSpoofedAddress()
                                                    ? ctx.getSpoofedAddress().getHostAddress()
                                                    : "null"
                                            ).replace("%detectionType%", detection)
                            )
            );
        }

        // Check if we should send a message to online staff
        if(config.getBoolean("config.login.actions.alerts.enabled")) {
            // If so, send the message
            plugin.getServer().getOnlinePlayers().forEach(target -> {
                if(target.hasPermission("sentey.alerts")) {
                    target.sendMessage(
                            config.getString("config.login.actions.alerts.message")
                                    .replace("%player%", player.getName())
                                    .replace("%uuid%", player.getUniqueId().toString())
                                    .replace("%proxyAddress%", ctx.getHandshakeAddress().getHostAddress())
                                    .replace("%address%", ctx.isValidSpoofedAddress()
                                            ? ctx.getSpoofedAddress().getHostAddress()
                                            : "null"
                                    ).replace("%detectionType%", detection)
                    );
                }
            });
        }

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
                    config.getString("config.login.actions.webhook.message")
                            .replace("%player%", player.getName())
                            .replace("%uuid%", player.getUniqueId().toString())
                            .replace("%proxyAddress%", ctx.getHandshakeAddress().getHostAddress())
                            .replace("%address%", ctx.isValidSpoofedAddress()
                                    ? ctx.getSpoofedAddress().getHostAddress()
                                    : "null"
                            ).replace("%detectionType%", detection)
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
