package com.comugamers.sentey.core.login.action.internal;

import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.common.report.AbuseDatabase;
import com.comugamers.sentey.core.login.action.LoginAction;
import com.comugamers.sentey.core.login.context.LoginContext;
import com.comugamers.sentey.core.util.PlaceholderUtil;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class AbuseReportLoginAction implements LoginAction {

    @Inject @Named("abuseipdb")
    private AbuseDatabase abuseDatabase;

    @Inject
    private YamlFile config;

    @Override
    public void handle(LoginContext context, String detection) {
        // Check if the AbuseIPDB integration is enabled
        if(!config.getBoolean("config.integrations.abuseipdb.enabled")) {
            // If not, return
            return;
        }

        // Check if the integration ignores login attempts
        if(config.getBoolean("config.login.actions.abuseipdb.ignore-login")) {
            // If so, return
            return;
        }

        // Get the address as a string
        String addressAsString = context.getHandshakeAddress().getHostAddress();

        // Get the comment
        String comment = PlaceholderUtil.applyPlaceholdersFromLoginContext(
                config.getString("config.login.actions.abuseipdb.comment"), detection, context
        );

        // Report it
        abuseDatabase.reportAddress(addressAsString, comment);
    }
}
