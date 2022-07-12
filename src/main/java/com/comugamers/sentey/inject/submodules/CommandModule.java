package com.comugamers.sentey.inject.submodules;

import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.command.SenteyCommand;
import com.comugamers.sentey.command.subcommand.SenteyAddressSubCommand;
import com.comugamers.sentey.command.subcommand.SenteyHelpSubCommand;
import com.comugamers.sentey.command.subcommand.SenteyReloadSubCommand;
import com.comugamers.sentey.command.subcommand.SenteyTrustedProxiesSubCommand;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.BaseCommand;
import org.bukkit.command.CommandSender;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;

import javax.inject.Singleton;

public class CommandModule extends AbstractModule {

    @Override
    protected void configure() {
        this.multibind(BaseCommand.class)
                .asSet()
                .to(SenteyCommand.class)
                .to(SenteyReloadSubCommand.class)
                .to(SenteyAddressSubCommand.class)
                .to(SenteyHelpSubCommand.class)
                .to(SenteyTrustedProxiesSubCommand.class)
                .singleton();
    }

    @Singleton
    @Provides
    public BukkitCommandManager<CommandSender> provideBukkitCommandManager(Sentey plugin) {
        return BukkitCommandManager.create(plugin);
    }
}
