package com.comugamers.sentey.service.login;

import com.comugamers.sentey.service.Service;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.login.filter.LoginFilter;
import com.google.inject.Inject;

import java.util.Set;

import static com.comugamers.sentey.util.TextUtil.colorize;

public class LoginService implements Service {

    @Inject
    private Set<LoginAction> loginActionSet;

    @Inject
    private Set<LoginFilter> loginFilterSet;

    @Inject
    private Sentey plugin;

    @Override
    public void start() {
        // Loop through each bound login filter
        loginFilterSet.forEach(filter -> {
            // Log that we're registering a specific login filter
            plugin.getServer().getConsoleSender().sendMessage(
                    colorize("&a-> &fRegistering internal login filter &a" + filter.getClass().getSimpleName())
            );

            plugin.addLoginFilter(filter);
        });

        // Loop through each bound login action
        loginActionSet.forEach(action -> {
            // Log that we're registering a specific login action
            plugin.getServer().getConsoleSender().sendMessage(
                    colorize("&a-> &fRegistering internal login action &a" + action.getClass().getSimpleName())
            );

            // Add it to the list of login actions
            plugin.addLoginAction(action);
        });
    }
}
