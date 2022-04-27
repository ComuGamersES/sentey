package com.comugamers.sentey.common.report.abuseipdb;

import com.comugamers.sentey.common.report.AbuseDatabase;
import com.comugamers.sentey.common.report.result.AbuseReportResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.plugin.Plugin;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AbuseIPDB implements AbuseDatabase {

    private final Plugin plugin;
    private final List<String> onCooldown;
    private String key;

    public AbuseIPDB(Plugin plugin, String key) {
        this.plugin = plugin;
        this.onCooldown = new ArrayList<>();
        this.key = key;
    }

    private JsonObject submitReport(String address, String comment) {
        // Create a new Closeable HTTP client
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Create a new HTTP post request
            HttpPost request = new HttpPost("https://api.abuseipdb.com/api/v2/report");

            // Set headers
            request.addHeader("Key", key);
            request.addHeader("Content-Type", "application/json");
            request.addHeader("User-Agent", "ComuGamers Sentey");

            // Create a new JSON object
            JsonObject entity = new JsonObject();

            // Add properties
            entity.addProperty("ip", address);
            entity.addProperty("categories", "14,15");
            entity.addProperty("comment", comment);

            // Create a new StringEntity using the previously created JSON object
            StringEntity stringEntity = new StringEntity(entity.toString());

            // Set it as the entity of the request
            request.setEntity(stringEntity);

            // Execute the request
            HttpResponse response = httpClient.execute(request);

            // Create a new JSON parser
            try (InputStream inputStream = response.getEntity().getContent()) {
                // Create a new String Writer
                String rawResponse = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

                // Return the response as a JSON object
                return new JsonParser()
                        .parse(rawResponse)
                        .getAsJsonObject();
            }
        } catch (Exception ex) {
            // Log an user-friendly error description
            plugin.getLogger().severe(
                    "An error occurred while reporting an address to AbuseIPDB (is the API key valid?):"
            );

            // Print the stack trace
            ex.printStackTrace();

            // Return null
            return null;
        }
    }

    @Override
    public AbuseReportResult reportAddress(String address, String comment) {
        // Check if the address is on cooldown
        if(onCooldown.contains(address)) {
            // If so, return an "ON_COOLDOWN" result
            return AbuseReportResult.ON_COOLDOWN;
        }

        // Report the address and get the response
        JsonObject jsonObject = submitReport(address, comment);

        // Check if the JSON object is null
        if(jsonObject == null) {
            // If so, return an "ERROR" result
            return AbuseReportResult.ERROR;
        }

        // Check if the JSON contains an "errors" property
        if(jsonObject.has("errors")) {
            // Cancel cooldown
            onCooldown.remove(address);

            // If not, get the first JSON element of the array
            JsonElement element = jsonObject.get("errors")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject();

            // Get the status code
            int statusCode = element.getAsJsonObject()
                    .get("status")
                    .getAsInt();

            // Get the details
            String detail = element.getAsJsonObject()
                    .get("detail")
                    .getAsString();

            // Check if the request failed because of cooldown
            if (statusCode == 403) {
                // If so, return an "ON_COOLDOWN" result
                return AbuseReportResult.ON_COOLDOWN;
            }

            // Check if the request failed because it reached the daily rate limit
            if(statusCode == 429) {
                // If so, return an "RATE_LIMIT_EXCEEDED" result
                return AbuseReportResult.RATE_LIMIT_EXCEEDED;
            }

            // Log a warning
            plugin.getLogger().warning(
                    "Unable to report abuse for " + address + ": Request failed with status code "
                            + statusCode + " (" + detail + ")"
            );

            // Return an "ERROR" result
            return AbuseReportResult.ERROR;
        } else {
            // Set the address on cooldown
            onCooldown.add(address);

            // Schedule a task to remove the address from the cooldown list
            plugin.getServer().getScheduler().runTaskLater(
                    plugin, () -> onCooldown.remove(address), 900 * 20L
            );

            // And return a "SUCCESS" result
            return AbuseReportResult.SUCCESS;
        }
    }

    @Override
    public void updateKey(String key) {
        this.key = key;
    }
}
