package com.comugamers.sentey.inject;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.inject.submodules.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;

import javax.inject.Singleton;

public class SenteyModule extends AbstractModule {

    private final Sentey plugin;

    public SenteyModule(Sentey plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        this.bind(Sentey.class).toInstance(plugin);
        this.bind(Plugin.class).toInstance(plugin);
        this.bind(JavaPlugin.class).toInstance(plugin);

        this.install(new YamlFileModule());
        this.install(new CommandModule());
        this.install(new MessageModule());
        this.install(new ServiceModule());
        this.install(new AbuseDatabaseModule());
        this.install(new LoginModule());
        this.install(new PingModule());
        this.install(new ListenerModule());
        this.install(new UpdateCheckerModule());
    }
}
