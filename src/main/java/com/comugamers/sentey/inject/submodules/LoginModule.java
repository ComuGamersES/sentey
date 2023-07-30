package com.comugamers.sentey.inject.submodules;

import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.login.filter.LoginFilter;
import com.comugamers.sentey.login.action.internal.*;
import com.comugamers.sentey.login.filter.internal.*;
import team.unnamed.inject.AbstractModule;

public class LoginModule extends AbstractModule {

    @Override
    protected void configure() {
        this.multibind(LoginAction.class)
                .asSet()
                .to(DisallowEventLoginAction.class)
                .to(CommandLoginAction.class)
                .to(AlertLoginAction.class)
                .to(WebhookLoginAction.class)
                .to(AbuseReportLoginAction.class)
                .singleton();

        this.multibind(LoginFilter.class)
                .asSet()
                .to(NullAddressLoginFilter.class)
                .to(MalformedAddressLoginFilter.class)
                .to(AnyLocalAddressLoginFilter.class)
                .to(LoopbackAddressLoginFilter.class)
                .to(SiteLocalAddressLoginFilter.class)
                .to(UnknownProxyLoginFilter.class)
                .singleton();
    }
}
