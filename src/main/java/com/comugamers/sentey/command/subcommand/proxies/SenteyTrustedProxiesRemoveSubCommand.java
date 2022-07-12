package com.comugamers.sentey.command.subcommand.proxies;

import com.comugamers.sentey.util.file.YamlFile;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

public class SenteyTrustedProxiesRemoveSubCommand {

    @Inject
    private YamlFile config;

    @Inject @Named("messages")
    private YamlFile messages;

    public void execute(CommandSender sender, String address) {
        // Check if enough arguments were provided
        if(address == null || address.isEmpty()) {
            // If not, send the usage message
            sender.sendMessage(
                    messages.getString("messages.command.trusted-proxies.remove.usage")
            );

            return;
        }

        // Get the list of trusted proxies
        List<String> trustedProxies = config.getStringList(
                "config.login.unknown-proxies.allowed-proxies"
        );

        // Check if the proxy is already trusted
        if (!trustedProxies.contains(address)) {
            // If not, send the 'Proxy not trusted' message
            sender.sendMessage(
                    messages.getString("messages.command.trusted-proxies.remove.not-trusted")
            );

            return;
        }

        // Remove the proxy from the trusted proxies list
        trustedProxies.remove(address);

        // Set the trusted proxies list
        config.set("config.login.unknown-proxies.allowed-proxies", trustedProxies);

        // Save the configuration file
        config.save();

        // Send the 'Proxy removed' message
        sender.sendMessage(
                messages.getString("messages.command.trusted-proxies.remove.success")
                        .replace("%proxy%", address)
        );
    }
}
