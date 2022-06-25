package com.comugamers.sentey.inject.submodules;

import com.comugamers.sentey.util.UpdateChecker;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;

import javax.inject.Singleton;

public class UpdateCheckerModule extends AbstractModule {

    @Singleton
    @Provides
    public UpdateChecker provideUpdateChecker() {
        return new UpdateChecker(102550);
    }
}
