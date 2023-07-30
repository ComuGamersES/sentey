package com.comugamers.sentey.command.subcommand.proxies;

import com.comugamers.sentey.util.file.YamlFile;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;

public class SenteyTrustedProxiesSetupSubCommand {

    @Inject
    private YamlFile config;

    @Inject
    @Named("messages")
    private YamlFile messages;

    public void execute(CommandSender sender) {
        boolean setup = config.getBoolean("config.login.unknown-proxies.setup");

        config.set("config.login.unknown-proxies.setup", !setup);
        config.save();

        sender.sendMessage(
                messages.getString(
                        "messages.command.trusted-proxies.toggle-setup."
                                + (setup ? "disabled" : "enabled")
                )
        );
    }
}
