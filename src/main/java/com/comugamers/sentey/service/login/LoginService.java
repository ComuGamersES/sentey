package com.comugamers.sentey.service.login;

import com.comugamers.sentey.service.Service;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.login.filter.LoginFilter;

import javax.inject.Inject;
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
        loginFilterSet.forEach(filter -> {
            plugin.getServer().getConsoleSender().sendMessage(
                    colorize("&a-> &fRegistering internal login filter &a" + filter.getClass().getSimpleName())
            );

            plugin.getLoginFilters().add(filter);
        });

        loginActionSet.forEach(action -> {
            plugin.getServer().getConsoleSender().sendMessage(
                    colorize("&a-> &fRegistering internal login action &a" + action.getClass().getSimpleName())
            );

            plugin.getLoginActions().add(action);
        });
    }
}
