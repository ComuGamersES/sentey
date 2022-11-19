package com.comugamers.sentey.command;

import com.comugamers.sentey.integrations.abuseipdb.AbuseIPDB;
import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.Sentey;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;

import static com.comugamers.sentey.util.TextUtil.colorize;

@Command(value = "sentey", alias = { "comusentey", "csentey" })
public class SenteyCommand extends BaseCommand {

    @Inject
    private Sentey plugin;

    @Inject
    private YamlFile config;

    @Inject
    private AbuseIPDB abuseIPDB;

    @Inject @Named("messages")
    private YamlFile messages;

    @Default
    public void execute(CommandSender sender) {
        // Send the 'Running Sentey version x.x.x' message
        sender.sendMessage(
                colorize(
                        "&b&lSENTEY >> &fRunning &aSentey &fversion &a" + plugin.getDescription().getVersion() + "&f."
                )
        );

        // And send the list of authors as well
        sender.sendMessage(
                colorize(
                        "&b&lSENTEY >> &fAuthors: &a"
                                + String.join("&f, &a", plugin.getDescription().getAuthors())
                )
        );

        // Check if the sender has the 'sentey.admin' permission
        if (sender.hasPermission("sentey.admin")) {
            // If so, send the 'Run /sentey help' message
            sender.sendMessage(
                    colorize("&b&lSENTEY >> &fRun &a/sentey help&f for help.")
            );
        }
    }
}
