package com.comugamers.sentey.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {

    private static final String SPIGOT_API_URL = "https://api.spigotmc.org/legacy/update.php?resource=";
    private final int resourceId;
    private final int timeout;

    public UpdateChecker(int resourceId) {
        this(resourceId, 1250);
    }

    public UpdateChecker(int resourceId, int timeout) {
        this.resourceId = resourceId;
        this.timeout = timeout;
    }

    public String fetchLatest() throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(SPIGOT_API_URL + resourceId).openConnection();
        con.setConnectTimeout(timeout);
        con.setReadTimeout(timeout);

        InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
        return new BufferedReader(inputStreamReader).readLine();
    }
}
