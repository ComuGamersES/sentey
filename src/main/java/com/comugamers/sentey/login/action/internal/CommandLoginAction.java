package com.comugamers.sentey.login.action.internal;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.util.PlaceholderUtil;
import com.comugamers.sentey.Sentey;
import com.google.inject.Inject;

public class CommandLoginAction implements LoginAction {

    @Inject
    private Sentey plugin;

    @Inject
    private YamlFile config;

    @Override
    public void handle(LoginContext context, String detection) {
        // Check if we should run commands
        if(config.getBoolean("config.login.actions.commands.enabled")) {
            // If so, loop through the list of commands
            config.getStringList("config.login.actions.commands.list").forEach(
                    // And dispatch each one of them
                    command ->
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
