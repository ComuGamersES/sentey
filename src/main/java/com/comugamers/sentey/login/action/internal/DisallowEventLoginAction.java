package com.comugamers.sentey.login.action.internal;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.util.PlaceholderUtil;
import org.bukkit.event.player.PlayerLoginEvent;

import javax.inject.Inject;

public class DisallowEventLoginAction implements LoginAction {

    @Inject
    private YamlFile config;

    @Override
    public void handle(LoginContext context, String detection) {
        if (config.getBoolean("config.login.actions.disallow-connection.enabled")) {
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
