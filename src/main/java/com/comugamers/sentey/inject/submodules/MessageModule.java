package com.comugamers.sentey.inject.submodules;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.Sentey;
import team.unnamed.inject.AbstractModule;

public class MessageModule extends AbstractModule {

    private final Sentey plugin;

    public MessageModule(Sentey plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        this.bind(YamlFile.class)
                .named("messages")
                .toInstance(new YamlFile(plugin, "messages.yml"));
    }
}
