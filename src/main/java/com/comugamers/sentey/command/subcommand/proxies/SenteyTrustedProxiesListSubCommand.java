package com.comugamers.sentey.command.subcommand.proxies;

import com.comugamers.sentey.util.file.YamlFile;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

public class SenteyTrustedProxiesListSubCommand {

    @Inject
    private YamlFile config;

    @Inject
    @Named("messages")
    private YamlFile messages;

    public void execute(CommandSender sender) {
        List<String> trustedProxies = config.getStringList(
                "config.login.unknown-proxies.allowed-proxies"
        );

        if (trustedProxies.isEmpty()) {
            sender.sendMessage(
                    messages.getString("messages.command.trusted-proxies.list.none")
            );
        } else {
            String delimiter = messages.getString("messages.command.trusted-proxies.list.delimiter");
            sender.sendMessage(
                    messages.getString("messages.command.trusted-proxies.list.message")
                            .replace("%proxies%", String.join(delimiter, trustedProxies))
            );
        }
    }
}
