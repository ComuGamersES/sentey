package com.comugamers.sentey.core.service.ping;

import com.comugamers.sentey.common.Service;
import com.comugamers.sentey.core.Sentey;
import com.comugamers.sentey.core.ping.action.PingAction;
import com.comugamers.sentey.core.ping.filter.PingFilter;
import com.google.inject.Inject;

import java.util.Set;

import static com.comugamers.sentey.common.util.TextUtil.colorize;

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

            plugin.getPingFilters().add(filter);
        });

        // Loop through each bound ping action
        pingActionSet.forEach(action -> {
            // Log that we're registering a specific ping action
            plugin.getServer().getConsoleSender().sendMessage(
                    colorize("&a-> &fRegistering internal ping action &a" + action.getClass().getSimpleName())
            );

            // Add it to the list of login actions
            plugin.getPingActions().add(action);
        });
    }
}
