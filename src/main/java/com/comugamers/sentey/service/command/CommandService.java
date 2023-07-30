package com.comugamers.sentey.service.command;

import com.comugamers.sentey.service.Service;
import com.comugamers.sentey.util.file.YamlFile;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.message.MessageKey;
import dev.triumphteam.cmd.core.message.context.MessageContext;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

public class CommandService implements Service {

    @Inject
    private Set<BaseCommand> commands;

    @Inject
    private BukkitCommandManager<CommandSender> commandManager;

    @Inject @Named("messages")
    private YamlFile messages;

    @Override
    public void start() {
        this.configureMessage(BukkitMessageKey.NO_PERMISSION, "messages.command.no-permission");
        this.configureMessage(BukkitMessageKey.CONSOLE_ONLY, "messages.command.console-only");
        this.configureMessage(BukkitMessageKey.PLAYER_ONLY, "messages.command.player-only");
        this.configureMessage(MessageKey.UNKNOWN_COMMAND, "messages.command.unknown-command");
        this.configureMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, "messages.command.not-enough-arguments");
        this.configureMessage(MessageKey.TOO_MANY_ARGUMENTS, "messages.command.too-many-arguments");
        this.configureMessage(MessageKey.INVALID_ARGUMENT, "messages.command.invalid-argument");

        commandManager.registerSuggestion(
                SuggestionKey.of("trusted-proxies"),
                (sender, ctx) -> Arrays.asList("list", "add", "remove", "setup")
        );

        commandManager.registerSuggestion(
                SuggestionKey.of("domains"),
                (sender, ctx) -> Arrays.asList(
                        "0x7f000001",
                        "comugamers.com",
                        "spigotmc.org",
                        "google.com",
                        "cloudflare.com",
                        "twitter.com"
                )
        );

        commands.forEach(commandManager::registerCommand);
    }

    @Override
    public void stop() {
        commands.forEach(commandManager::unregisterCommand);
    }

    private void configureMessage(BukkitMessageKey<? extends MessageContext> key, String path) {
        commandManager.registerMessage(key, (sender, context) ->
            sender.sendMessage(messages.getString(path))
        );
    }

    private void configureMessage(MessageKey<? extends MessageContext> key, String path) {
        commandManager.registerMessage(key, (sender, context) ->
            sender.sendMessage(messages.getString(path))
        );
    }
}
