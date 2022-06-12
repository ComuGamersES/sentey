package com.comugamers.sentey.guice.submodules;

import com.comugamers.sentey.util.UpdateChecker;
import com.google.inject.AbstractModule;

public class UpdateCheckerModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(UpdateChecker.class)
                .toInstance(
                        new UpdateChecker(102550)
                );
    }
}
