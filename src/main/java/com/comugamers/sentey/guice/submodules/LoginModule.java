package com.comugamers.sentey.guice.submodules;

import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.core.login.action.internal.*;
import com.comugamers.sentey.login.filter.LoginFilter;
import com.comugamers.sentey.core.login.filter.internal.*;
import com.comugamers.sentey.login.action.internal.*;
import com.comugamers.sentey.login.filter.internal.*;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class LoginModule extends AbstractModule {

    @Override
    protected void configure() {
        // Bind each internal login action
        bindInternalActions();

        // Bind each internal login filter
        bindInternalFilters();
    }

    private void bindInternalActions() {
        // Create a new Multibinder for the LoginAction interface
        Multibinder<LoginAction> loginActionMultibinder = Multibinder.newSetBinder(binder(), LoginAction.class);

        // Bind each internal login action
        loginActionMultibinder.addBinding().to(DisallowEventLoginAction.class);
        loginActionMultibinder.addBinding().to(CommandLoginAction.class);
        loginActionMultibinder.addBinding().to(AlertLoginAction.class);
        loginActionMultibinder.addBinding().to(WebhookLoginAction.class);
        loginActionMultibinder.addBinding().to(AbuseReportLoginAction.class);
    }

    private void bindInternalFilters() {
        // Create a new multibinder for the LoginFilter interface as well
        Multibinder<LoginFilter> multibinder = Multibinder.newSetBinder(binder(), LoginFilter.class);

        // Bind each internal login filter
        multibinder.addBinding().to(NullAddressLoginFilter.class);
        multibinder.addBinding().to(MalformedAddressLoginFilter.class);
        multibinder.addBinding().to(AnyLocalAddressLoginFilter.class);
        multibinder.addBinding().to(LoopbackAddressLoginFilter.class);
        multibinder.addBinding().to(SiteLocalAddressLoginFilter.class);
        multibinder.addBinding().to(UnknownProxyLoginFilter.class);
    }
}
