package com.comugamers.sentey.core;

import com.comugamers.sentey.common.Service;
import com.comugamers.sentey.core.login.modifier.LoginModifier;
import com.comugamers.sentey.core.login.action.LoginAction;
import com.comugamers.sentey.core.guice.SenteyModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Sentey extends JavaPlugin {

    @Inject
    private Service service;

    private List<LoginModifier> loginModifiers;
    private List<LoginAction> loginActions;

    @Override
    public void onLoad() {
        this.loginModifiers = new ArrayList<>();
        this.loginActions = new ArrayList<>();
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

    public List<LoginModifier> getLoginModifiers() {
        return loginModifiers;
    }

    public List<LoginAction> getLoginActions() {
        return loginActions;
    }
}
