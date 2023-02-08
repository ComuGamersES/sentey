package com.comugamers.sentey.command.subcommand.proxies;

import com.comugamers.sentey.util.file.YamlFile;
import com.google.common.net.InetAddresses;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

        // Rewrite 'docker' to default Docker network
        if (address.equals("docker")) {
            address = "172.17.0.1";
        }

        // Rewrite 'pterodactyl' to default pterodactyl Docker network
        if (address.equals("pterodactyl")) {
            address = "172.18.0.1";
        }

        // Set the allowed proxies path
        String path = "config.login.unknown-proxies.allowed-proxies";

        // Get the list of trusted proxies
        List<String> trustedProxies = config.getStringList(path);

        // Check if the proxy is already trusted
        if (trustedProxies.contains(address)) {
            // If so, send the 'Proxy already trusted' message and return
            sender.sendMessage(messages.getString("messages.command.trusted-proxies.add.already-trusted"));
            return;
        }

        // Check if the proxy address is an IPv4/IPv6 address
        if (InetAddresses.isInetAddress(address)) {
            // Add the proxy to the trusted proxies list
            trustedProxies.add(address);

            // Set the trusted proxies list
            config.set(path, trustedProxies);

            // Save the configuration file
            config.save();

            // Send the 'Proxy added' message
            sender.sendMessage(
                    messages.getString("messages.command.trusted-proxies.add.success")
                            .replace("%proxy%", address)
            );
        } else {
            try {
                // If not, check if the address is a hostname
                InetAddress domainAddress = InetAddress.getByName(address);

                // If so, add the proxy to the trusted proxies list
                trustedProxies.add(domainAddress.getHostAddress());

                // Send the 'Proxy added' message
                sender.sendMessage(
                        messages.getString("messages.command.trusted-proxies.add.success-resolved")
                                .replace("%raw%", address)
                                .replace("%resolved%", domainAddress.getHostAddress())
                );

                // Set the trusted proxies list
                config.set(path, trustedProxies);

                // Save the configuration file
                config.save();
            } catch (UnknownHostException ex) {
                sender.sendMessage(
                        messages.getString("messages.command.resolve.unknown-host")
                                .replace("%hostname%", address)
                );
            }
        }
    }
}
