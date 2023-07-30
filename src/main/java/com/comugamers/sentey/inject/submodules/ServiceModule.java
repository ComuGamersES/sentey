package com.comugamers.sentey.inject.submodules;

import com.comugamers.sentey.service.Service;
import com.comugamers.sentey.service.main.MainService;
import com.comugamers.sentey.service.command.CommandService;
import com.comugamers.sentey.service.listener.ListenerService;
import com.comugamers.sentey.service.login.LoginService;
import com.comugamers.sentey.service.metrics.MetricsService;
import com.comugamers.sentey.service.ping.PingService;
import com.comugamers.sentey.service.update.UpdateCheckerService;
import team.unnamed.inject.AbstractModule;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(Service.class)
                .to(MainService.class)
                .singleton();

        this.bind(Service.class)
                .named("listener")
                .to(ListenerService.class)
                .singleton();

        this.bind(Service.class)
                .named("command")
                .to(CommandService.class)
                .singleton();

        this.bind(Service.class)
                .named("ping")
                .to(PingService.class)
                .singleton();

        this.bind(Service.class)
                .named("login")
                .to(LoginService.class)
                .singleton();

        this.bind(Service.class)
                .named("updateChecker")
                .to(UpdateCheckerService.class)
                .singleton();

        this.bind(Service.class)
                .named("metrics")
                .to(MetricsService.class)
                .singleton();
    }
}
