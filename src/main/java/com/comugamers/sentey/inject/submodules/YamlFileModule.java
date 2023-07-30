package com.comugamers.sentey.inject.submodules;

import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.util.file.YamlFile;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;

import javax.inject.Singleton;

public class YamlFileModule extends AbstractModule {

    @Singleton
    @Provides
    public YamlFile provideConfigurationFile(Sentey plugin) {
        return new YamlFile(plugin, "config");
    }
}
