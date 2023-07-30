package com.comugamers.sentey.listeners.player;

import com.comugamers.sentey.login.filter.LoginFilter;
import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.util.file.YamlFile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import javax.inject.Inject;

import static com.comugamers.sentey.util.ConnectionUtil.getRemoteAddress;

public class PlayerLoginListener implements Listener {

    @Inject
    private Sentey plugin;

    @Inject
    private YamlFile config;

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        LoginContext ctx = new LoginContext(event);

        for (LoginFilter module : plugin.getLoginFilters()) {
            if (!module.isClean(ctx)) {
                for (LoginAction action : plugin.getLoginActions()) {
                    action.handle(ctx, module.getName());
                }

                break;
            }
        }
    }
}
