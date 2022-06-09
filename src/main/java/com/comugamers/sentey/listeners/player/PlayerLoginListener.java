package com.comugamers.sentey.listeners.player;

import com.comugamers.sentey.login.filter.LoginFilter;
import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.Sentey;
import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    @Inject
    private Sentey plugin;

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        // Create a new login context
        LoginContext ctx = new LoginContext(event);

        // Loop through each login filter
        for(LoginFilter module : plugin.getLoginFilters()) {
            // Handle the login attempt
            boolean result = module.isClean(ctx);

            // If the login attempt was denied, cancel the login event
            if(!result) {
                // Run user desired actions
                for(LoginAction action : plugin.getLoginActions()) {
                    action.handle(ctx, module.getName());
                }

                // And break the loop
                break;
            }
        }
    }
}
