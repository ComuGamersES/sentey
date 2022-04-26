package com.comugamers.sentey.core.service.action;

import com.comugamers.sentey.common.Service;
import com.comugamers.sentey.core.login.action.LoginAction;
import com.comugamers.sentey.core.Sentey;
import com.google.inject.Inject;

import java.util.Set;

public class InternalLoginActionService implements Service {

    @Inject
    private Sentey plugin;

    @Inject
    private Set<LoginAction> loginActionSet;

    @Override
    public void start() {
        // Register all bound login actions
        loginActionSet.forEach(action -> plugin.getLoginActions().add(action));
    }
}
