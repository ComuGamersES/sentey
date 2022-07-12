package com.comugamers.sentey.command.subcommand.proxies;

import com.comugamers.sentey.util.file.YamlFile;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

public class SenteyTrustedProxiesListSubCommand {

    @Inject
    private YamlFile config;

    @Inject @Named("messages")
    private YamlFile messages;

    public void execute(CommandSender sender) {
        // Get the list of trusted proxies
        List<String> trustedProxies = config.getStringList(
                "config.login.unknown-proxies.allowed-proxies"
        );

        // Check if it is empty
        if(trustedProxies.isEmpty()) {
            // If so, send the 'There are no trusted proxies' message
            sender.sendMessage(
                    messages.getString("messages.command.trusted-proxies.list.none")
            );

            return;
        }

        // Get the trusted proxy list delimiter
        String delimiter = messages.getString("messages.command.trusted-proxies.list.delimiter");

        // Send the list of trusted proxies
        sender.sendMessage(
                messages.getString("messages.command.trusted-proxies.list.message")
                        .replace("%proxies%", String.join(delimiter, trustedProxies))
        );
    }
}
