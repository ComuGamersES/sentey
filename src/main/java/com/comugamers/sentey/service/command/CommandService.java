package com.comugamers.sentey.service.command;

import com.comugamers.sentey.service.Service;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.command.SenteyCommand;
import com.google.inject.Inject;

public class CommandService implements Service {

    @Inject
    private Sentey plugin;

    @Inject
    private SenteyCommand senteyCommand;

    @Override
    public void start() {
        plugin.getCommand("sentey").setExecutor(senteyCommand);
    }
}
