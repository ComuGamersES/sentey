package com.comugamers.sentey.command.subcommand;

import com.comugamers.sentey.command.SenteyCommand;
import com.comugamers.sentey.util.file.YamlFile;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SenteyResolveSubCommand extends SenteyCommand {

    @Inject @Named("messages")
    private YamlFile messages;

    @SubCommand(value = "resolve", alias = { "resolve-domain", "resolvedomain" })
    public void resolve(CommandSender sender, String host) {
        try {
            InetAddress address = InetAddress.getByName(host);
            sender.sendMessage(
                    messages.getString("messages.command.resolve.success")
                            .replace("%host%", host)
                            .replace("%address%", address.getHostAddress())
            );
        } catch (UnknownHostException ex) {
            sender.sendMessage(
                    messages.getString("messages.command.resolve.unknown-host")
                            .replace("%host%", host)
            );
        }
    }
}
