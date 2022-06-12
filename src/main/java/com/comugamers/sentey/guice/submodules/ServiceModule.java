package com.comugamers.sentey.guice.submodules;

import com.comugamers.sentey.service.Service;
import com.comugamers.sentey.service.main.MainService;
import com.comugamers.sentey.service.command.CommandService;
import com.comugamers.sentey.service.listener.ListenerService;
import com.comugamers.sentey.service.login.LoginService;
import com.comugamers.sentey.service.ping.PingService;
import com.comugamers.sentey.service.update.UpdateCheckerService;
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

        // Bind the ping service
        this.bind(Service.class)
                .annotatedWith(named("ping"))
                .to(PingService.class)
                .in(Scopes.SINGLETON);

        // Bind the login service
        this.bind(Service.class)
                .annotatedWith(named("login"))
                .to(LoginService.class)
                .in(Scopes.SINGLETON);

        this.bind(Service.class)
                .annotatedWith(named("updateChecker"))
                .to(UpdateCheckerService.class)
                .in(Scopes.SINGLETON);
    }
}
