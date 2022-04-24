package com.comugamers.sentey.core.listeners;

import com.comugamers.sentey.common.file.YamlFile;
import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @Inject
    private YamlFile config;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Get the player that joined
        Player player = event.getPlayer();

        // Check if the unknown proxy detection is currently enabled on setup mode
        if(config.getBoolean("config.login.unknown-proxies.enabled")
                && config.getBoolean("config.login.unknown-proxies.setup")
        ) {
            // If true, check if the player has enough permissions to see
            // the 'Please configure the unknown proxy detection' message
            if(player.hasPermission("sentey.unknown-proxies.setup")) {
                // If true, send the message to the player
                player.sendMessage(config.getString("config.messages.setup-mode-enabled"));
            }
        }
    }
}
