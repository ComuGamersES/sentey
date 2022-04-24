package com.comugamers.sentey.core.service.command;

import com.comugamers.sentey.common.Service;
import com.comugamers.sentey.core.Sentey;
import com.comugamers.sentey.core.command.SenteyCommand;
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
