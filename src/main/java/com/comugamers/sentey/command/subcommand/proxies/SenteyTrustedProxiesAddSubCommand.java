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
        if(address == null || address.isEmpty()) {
            sender.sendMessage(messages.getString("messages.command.trusted-proxies.add.usage"));
            return;
        }

        if (address.equals("docker")) {
            address = "172.17.0.1";
        }

        if (address.equals("pterodactyl")) {
            address = "172.18.0.1";
        }

        String path = "config.login.unknown-proxies.allowed-proxies";
        List<String> trustedProxies = config.getStringList(path);

        if (trustedProxies.contains(address)) {
            sender.sendMessage(messages.getString("messages.command.trusted-proxies.add.already-trusted"));
            return;
        }

        if (InetAddresses.isInetAddress(address)) {
            trustedProxies.add(address);

            config.set(path, trustedProxies);
            config.save();

            sender.sendMessage(
                    messages.getString("messages.command.trusted-proxies.add.success")
                            .replace("%proxy%", address)
            );
        } else {
            try {
                InetAddress domainAddress = InetAddress.getByName(address);
                trustedProxies.add(domainAddress.getHostAddress());

                sender.sendMessage(
                        messages.getString("messages.command.trusted-proxies.add.success-resolved")
                                .replace("%raw%", address)
                                .replace("%resolved%", domainAddress.getHostAddress())
                );

                config.set(path, trustedProxies);
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
