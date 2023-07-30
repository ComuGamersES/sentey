package com.comugamers.sentey.command.subcommand.proxies;

import com.comugamers.sentey.util.file.YamlFile;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class SenteyTrustedProxiesRemoveSubCommand {

    @Inject
    private YamlFile config;

    @Inject
    @Named("messages")
    private YamlFile messages;

    public void execute(CommandSender sender, String address) {
        if (address == null || address.isEmpty()) {
            sender.sendMessage(
                    messages.getString("messages.command.trusted-proxies.remove.usage")
            );

            return;
        }

        List<String> trustedProxies = config.getStringList(
                "config.login.unknown-proxies.allowed-proxies"
        );

        if (!trustedProxies.contains(address)) {
            try {
                InetAddress domainAddress = InetAddress.getByName(address);

                if (!trustedProxies.contains(domainAddress.getHostAddress())) {
                    sender.sendMessage(
                            messages.getString("messages.command.trusted-proxies.remove.not-trusted")
                    );
                } else {
                    trustedProxies.remove(domainAddress.getHostAddress());

                    config.set("config.login.unknown-proxies.allowed-proxies", trustedProxies);
                    config.save();

                    sender.sendMessage(
                            messages.getString("messages.command.trusted-proxies.remove.success-resolved")
                                    .replace("%raw%", address)
                                    .replace("%resolved%", domainAddress.getHostAddress())
                    );
                }
            } catch (UnknownHostException ex) {
                sender.sendMessage(
                        messages.getString("messages.command.resolve.unknown-host")
                );
            }

            return;
        }

        trustedProxies.remove(address);

        config.set("config.login.unknown-proxies.allowed-proxies", trustedProxies);
        config.save();

        sender.sendMessage(
                messages.getString("messages.command.trusted-proxies.remove.success")
                        .replace("%proxy%", address)
        );
    }
}
