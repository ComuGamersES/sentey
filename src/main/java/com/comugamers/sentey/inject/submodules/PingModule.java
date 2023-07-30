package com.comugamers.sentey.inject.submodules;

import com.comugamers.sentey.ping.action.PingAction;
import com.comugamers.sentey.ping.action.internal.AbuseReportPingAction;
import com.comugamers.sentey.ping.action.internal.AlertPingAction;
import com.comugamers.sentey.ping.action.internal.CommandPingAction;
import com.comugamers.sentey.ping.action.internal.WebhookPingAction;
import com.comugamers.sentey.ping.filter.PingFilter;
import com.comugamers.sentey.ping.filter.internal.TrustedSourcesPingFilter;
import team.unnamed.inject.AbstractModule;

public class PingModule extends AbstractModule {

    @Override
    protected void configure() {
        this.multibind(PingAction.class)
                .asSet()
                .to(AlertPingAction.class)
                .to(WebhookPingAction.class)
                .to(AbuseReportPingAction.class)
                .to(CommandPingAction.class)
                .singleton();

        this.multibind(PingFilter.class)
                .asSet()
                .to(TrustedSourcesPingFilter.class)
                .singleton();
    }
}
