package com.comugamers.sentey.core.service.modifier;

import com.comugamers.sentey.common.Service;
import com.comugamers.sentey.core.login.modifier.LoginModifier;
import com.comugamers.sentey.core.Sentey;
import com.google.inject.Inject;

import java.util.Set;

public class InternalLoginModifierService implements Service {

    @Inject
    private Set<LoginModifier> loginModifierSet;

    @Inject
    private Sentey plugin;

    @Override
    public void start() {
        // Register all bound login modifiers
        loginModifierSet.forEach(modifier -> plugin.getLoginModifiers().add(modifier));
    }
}
