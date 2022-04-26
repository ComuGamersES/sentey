package com.comugamers.sentey.core.listeners;

import com.comugamers.sentey.core.login.modifier.LoginModifier;
import com.comugamers.sentey.core.login.action.LoginAction;
import com.comugamers.sentey.core.login.context.LoginContext;
import com.comugamers.sentey.core.Sentey;
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

        // Loop through each login modifier
        for(LoginModifier module : plugin.getLoginModifiers()) {
            // Handle the login attempt
            boolean result = module.handle(ctx);

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
