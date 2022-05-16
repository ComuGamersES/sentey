package com.comugamers.sentey.core.guice.submodules;

import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.core.Sentey;
import com.google.inject.AbstractModule;

import static com.google.inject.name.Names.named;

public class MessageModule extends AbstractModule {

    private final Sentey plugin;

    public MessageModule(Sentey plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        this.bind(YamlFile.class)
                .annotatedWith(named("messages"))
                .toInstance(new YamlFile(plugin, "messages.yml"));
    }
}
