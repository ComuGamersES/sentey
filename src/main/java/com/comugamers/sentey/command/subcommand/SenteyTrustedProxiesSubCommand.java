package com.comugamers.sentey.command.subcommand;

import com.comugamers.sentey.command.SenteyCommand;
import com.comugamers.sentey.command.subcommand.proxies.SenteyTrustedProxiesAddSubCommand;
import com.comugamers.sentey.command.subcommand.proxies.SenteyTrustedProxiesListSubCommand;
import com.comugamers.sentey.command.subcommand.proxies.SenteyTrustedProxiesRemoveSubCommand;
import com.comugamers.sentey.command.subcommand.proxies.SenteyTrustedProxiesSetupSubCommand;
import dev.triumphteam.cmd.core.annotation.Optional;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

public class SenteyTrustedProxiesSubCommand extends SenteyCommand {

    @Inject
    private SenteyTrustedProxiesSetupSubCommand setupSubCommand;

    @Inject
    private SenteyTrustedProxiesAddSubCommand addSubCommand;

    @Inject
    private SenteyTrustedProxiesRemoveSubCommand removeSubCommand;

    @Inject
    private SenteyTrustedProxiesListSubCommand listSubCommand;

    @Inject
    private SenteyHelpSubCommand helpSubCommand;

    @SubCommand(value = "trusted-proxies", alias = { "allowed-proxies", "allowed-ips", "trusted", "proxies" })
    public void execute(CommandSender sender, @Suggestion("trusted-proxies") String action, @Optional String address) {
        switch(action.toLowerCase()) {
            case "add": {
                addSubCommand.execute(sender, address);
                return;
            }
            case "rm":
            case "delete":
            case "remove": {
                removeSubCommand.execute(sender, address);
                return;
            }
            case "toggle-setup":
            case "setup": {
                setupSubCommand.execute(sender);
                return;
            }
            case "list": {
                listSubCommand.execute(sender);
                return;
            }
            default: {
                helpSubCommand.execute(sender);
            }
        }
    }
}
