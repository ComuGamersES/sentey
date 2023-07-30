package com.comugamers.sentey;

import com.comugamers.sentey.service.Service;
import com.comugamers.sentey.login.filter.LoginFilter;
import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.inject.SenteyModule;
import com.comugamers.sentey.ping.action.PingAction;
import com.comugamers.sentey.ping.filter.PingFilter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.inject.Injector;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class of the Sentey {@link Plugin plugin}.
 *
 * @author ComuGamers
 */
public class Sentey extends JavaPlugin {

    @Inject
    private Service service;

    private static String version;

    private List<LoginFilter> loginFilters;
    private List<LoginAction> loginActions;
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
        version = getDescription().getVersion();
        Injector injector = Injector.create(new SenteyModule(this));
        injector.injectMembers(this);
        service.start();
    }

    @Override
    public void onDisable() {
        if (service == null) {
            this.getLogger().severe(
                    "Sentey was not properly initialized. Try checking above for more information. " +
                            "(version: " + this.getDescription().getVersion() + ")"
            );
        } else {
            service.stop();
        }
    }

    /**
     * @return The current version of {@link Sentey}.
     */
    public static String getCurrentVersion() {
        return version;
    }

    /**
     * This method will return the raw {@link ArrayList} of {@link LoginFilter login actions}. Removing items
     * from the {@link ArrayList} is not recommended and may result in unexpected and/or unwanted behavior.
     *
     * @return The {@link ArrayList} of registered {@link LoginFilter ping actions}.
     */
    public List<LoginFilter> getLoginFilters() {
        return loginFilters;
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
     * This method will return the raw {@link ArrayList} of {@link PingAction ping actions}. Removing items
     * from the {@link ArrayList} is not recommended and may result in unexpected and/or unwanted behavior.
     *
     * @return The {@link ArrayList} of registered {@link PingAction ping actions}.
     */
    public List<PingAction> getPingActions() {
        return pingActions;
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
}
