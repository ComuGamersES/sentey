package com.comugamers.sentey.command;

import com.comugamers.sentey.integrations.abuseipdb.AbuseIPDB;
import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.Sentey;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Description;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;

import static com.comugamers.sentey.util.TextUtil.colorize;

@Command(value = "sentey", alias = { "comusentey", "csentey" })
@Description("Main command of the plugin.")
public class SenteyCommand extends BaseCommand {

    @Inject
    private Sentey plugin;

    @Default
    public void execute(CommandSender sender) {
        sender.sendMessage(
                colorize(
                        "&b&lSENTEY >> &fRunning &aSentey &fversion &a" + plugin.getDescription().getVersion() + "&f."
                )
        );

        sender.sendMessage(
                colorize(
                        "&b&lSENTEY >> &fAuthors: &a"
                                + String.join("&f, &a", plugin.getDescription().getAuthors())
                )
        );

        if (sender.hasPermission("sentey.admin")) {
            sender.sendMessage(
                    colorize("&b&lSENTEY >> &fRun &a/sentey help&f for help.")
            );
        }
    }
}
