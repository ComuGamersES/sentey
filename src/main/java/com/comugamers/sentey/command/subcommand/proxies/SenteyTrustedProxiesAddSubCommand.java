package com.comugamers.sentey.command.subcommand.proxies;

import com.comugamers.sentey.util.file.YamlFile;
import com.google.common.net.InetAddresses;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

public class SenteyTrustedProxiesAddSubCommand {

    @Inject
    private YamlFile config;

    @Inject @Named("messages")
    private YamlFile messages;

    public void execute(CommandSender sender, String address) {
        // Check if enough arguments were provided
        if(address == null || address.isEmpty()) {
            // If not, send the usage message
            sender.sendMessage(messages.getString("messages.command.trusted-proxies.add.usage"));
            return;
        }

        // Get the list of trusted proxies
        List<String> trustedProxies = config.getStringList(
                "config.login.unknown-proxies.allowed-proxies"
        );

        // Check if the proxy address is valid
        if (!InetAddresses.isInetAddress(address)) {
            // If not, send the 'Invalid IP address' message
            sender.sendMessage(
                    messages.getString("messages.command.trusted-proxies.add.invalid-ipv4")
                            .replace("%proxy%", address)
            );

            return;
        }

        // Check if the proxy is already trusted
        if (trustedProxies.contains(address)) {
            // If so, send the 'Proxy already trusted' message and return
            sender.sendMessage(messages.getString("messages.command.trusted-proxies.add.already-trusted"));
            return;
        }

        // Add the proxy to the trusted proxies list
        trustedProxies.add(address);

        // Set the trusted proxies list
        config.set("config.login.unknown-proxies.allowed-proxies", trustedProxies);

        // Save the configuration file
        config.save();

        // Send the 'Proxy added' message
        sender.sendMessage(
                messages.getString("messages.command.trusted-proxies.add.success")
                        .replace("%proxy%", address)
        );
    }
}
