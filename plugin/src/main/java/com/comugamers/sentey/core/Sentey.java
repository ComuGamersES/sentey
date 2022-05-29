package com.comugamers.sentey.core;

import com.comugamers.sentey.common.Service;
import com.comugamers.sentey.core.login.filter.LoginFilter;
import com.comugamers.sentey.core.login.action.LoginAction;
import com.comugamers.sentey.core.guice.SenteyModule;
import com.comugamers.sentey.core.ping.action.PingAction;
import com.comugamers.sentey.core.ping.filter.PingFilter;
import com.comugamers.sentey.core.service.dependency.DependencyService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        // Check if the server is running Java 16 or later. We do this on plugin load
        // to prevent Guice from doing its things before sending the warning to the logs.
        if (SystemUtils.getJavaVersion() >= 16) {
            // If so, warn the server admin that this plugin may not be compatible with it
            getLogger().warning(
                    "It looks like you're running Java 16 or higher. " + this.getDescription().getName()
                            + " may not be fully compatible with it."
            );

            // Get required arguments since in Java 17 the "--illegal-access=permit" argument was removed
            String requiredArguments = SystemUtils.getJavaVersion() >= 17
                    ? "--add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util.zip=ALL-UNNAMED"
                    : "--illegal-access=permit";

            // And tell the admin to add the following arguments to their
            // JVM in order to solve the errors during plugin initialization
            getLogger().warning(
                    "Please make sure to include the following argument(s) to your JVM - especially if" +
                            " you see errors during plugin initialization: \"" + requiredArguments + "\""
            );
        }

        // Initialize array lists
        this.loginFilters = new ArrayList<>();
        this.loginActions = new ArrayList<>();
        this.pingActions = new ArrayList<>();
        this.pingFilters = new ArrayList<>();

        // Start the dependency service which will download all required dependencies by the plugin
        Service dependencyService = new DependencyService(this);
        dependencyService.start();
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
        // Check if the main service was injected properly
        if(service == null) {
            // If not, log an error
            this.getLogger().severe(
                    "Sentey was not properly initialized. Did an error occur while downloading dependencies?"
            );
        } else {
            // Stop the main service
            service.stop();
        }
    }

    /**
     * This method will return the raw {@link ArrayList} of {@link LoginFilter login actions}. Removing items
     * from the {@link ArrayList} is not recommended and may result in unexpected and/or unwanted behavior.
     *
     * You may want to use {@link Sentey#addLoginFilter(LoginFilter)}, {@link Sentey#removeLoginFilter(LoginFilter)}
     * or {@link Sentey#isLoginFilterRegistered(LoginFilter)} instead.
     *
     * @return The {@link ArrayList} of registered {@link LoginFilter ping actions}.
     */
    public List<LoginFilter> getLoginFilters() {
        return loginFilters;
    }

    /**
     * Adds a {@link LoginFilter} to the {@link ArrayList} of registered {@link LoginFilter login filters}.
     * Attempting to add a {@link LoginFilter} that is already registered will have no effect.
     *
     * @param filter The {@link LoginFilter} to add.
     */
    public void addLoginFilter(LoginFilter filter) {
        if(loginFilters.contains(filter)) {
            return;
        }

        loginFilters.add(filter);
    }

    /**
     * Removes a {@link LoginFilter} from the {@link ArrayList} of registered {@link LoginFilter login filters}.
     * Attempting to remove a {@link LoginFilter} that is not registered will have no effect. Note that using
     * this method is not recommended and may result in unexpected and/or unwanted behavior.
     *
     * @param filter The {@link LoginFilter} to remove.
     */
    public void removeLoginFilter(LoginFilter filter) {
        loginFilters.remove(filter);
    }

    /**
     * Checks if a {@link LoginFilter} is registered.
     *
     * @param filter The {@link LoginFilter} to check.
     * @return True if the {@link LoginFilter} is registered, false otherwise.
     */
    public boolean isLoginFilterRegistered(LoginFilter filter) {
        return loginFilters.contains(filter);
    }

    /**
     * This method will return the raw {@link ArrayList} of {@link LoginAction login actions}. Removing items
     * from the {@link ArrayList} is not recommended and may result in unexpected and/or unwanted behavior.
     *
     * @return The {@link ArrayList} of registered {@link LoginAction ping actions}.
     */
    public List<LoginAction> getLoginActions() {
        return loginActions;
    }

    /**
     * Adds a {@link LoginAction} to the {@link ArrayList} of registered {@link LoginAction login actions}.
     * Attempting to add a {@link LoginAction} that is already registered will have no effect.
     *
     * @param action The {@link LoginAction} to add.
     */
    public void addLoginAction(LoginAction action) {
        if(loginActions.contains(action)) {
            return;
        }

        loginActions.add(action);
    }

    /**
     * Removes a {@link LoginAction} from the {@link ArrayList} of registered {@link LoginAction login actions}.
     * Attempting to remove a {@link LoginAction} that is not registered will have no effect. Note that using
     * this method is not recommended and may result in unexpected and/or unwanted behavior.
     *
     * @param action The {@link LoginAction} to remove.
     */
    public void removeLoginAction(LoginAction action) {
        loginActions.remove(action);
    }

    /**
     * Checks if a {@link LoginAction} is registered.
     *
     * @param action The {@link LoginAction} to check.
     * @return true if the {@link LoginAction} is registered, false otherwise.
     */
    public boolean isLoginActionRegistered(LoginAction action) {
        return loginActions.contains(action);
    }

    /**
     * This method will return the raw {@link ArrayList} of {@link PingAction ping actions}. Removing items
     * from the {@link ArrayList} is not recommended and may result in unexpected and/or unwanted behavior.
     *
     * @return The {@link ArrayList} of registered {@link PingAction ping actions}.
     */
    public List<PingAction> getPingActions() {
        return pingActions;
    }

    /**
     * Adds a {@link PingAction} to the {@link ArrayList} of registered {@link PingAction ping actions}.
     * Attempting to add a {@link PingAction} that is already registered will have no effect.
     *
     * @param action The {@link PingAction} to add.
     */
    public void addPingAction(PingAction action) {
        if(pingActions.contains(action)) {
            return;
        }

        pingActions.add(action);
    }

    /**
     * Removes a {@link PingAction} from the {@link ArrayList} of registered {@link PingAction ping actions}.
     * Attempting to remove a {@link PingAction} that is not registered will have no effect. Note that using
     * this method is not recommended and may result in unexpected and/or unwanted behavior.
     *
     * @param action The {@link PingAction} to remove.
     */
    public void removePingAction(PingAction action) {
        pingActions.remove(action);
    }

    /**
     * Checks if a {@link PingAction} is registered.
     *
     * @param action The {@link PingAction} to check.
     * @return true if the {@link PingAction} is registered, false otherwise.
     */
    public boolean isPingActionRegistered(PingAction action) {
        return pingActions.contains(action);
    }

    /**
     * This method will return the raw {@link ArrayList} of {@link PingFilter ping filters}. Removing items
     * from the {@link ArrayList} is not recommended and may result in unexpected and/or unwanted behavior.
     *
     * @return The {@link ArrayList} of registered {@link PingFilter ping actions}.
     */
    public List<PingFilter> getPingFilters() {
        return pingFilters;
    }

    /**
     * Adds a {@link PingFilter} to the {@link ArrayList} of registered {@link PingFilter ping filters}.
     * Attempting to add a {@link PingFilter} that is already registered will have no effect.
     *
     * @param filter The {@link PingFilter} to add.
     */
    public void addPingFilter(PingFilter filter) {
        if(pingFilters.contains(filter)) {
            return;
        }

        pingFilters.add(filter);
    }

    /**
     * Removes a {@link PingFilter} from the {@link ArrayList} of registered {@link PingFilter ping filters}.
     * Attempting to remove a {@link PingFilter} that is not registered will have no effect. Note that using
     * this method is not recommended and may result in unexpected and/or unwanted behavior.
     *
     * @param filter The {@link PingFilter} to remove.
     */
    public void removePingFilter(PingFilter filter) {
        pingFilters.remove(filter);
    }

    /**
     * Checks if a {@link PingFilter} is registered.
     *
     * @param filter The {@link PingFilter} to check.
     * @return true if the {@link PingFilter} is registered, false otherwise.
     */
    public boolean isPingFilterRegistered(PingFilter filter) {
        return pingFilters.contains(filter);
    }
}
