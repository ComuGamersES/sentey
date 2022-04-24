package com.comugamers.sentey.core.listeners;

import com.comugamers.sentey.common.discord.DiscordWebhook;
import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.common.payload.PayloadType;
import com.comugamers.sentey.core.Sentey;
import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

import static com.comugamers.sentey.common.util.NetworkUtil.isValidIPv4;

public class PlayerLoginListener implements Listener {

    @Inject
    private YamlFile config;

    @Inject
    private Sentey plugin;

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        InetAddress spoofedAddress = event.getAddress();
        InetAddress handshakeAddress = event.getRealAddress();

        // Check if we should block null IP addresses
        if(config.getBoolean("config.login.block-null-addresses.enabled")) {
            // Check if any of the IP addresses is null
            if(handshakeAddress == null || spoofedAddress == null) {
                // If so, block the login
                disallow(event, PayloadType.NULL_ADDRESS);
                return;
            }
        }

        // Check if we should block local addresses
        if(config.getBoolean("config.login.block-local-addresses.enabled")) {
            // Check if we should check the handshake IP address
            if(config.getBoolean("config.login.block-local-addresses.check-handshake")) {
                // Check if the handshake IP address is local
                if(handshakeAddress.isAnyLocalAddress()) {
                    // Block the login
                    disallow(event, PayloadType.LOCAL_IP_ADDRESS);
                    return;
                }
            }

            if(config.getBoolean("config.login.block-local-addresses.check-spoofed-address")) {
                // Check if the spoofed IP address is local
                if(spoofedAddress.isAnyLocalAddress()) {
                    // Block the login
                    disallow(event, PayloadType.LOCAL_IP_ADDRESS);
                    return;
                }
            }
        }

        // Check if we should block site local addresses
        if(config.getBoolean("config.login.block-site-local-addresses.enabled")) {
            // Check if we should check the handshake IP address
            if(config.getBoolean("config.login.block-site-local-addresses.check-handshake")) {
                // Check if the handshake IP address is local
                if(handshakeAddress.isAnyLocalAddress()) {
                    // Block the login
                    disallow(event, PayloadType.SITE_LOCAL_IP_ADDRESS);
                    return;
                }
            }

            if(config.getBoolean("config.login.block-site-local-addresses.check-spoofed-address")) {
                // Check if the spoofed IP address is local
                if(spoofedAddress.isAnyLocalAddress()) {
                    // Block the login
                    disallow(event, PayloadType.SITE_LOCAL_IP_ADDRESS);
                    return;
                }
            }
        }

        // Check if we should block loopback addresses
        if(config.getBoolean("config.login.block-loopback-addresses.enabled")) {
            // Check if we should check the handshake IP address
            if(config.getBoolean("config.login.block-loopback-addresses.check-handshake")) {
                // Check if the handshake IP address is local
                if(handshakeAddress.isAnyLocalAddress()) {
                    // Block the login
                    disallow(event, PayloadType.LOOPBACK_IP_ADDRESS);
                    return;
                }
            }

            if(config.getBoolean("config.login.block-loopback-addresses.check-spoofed-address")) {
                // Check if the spoofed IP address is local
                if(spoofedAddress.isAnyLocalAddress()) {
                    // Block the login
                    disallow(event, PayloadType.LOOPBACK_IP_ADDRESS);
                    return;
                }
            }
        }

        // Check if we should check for invalid IP addresses - usually this is already done by
        // the null address check, but we do it again just in case
        if(config.getBoolean("config.login.block-invalid-addresses.enabled")) {
            // Check if any of the IP addresses are invalid
            if(!isValidIPv4(handshakeAddress.getHostAddress()) || !isValidIPv4(spoofedAddress.getHostAddress())) {
                // If so, block the login
                disallow(event, PayloadType.INVALID_IP_ADDRESS);
                return;
            }
        }

        // Check if the 'Unknown proxy' detection type is enabled
        if(config.getBoolean("config.login.unknown-proxies.enabled")) {
            // If so, check if the detection is on setup mode
            if(config.getBoolean("config.login.unknown-proxies.setup")) {
                // If true, return
                return;
            }

            // Get the list of allowed proxies
            List<String> allowedProxies = config.getStringList(
                    "config.login.unknown-proxies.allowed-proxies", false
            );

            // Check if the handshake IP address is an allowed proxy
            if(!allowedProxies.contains(handshakeAddress.getHostAddress())) {
                // If false, disallow the connection
                disallow(event, PayloadType.UNKNOWN_PROXY);
            }
        }
    }

    private void disallow(PlayerLoginEvent event, PayloadType type) {
        Player player = event.getPlayer();

        // Check if we should disallow the connection
        if(config.getBoolean("config.login.actions.disallow-connection.enabled")) {
            // If so, do it:
            event.disallow(
                    PlayerLoginEvent.Result.KICK_BANNED,
                    config.getString("config.login.actions.disallow-connection.reason")
            );
        }

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
                                            .replace("%proxyAddress%", event.getRealAddress().getHostAddress())
                                            .replace("%address%", type != PayloadType.NULL_ADDRESS
                                                    ? event.getAddress().getHostAddress()
                                                    : "null"
                                            ).replace("%detectionType%", type.name())
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
                                    .replace("%proxyAddress%", event.getRealAddress().getHostAddress())
                                    .replace("%address%", type != PayloadType.NULL_ADDRESS
                                            ? event.getAddress().getHostAddress()
                                            : "null"
                                    ).replace("%detectionType%", type.name())
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
                            .replace("%proxyAddress%", event.getRealAddress().getHostAddress())
                            .replace("%address%", type != PayloadType.NULL_ADDRESS
                                    ? event.getAddress().getHostAddress()
                                    : "null"
                            ).replace("%detectionType%", type.name())
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
