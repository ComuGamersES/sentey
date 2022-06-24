package com.comugamers.sentey.service.ping;

import com.comugamers.sentey.service.Service;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.ping.action.PingAction;
import com.comugamers.sentey.ping.filter.PingFilter;

import javax.inject.Inject;
import java.util.Set;

import static com.comugamers.sentey.util.TextUtil.colorize;

public class PingService implements Service {

    @Inject
    private Set<PingAction> pingActionSet;

    @Inject
    private Set<PingFilter> pingFilterSet;

    @Inject
    private Sentey plugin;

    @Override
    public void start() {
        // Loop through each bound ping filter
        pingFilterSet.forEach(filter -> {
            // Log that we're registering a specific ping filter
            plugin.getServer().getConsoleSender().sendMessage(
                    colorize("&a-> &fRegistering internal ping filter &a" + filter.getClass().getSimpleName())
            );

            // And add it to the list of ping filters
            plugin.addPingFilter(filter);
        });

        // Loop through each bound ping action
        pingActionSet.forEach(action -> {
            // Log that we're registering a specific ping action
            plugin.getServer().getConsoleSender().sendMessage(
                    colorize("&a-> &fRegistering internal ping action &a" + action.getClass().getSimpleName())
            );

            // And add it to the list of login actions
            plugin.addPingAction(action);
        });
    }
}
