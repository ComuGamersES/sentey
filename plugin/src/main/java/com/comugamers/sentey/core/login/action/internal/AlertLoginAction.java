package com.comugamers.sentey.core.login.action.internal;

import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.core.login.action.LoginAction;
import com.comugamers.sentey.core.login.context.LoginContext;
import com.comugamers.sentey.core.util.PlaceholderUtil;
import com.comugamers.sentey.core.Sentey;
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
                            PlaceholderUtil.applyPlaceholdersFromContext(
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
