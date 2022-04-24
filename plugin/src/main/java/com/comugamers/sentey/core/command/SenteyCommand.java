package com.comugamers.sentey.core.command;

import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.core.Sentey;
import com.google.inject.Inject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

import static com.comugamers.sentey.common.string.StringUtil.colorize;
import static com.comugamers.sentey.common.util.NetworkUtil.isValidIPv4;

// TODO: improve this class, it is a mess
public class SenteyCommand implements CommandExecutor {

    @Inject
    private YamlFile config;

    @Inject
    private Sentey plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 0) {
            // Send the 'Running Sentey version x.x.x' message
            sender.sendMessage(
                    colorize(
                            "&5&l>> &fRunning &aSentey &fversion &a" + plugin.getDescription().getVersion()
                                    + "&f.\n&5&l>> &fAuthors: &a"
                                    + String.join("&f, &a", plugin.getDescription().getAuthors()
                            )
                    )
            );

            // Check if the sender has the 'sentey.admin' permission
            if(sender.hasPermission("sentey.admin")) {
                // If so, send the 'Run /sentey help' message
                sender.sendMessage(
                        colorize("&5&l>> &fRun &a/sentey help&f for help.")
                );
            }

            // And return
            return true;
        }

        if(!sender.hasPermission("sentey.admin")) {
            sender.sendMessage(config.getString("config.messages.command.no-permission"));
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch(subCommand) {
            case "reload": {
                // Reload the configuration file
                config.reload();

                // Send the 'Configuration reloaded' message
                sender.sendMessage(config.getString("config.messages.command.config-reloaded"));
                return true;
            }
            default:
            case "help": {
                // Send the help message
                sender.sendMessage(config.getString("config.messages.command.help"));
                return true;
            }
            case "trusted-proxies": {
                if(args.length < 2) {
                    sender.sendMessage(config.getString("config.messages.command.trusted-proxies.usage"));
                    return true;
                }

                String action = args[1].toLowerCase();
                switch(action) {
                    default: {
                        // Send the usage message
                        sender.sendMessage(config.getString("config.messages.command.trusted-proxies.usage"));

                        // And return
                        return true;
                    }
                    case "add": {
                        // Check if enough arguments were provided
                        if(args.length < 3) {
                            // If not, send the usage message
                            sender.sendMessage(config.getString("config.messages.command.trusted-proxies.add.usage"));
                            return true;
                        }

                        // Get the list of trusted proxies
                        List<String> trustedProxies = config.getStringList(
                                "config.login.unknown-proxies.allowed-proxies"
                        );

                        // Get the proxy address
                        String proxyAddress = args[2];

                        // Check if the proxy address is valid
                        if (!isValidIPv4(proxyAddress)) {
                            // If not, send the 'Invalid IP address' message
                            sender.sendMessage(
                                    config.getString("config.messages.command.trusted-proxies.add.invalid-ipv4")
                                            .replace("%proxy%", proxyAddress)
                            );

                            // And return
                            return true;
                        }

                        // Check if the proxy is already trusted
                        if (trustedProxies.contains(proxyAddress)) {
                            // If so, send the 'Proxy already trusted' message and return
                            sender.sendMessage(config.getString("config.messages.command.trusted-proxies.add.already-trusted"));
                            return true;
                        }

                        // Add the proxy to the trusted proxies list
                        trustedProxies.add(proxyAddress);

                        // Set the trusted proxies list
                        config.set("config.login.unknown-proxies.allowed-proxies", trustedProxies);

                        // Save the configuration file
                        config.save();

                        // Send the 'Proxy added' message
                        sender.sendMessage(
                                config.getString("config.messages.command.trusted-proxies.add.success")
                                        .replace("%proxy%", proxyAddress)
                        );

                        // And return
                        return true;
                    }
                    case "delete":
                    case "remove": {
                        // Check if enough arguments were provided
                        if(args.length < 3) {
                            // If not, send the usage message
                            sender.sendMessage(
                                    config.getString("config.messages.command.trusted-proxies.remove.usage")
                            );

                            // And return
                            return true;
                        }

                        // Get the proxy address
                        String proxyAddress = args[2];

                        // Get the list of trusted proxies
                        List<String> trustedProxies = config.getStringList(
                                "config.login.unknown-proxies.allowed-proxies"
                        );

                        // Check if the proxy is already trusted
                        if (!trustedProxies.contains(proxyAddress)) {
                            // If not, send the 'Proxy not trusted' message
                            sender.sendMessage(
                                    config.getString("config.messages.command.trusted-proxies.remove.not-trusted")
                            );

                            // And return
                            return true;
                        }

                        // Remove the proxy from the trusted proxies list
                        trustedProxies.remove(proxyAddress);

                        // Set the trusted proxies list
                        config.set("config.login.unknown-proxies.allowed-proxies", trustedProxies);

                        // Save the configuration file
                        config.save();

                        // Send the 'Proxy removed' message
                        sender.sendMessage(
                                config.getString("config.messages.command.trusted-proxies.remove.success")
                                        .replace("%proxy%", proxyAddress)
                        );

                        // And return
                        return true;
                    }
                    case "list": {
                        // Get the list of trusted proxies
                        List<String> trustedProxies = config.getStringList(
                                "config.login.unknown-proxies.allowed-proxies"
                        );

                        // Check if it is empty
                        if(trustedProxies.isEmpty()) {
                            // If so, send the 'There are no trusted proxies' message
                            sender.sendMessage(
                                    config.getString("config.messages.command.trusted-proxies.list.none")
                            );

                            // And return
                            return true;
                        }

                        // Get the trusted proxy list delimiter
                        String delimiter = config.getString("config.messages.command.trusted-proxies.list.delimiter");

                        // Send the list of trusted proxies
                        sender.sendMessage(
                                config.getString("config.messages.command.trusted-proxies.list.message")
                                        .replace("%proxies%", String.join(delimiter, trustedProxies))
                        );

                        // And return
                        return true;
                    }
                    case "setup-toggle":
                    case "setup":
                    case "toggle-setup": {
                        // Get the current setup state
                        boolean setup = config.getBoolean("config.login.unknown-proxies.setup");

                        // Set to the new one
                        config.set("config.login.unknown-proxies.setup", !setup);

                        // Save the configuration file
                        config.save();

                        // Send the 'Setup enabled/disabled' message
                        sender.sendMessage(
                                config.getString(
                                        "config.messages.command.trusted-proxies.toggle-setup."
                                                + (setup ? "disabled" : "enabled")
                                )
                        );

                        // And return
                        return true;
                    }
                }
            }
        }
    }
}
