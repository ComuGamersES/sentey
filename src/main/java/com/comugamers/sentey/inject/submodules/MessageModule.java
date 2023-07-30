package com.comugamers.sentey.inject.submodules;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.Sentey;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

public class MessageModule extends AbstractModule {

    @Singleton
    @Provides
    @Named("messages")
    private YamlFile provideMessages(Sentey plugin) {
        return new YamlFile(plugin, "messages.yml");
    }
}
