package com.comugamers.sentey.core.guice.submodules;

import com.comugamers.sentey.common.login.LoginModifier;
import com.comugamers.sentey.core.login.address.*;
import com.comugamers.sentey.core.login.proxy.UnknownProxyLoginModifier;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class ModifierModule extends AbstractModule {

    @Override
    protected void configure() {
        // Create a new multibinder
        Multibinder<LoginModifier> multibinder = Multibinder.newSetBinder(binder(), LoginModifier.class);

        // Add bindings
        multibinder.addBinding().to(NullAddressLoginModifier.class);
        multibinder.addBinding().to(MalformedAddressLoginModifier.class);
        multibinder.addBinding().to(AnyLocalAddressLoginModifier.class);
        multibinder.addBinding().to(LoopbackAddressLoginModifier.class);
        multibinder.addBinding().to(SiteLocalAddressLoginModifier.class);
        multibinder.addBinding().to(UnknownProxyLoginModifier.class);
    }
}
