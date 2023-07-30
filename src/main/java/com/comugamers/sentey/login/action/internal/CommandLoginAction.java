package com.comugamers.sentey.login.action.internal;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.util.PlaceholderUtil;
import com.comugamers.sentey.Sentey;

import javax.inject.Inject;

public class CommandLoginAction implements LoginAction {

    @Inject
    private Sentey plugin;

    @Inject
    private YamlFile config;

    @Override
    public void handle(LoginContext context, String detection) {
        if (config.getBoolean("config.login.actions.commands.enabled")) {
            config.getStringList("config.login.actions.commands.list").forEach(command ->
                    plugin.getServer().dispatchCommand(
                            plugin.getServer().getConsoleSender(),
                            PlaceholderUtil.applyPlaceholdersFromLoginContext(
                                    command, detection, context
                            )
                    )
            );
        }
    }
}
