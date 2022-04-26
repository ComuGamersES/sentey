package com.comugamers.sentey.core.guice.submodules;

import com.comugamers.sentey.core.login.action.LoginAction;
import com.comugamers.sentey.core.login.action.internal.AlertLoginAction;
import com.comugamers.sentey.core.login.action.internal.CommandLoginAction;
import com.comugamers.sentey.core.login.action.internal.DisallowEventLoginAction;
import com.comugamers.sentey.core.login.action.internal.WebhookLoginAction;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class ActionModule extends AbstractModule {

    @Override
    protected void configure() {
        // Create a new Multibinder for the LoginModifier interface
        Multibinder<LoginAction> multibinder = Multibinder.newSetBinder(binder(), LoginAction.class);

        // Bind each internal action
        multibinder.addBinding().to(DisallowEventLoginAction.class);
        multibinder.addBinding().to(CommandLoginAction.class);
        multibinder.addBinding().to(AlertLoginAction.class);
        multibinder.addBinding().to(WebhookLoginAction.class);
    }
}
