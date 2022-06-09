package com.comugamers.sentey.login.action.internal;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.util.PlaceholderUtil;
import com.comugamers.sentey.Sentey;
import com.google.inject.Inject;

public class AlertLoginAction implements LoginAction {

    @Inject
    private YamlFile config;

    @Inject
    private Sentey plugin;

    @Override
    public void handle(LoginContext context, String detection) {
        // Check if we should send a message to online staff
        if(config.getBoolean("config.login.actions.alerts.enabled")) {
            // If so, send the message
            plugin.getServer().getOnlinePlayers().forEach(target -> {
                if(target.hasPermission("sentey.alerts")) {
                    target.sendMessage(
                            PlaceholderUtil.applyPlaceholdersFromLoginContext(
                                    config.getString("config.login.actions.alerts.message"),
                                    detection,
                                    context
                            )
                    );
                }
            });
        }
    }
}
