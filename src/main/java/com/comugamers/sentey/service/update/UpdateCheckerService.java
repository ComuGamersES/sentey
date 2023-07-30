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
        String current = plugin.getDescription().getVersion();

        if (current.contains("SNAPSHOT") || current.contains("DEV")) {
            plugin.getServer()
                    .getConsoleSender()
                    .sendMessage(
                            colorize(
                                    "&e-> &fUpdate checking disabled - development build detected!"
                            )
                    );

            return;
        }

        if (config.getBoolean("config.updates.plugin-startup", true)) {
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this::requestCheck, 80L);
            return;
        }

        if (!config.getBoolean("config.updates.repeating-task", true)) {
            return;
        }

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::requestCheck, 18000L, 0L);
    }

    private void requestCheck() {
        try {
            String latest = updateChecker.fetchLatest();
            String current = plugin.getDescription().getVersion();

            if (latest.equals(current)) {
                return;
            }

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
