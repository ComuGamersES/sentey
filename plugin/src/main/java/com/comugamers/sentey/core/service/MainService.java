package com.comugamers.sentey.core.service;

import com.comugamers.sentey.common.Service;
import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.core.Sentey;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.bukkit.command.CommandSender;
import org.spigotmc.SpigotConfig;

import static com.comugamers.sentey.common.util.TextUtil.colorize;
import static com.comugamers.sentey.common.util.VersionUtil.isSpigot;

public class MainService implements Service {

    @Inject
    private Sentey plugin;

    @Inject
    private YamlFile config;

    @Inject @Named("listener")
    private Service listenerService;

    @Inject @Named("command")
    private Service commandService;

    @Inject @Named("login")
    private Service loginService;

    @Inject @Named("ping")
    private Service pingService;

    @Override
    public void start() {
        // Get the ConsoleCommandSender
        CommandSender consoleCommandSender = plugin.getServer().getConsoleSender();

        // Send a nice initialization message to the console
        consoleCommandSender.sendMessage(colorize("          &fSentey - Version &a" + plugin.getDescription().getVersion()));
        consoleCommandSender.sendMessage(colorize("&a------------------------------------------"));
        consoleCommandSender.sendMessage(colorize("&a-> &fMade by: &a" + plugin.getDescription().getAuthors().toString()));
        consoleCommandSender.sendMessage(colorize(""));
        consoleCommandSender.sendMessage(colorize("&a-> &fServer software: &a" + plugin.getServer().getVersion()));
        consoleCommandSender.sendMessage(colorize(""));

        // Check if the 'Ignore Spigot Check' option is disabled
        if(!config.getBoolean("config.ignore-spigot-check")) {
            // If so, check if the server is running Spigot
            if(!isSpigot()) {
                // If not, log that the plugin can't run on CraftBukkit
                plugin.getServer()
                        .getConsoleSender()
                        .sendMessage(
                                colorize(
                                        "&4-> &fYour current server software is unsupported. Please use Spigot or a fork."
                                )
                        );

                // Log that the plugin startup process finished with a fatal error.
                cancelStartup();
            }
        } else {
            // Log that the server software check is ignored.
            plugin.getServer()
                    .getConsoleSender()
                    .sendMessage(colorize("&e-> &fIgnoring server software check!"));
        }

        // Check if the 'Ignore BungeeCord setting' option is disabled
        if(!config.getBoolean("config.ignore-bungeecord-check")) {
            // If so, check if the server has the BungeeCord option enabled
            if(isSpigot() && !SpigotConfig.bungee) {
                // If not, log that the BungeeCord option must be enabled on the 'spigot.yml' file.
                plugin.getServer()
                        .getConsoleSender()
                        .sendMessage(
                                colorize(
                                        "&4-> &fYour current server has the 'bungeecord' option disabled. Enable it on 'spigot.yml' file to use Sentey."
                                )
                        );

                // Log that the plugin startup process finished with a fatal error.
                cancelStartup();
            }
        } else {
            // Log that the BungeeCord option check is ignored.
            plugin.getServer()
                    .getConsoleSender()
                    .sendMessage(colorize("&e-> &fIgnoring BungeeCord boolean check!"));
        }

        // Start all services
        startServices(
                loginService,
                pingService,
                listenerService,
                commandService
        );

        // Send a message to the console saying that the startup process is finished
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
        // Log that the plugin startup process finished with a fatal error.
        plugin.getServer()
                .getConsoleSender()
                .sendMessage(colorize("&4-> &fStartup process finished with a fatal error."));

        // Log a separator
        plugin.getServer()
                .getConsoleSender()
                .sendMessage(colorize("&a------------------------------------------"));

        // And then stop the plugin
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }
}
