package com.comugamers.sentey.command.subcommand;

import com.comugamers.sentey.command.SenteyCommand;
import com.comugamers.sentey.util.ConnectionUtil;
import com.comugamers.sentey.util.file.YamlFile;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Named;

public class SenteyAddressSubCommand extends SenteyCommand {

    @Inject @Named("messages")
    private YamlFile messages;

    // TODO: should I make target an optional argument so we can send the usage message?

    @SubCommand(value = "address", alias = { "socket-address", "real-address", "ip" })
    @Permission("sentey.admin")
    public void execute(CommandSender sender, Player target) {
        // Send the address information message
        sender.sendMessage(
                messages.getString("messages.command.address.information")
                        .replace("%player%", target.getName())
                        .replace("%rawAddress%", ConnectionUtil.getRemoteAddress(target))
                        .replace("%address%",
                                target.getAddress() != null
                                        ? target.getAddress().getAddress().getHostAddress()
                                        : "null"
                        )
        );
    }
}
