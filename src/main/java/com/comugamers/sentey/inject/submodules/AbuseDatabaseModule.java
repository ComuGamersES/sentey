package com.comugamers.sentey.inject.submodules;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.integrations.abuseipdb.AbuseIPDB;
import com.comugamers.sentey.Sentey;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

public class AbuseDatabaseModule extends AbstractModule {

    @Singleton
    @Provides
    public AbuseIPDB provideAbuseIPDB(Sentey plugin, YamlFile config) {
        return new AbuseIPDB(
                plugin, config.getString("config.integrations.abuseipdb.key", "unknown")
        );
    }
}
