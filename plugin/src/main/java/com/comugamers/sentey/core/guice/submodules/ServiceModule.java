package com.comugamers.sentey.core.guice.submodules;

import com.comugamers.sentey.common.Service;
import com.comugamers.sentey.core.service.MainService;
import com.comugamers.sentey.core.service.command.CommandService;
import com.comugamers.sentey.core.service.listener.ListenerService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import static com.google.inject.name.Names.named;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        // Bind the main service
        this.bind(Service.class)
                .to(MainService.class)
                .in(Scopes.SINGLETON);

        // Bind the listener service
        this.bind(Service.class)
                .annotatedWith(named("listener"))
                .to(ListenerService.class)
                .in(Scopes.SINGLETON);

        // Bind the command service
        this.bind(Service.class)
                .annotatedWith(named("command"))
                .to(CommandService.class)
                .in(Scopes.SINGLETON);
    }
}
