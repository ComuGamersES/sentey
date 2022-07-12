package com.comugamers.sentey.command.subcommand.proxies;

import com.comugamers.sentey.util.file.YamlFile;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;

public class SenteyTrustedProxiesSetupSubCommand {

    @Inject
    private YamlFile config;

    @Inject @Named("messages")
    private YamlFile messages;

    public void execute(CommandSender sender) {
        // Get the current setup state
        boolean setup = config.getBoolean("config.login.unknown-proxies.setup");

        // Set to the new one
        config.set("config.login.unknown-proxies.setup", !setup);

        // Save the configuration file
        config.save();

        // Send the 'Setup enabled/disabled' message
        sender.sendMessage(
                messages.getString(
                        "messages.command.trusted-proxies.toggle-setup."
                                + (setup ? "disabled" : "enabled")
                )
        );
    }
}
