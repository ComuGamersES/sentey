package com.comugamers.sentey.command.subcommand;

import com.comugamers.sentey.command.SenteyCommand;
import com.comugamers.sentey.util.file.YamlFile;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;

public class SenteyHelpSubCommand extends SenteyCommand {

    @Inject
    @Named("messages")
    private YamlFile messages;

    @SubCommand(value = "help", alias = { "h" })
    @Permission("sentey.admin")
    public void execute(CommandSender sender) {
        sender.sendMessage(messages.getString("messages.command.help"));
    }
}
