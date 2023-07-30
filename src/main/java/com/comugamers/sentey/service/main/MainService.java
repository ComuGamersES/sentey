package com.comugamers.sentey.service.main;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.service.Service;
import org.bukkit.command.CommandSender;
import org.spigotmc.SpigotConfig;

import javax.inject.Inject;
import javax.inject.Named;

import static com.comugamers.sentey.util.TextUtil.colorize;
import static com.comugamers.sentey.util.VersionUtil.isSpigot;

public class MainService implements Service {

    @Inject
    private Sentey plugin;

    @Inject
    private YamlFile config;

    @Inject
    @Named("listener")
    private Service listenerService;

    @Inject
    @Named("command")
    private Service commandService;

    @Inject
    @Named("login")
    private Service loginService;

    @Inject
    @Named("ping")
    private Service pingService;

    @Inject
    @Named("updateChecker")
    private Service updateCheckerService;

    @Inject
    @Named("metrics")
    private Service metricsService;

    @Override
    public void start() {
        CommandSender consoleCommandSender = plugin.getServer().getConsoleSender();

        consoleCommandSender.sendMessage(colorize("          &fSentey - Version &a" + plugin.getDescription().getVersion()));
        consoleCommandSender.sendMessage(colorize("&a------------------------------------------"));
        consoleCommandSender.sendMessage(colorize("&a-> &fMade by: &a" + plugin.getDescription().getAuthors().toString()));
        consoleCommandSender.sendMessage(colorize(""));
        consoleCommandSender.sendMessage(colorize("&a-> &fServer software: &a" + plugin.getServer().getVersion()));
        consoleCommandSender.sendMessage(colorize(""));

        if (!config.getBoolean("config.ignore-spigot-check")) {
            if (!isSpigot()) {
                plugin.getServer()
                        .getConsoleSender()
                        .sendMessage(
                                colorize(
                                        "&4-> &fYour current server software is unsupported. Please use Spigot " +
                                                "or a fork of it."
                                )
                        );

                cancelStartup();
                return;
            }
        } else {
            plugin.getServer()
                    .getConsoleSender()
                    .sendMessage(colorize("&e-> &fIgnoring server software check!"));
        }

        if (!config.getBoolean("config.ignore-bungeecord-check")) {
            if (isSpigot() && !SpigotConfig.bungee) {
                plugin.getServer()
                        .getConsoleSender()
                        .sendMessage(
                                colorize(
                                        "&4-> &fYour current server has the 'bungeecord' option disabled. " +
                                                "Enable it on 'spigot.yml' file to use Sentey."
                                )
                        );

                cancelStartup();
                return;
            }
        } else {
            plugin.getServer()
                    .getConsoleSender()
                    .sendMessage(colorize("&e-> &fIgnoring BungeeCord boolean check!"));
        }

        startServices(
                loginService,
                pingService,
                listenerService,
                commandService,
                updateCheckerService
        );

        consoleCommandSender.sendMessage(colorize("&a-> &fStartup process finished!"));
        consoleCommandSender.sendMessage(colorize("&a------------------------------------------"));
    }

    @Override
    public void stop() {
        plugin.getLogger().info("Sentey has been disabled. See you next time.");
    }

    private void startServices(Service... services) {
        for (Service service : services) {
            service.start();
        }
    }

    private void stopServices(Service... services) {
        for (Service service : services) {
            service.stop();
        }
    }

    private void cancelStartup() {
        plugin.getServer()
                .getConsoleSender()
                .sendMessage(colorize("&4-> &fStartup process finished with a fatal error."));

        plugin.getServer()
                .getConsoleSender()
                .sendMessage(colorize("&a------------------------------------------"));

        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }
}
