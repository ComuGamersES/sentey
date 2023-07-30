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
        pingFilterSet.forEach(filter -> {
            plugin.getServer().getConsoleSender().sendMessage(
                    colorize("&a-> &fRegistering internal ping filter &a" + filter.getClass().getSimpleName())
            );

            plugin.getPingFilters().add(filter);
        });

        pingActionSet.forEach(action -> {
            plugin.getServer().getConsoleSender().sendMessage(
                    colorize("&a-> &fRegistering internal ping action &a" + action.getClass().getSimpleName())
            );

            plugin.getPingActions().add(action);
        });
    }
}
