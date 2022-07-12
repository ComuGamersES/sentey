package com.comugamers.sentey.listeners.player;

import com.comugamers.sentey.login.filter.LoginFilter;
import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.util.file.YamlFile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import javax.inject.Inject;

import static com.comugamers.sentey.util.ConnectionUtil.getRemoteAddress;

public class PlayerLoginListener implements Listener {

    @Inject
    private Sentey plugin;

    @Inject
    private YamlFile config;

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        // Create a new login context
        LoginContext ctx = new LoginContext(event);

        // Get the player who logged in
        Player player = ctx.getPlayer();

        // Check if we should log socket addresses
        if(config.getBoolean("config.log-socket-addresses", false)) {
            // If so, do it
            plugin.getLogger().info(
                    "Player " + player.getName() + " is joining through " + getRemoteAddress(player)
            );
        }

        // Loop through each login filter
        for (LoginFilter module : plugin.getLoginFilters()) {
            // If the login attempt was denied, run login actions
            if (!module.isClean(ctx)) {
                // Run user desired actions
                for (LoginAction action : plugin.getLoginActions()) {
                    action.handle(ctx, module.getName());
                }

                // And break the loop
                break;
            }
        }
    }
}
