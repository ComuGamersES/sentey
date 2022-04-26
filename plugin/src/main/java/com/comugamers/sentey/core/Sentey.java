package com.comugamers.sentey.core;

import com.comugamers.sentey.common.Service;
import com.comugamers.sentey.core.login.filter.LoginFilter;
import com.comugamers.sentey.core.login.action.LoginAction;
import com.comugamers.sentey.core.guice.SenteyModule;
import com.comugamers.sentey.core.ping.action.PingAction;
import com.comugamers.sentey.core.ping.filter.PingFilter;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Sentey extends JavaPlugin {

    @Inject
    private Service service;

    // Login related
    private List<LoginFilter> loginFilters;
    private List<LoginAction> loginActions;

    // Server list ping related
    private List<PingAction> pingActions;
    private List<PingFilter> pingFilters;

    @Override
    public void onLoad() {
        this.loginFilters = new ArrayList<>();
        this.loginActions = new ArrayList<>();
        this.pingActions = new ArrayList<>();
        this.pingFilters = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        // Create a new Injector based on the Sentey module
        Injector injector = Guice.createInjector(
                new SenteyModule(this)
        );

        // Inject members
        injector.injectMembers(this);

        // Start the main service
        service.start();
    }

    @Override
    public void onDisable() {
        // Stop the main service
        service.stop();
    }

    public List<LoginFilter> getLoginFilters() {
        return loginFilters;
    }

    public List<LoginAction> getLoginActions() {
        return loginActions;
    }

    public List<PingAction> getPingActions() {
        return pingActions;
    }

    public List<PingFilter> getPingFilters() {
        return pingFilters;
    }
}
