package com.comugamers.sentey.command.subcommand;

import com.comugamers.sentey.command.SenteyCommand;
import com.comugamers.sentey.integrations.abuseipdb.AbuseIPDB;
import com.comugamers.sentey.util.file.YamlFile;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;

public class SenteyReloadSubCommand extends SenteyCommand {

    @Inject
    private AbuseIPDB abuseIPDB;

    @Inject
    private YamlFile config;

    @Inject @Named("messages")
    private YamlFile messages;

    @SubCommand(value = "reload", alias = { "rl" })
    @Permission("sentey.admin")
    public void execute(CommandSender sender) {
        config.reload();
        messages.reload();

        abuseIPDB.updateKey(
                config.getString("config.integrations.abuseipdb.key")
        );

        sender.sendMessage(messages.getString("messages.command.config-reloaded"));
    }
}
