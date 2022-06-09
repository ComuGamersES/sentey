package com.comugamers.sentey.guice.submodules;

import com.comugamers.sentey.ping.action.PingAction;
import com.comugamers.sentey.ping.action.internal.AbuseReportPingAction;
import com.comugamers.sentey.ping.action.internal.AlertPingAction;
import com.comugamers.sentey.ping.action.internal.WebhookPingAction;
import com.comugamers.sentey.ping.filter.PingFilter;
import com.comugamers.sentey.ping.filter.internal.TrustedSourcesPingFilter;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class PingModule extends AbstractModule {

    @Override
    protected void configure() {
        // Bind each internal ping action
        bindInternalActions();

        // Bind each internal ping filter
        bindInternalFilters();
    }

    private void bindInternalActions() {
        // Create a new multibinder for the PingAction interface
        Multibinder<PingAction> multibinder = Multibinder.newSetBinder(binder(), PingAction.class);

        // Bind each internal ping action
        multibinder.addBinding().to(AlertPingAction.class);
        multibinder.addBinding().to(WebhookPingAction.class);
        multibinder.addBinding().to(AbuseReportPingAction.class);
    }

    private void bindInternalFilters() {
        // Create a new multibinder for the PingFilter interface
        Multibinder<PingFilter> multibinder = Multibinder.newSetBinder(binder(), PingFilter.class);

        // Bind each internal ping filter
        multibinder.addBinding().to(TrustedSourcesPingFilter.class);
    }
}
