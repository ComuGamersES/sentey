package com.comugamers.sentey.login.action.internal;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.util.PlaceholderUtil;
import com.google.inject.Inject;
import org.bukkit.event.player.PlayerLoginEvent;

public class DisallowEventLoginAction implements LoginAction {

    @Inject
    private YamlFile config;

    @Override
    public void handle(LoginContext context, String detection) {
        // Check if we should disallow the connection attempt
        if(config.getBoolean("config.login.actions.disallow-connection.enabled")) {
            // If so, do it I guess
            context.getRawLoginEvent().disallow(
                    PlayerLoginEvent.Result.KICK_OTHER,
                    PlaceholderUtil.applyPlaceholdersFromLoginContext(
                            config.getString("config.login.actions.disallow-connection.reason"),
                            detection,
                            context
                    )
            );
        }
    }
}
