package com.comugamers.sentey.service.update;

import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.service.Service;
import com.comugamers.sentey.util.UpdateChecker;
import com.comugamers.sentey.util.file.YamlFile;

import javax.inject.Inject;
import java.io.IOException;

import static com.comugamers.sentey.util.TextUtil.colorize;

public class UpdateCheckerService implements Service {

    @Inject
    private Sentey plugin;

    @Inject
    private YamlFile config;

    @Inject
    private UpdateChecker updateChecker;

    @Override
    public void start() {
        // Get the current version
        String current = plugin.getDescription().getVersion();

        // Check if the plugin version is a snapshot or a development build
        if(current.contains("SNAPSHOT") || current.contains("DEV")) {
            // If so, log a warning
            plugin.getServer()
                    .getConsoleSender()
                    .sendMessage(
                            colorize(
                                    "&e-> &fUpdate checking disabled - development build detected!"
                            )
                    );

            // And disable this service regardless of the preferred settings
            return;
        }

        // Check if checking for updates on plugin startup is enabled
        if(config.getBoolean("config.updates.plugin-startup", true)) {
            // Request a check on plugin startup after a while
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this::requestCheck, 80L);
            return;
        }

        // Check if the repeating task is enabled
        if(!config.getBoolean("config.updates.repeating-task", true)) {
            // If it isn't, return
            return;
        }

        // Make a repeating task that will check for updates every once in a while
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::requestCheck, 18000L, 0L);
    }

    private void requestCheck() {
        try {
            // Fetch the latest and get the current version
            String latest = updateChecker.fetchLatest();
            String current = plugin.getDescription().getVersion();

            // Check if the version is different
            if(latest.equals(current)) {
                return;
            }

            // Log that the server is running a different version
            plugin.getLogger().info(
                    "You seem to be running a different version than the one on SpigotMC. Consider updating "
                            + "Sentey from https://www.spigotmc.org/resources/102550/ - it should only take a while!"
                            + " (current: " + current + "; latest: " + latest + ")"
            );
        } catch (IOException ex) {
            plugin.getLogger().warning("Unable to check for updates. Sorry about that. (" + ex.getMessage() + ")");
        }
    }
}
