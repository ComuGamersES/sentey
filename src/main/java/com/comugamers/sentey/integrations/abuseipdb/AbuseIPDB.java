package com.comugamers.sentey.integrations.abuseipdb;

import com.comugamers.sentey.integrations.abuseipdb.categories.AbuseCategory;
import com.comugamers.sentey.integrations.abuseipdb.result.AbuseReportResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.plugin.Plugin;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbuseIPDB {

    private final Plugin plugin;
    private final List<String> onCooldown;
    private String key;

    public AbuseIPDB(Plugin plugin, String key) {
        this.plugin = plugin;
        this.onCooldown = new ArrayList<>();
        this.key = key;
    }

    private JsonObject submitReport(String address, String comment, int... categories) {
        // Check if categories were provided
        if(categories == null || categories.length == 0) {
            // If not, throw an exception
            throw new IllegalArgumentException("No categories were provided!");
        }

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

            // Parse provided categories
            String parsedCategories = StringUtils.join(ArrayUtils.toObject(categories), ",");

            // Add properties
            entity.addProperty("ip", address);
            entity.addProperty("categories", parsedCategories);
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

    /**
     * Reports an IP address to <a href="https://www.abuseipdb.com">AbuseIPDB</a>.
     * @param address The IP address to report.
     * @param comment The comment to include with the report.
     * @deprecated Please provide categories by either using the {@link AbuseCategory} enum or
     *             <a href="https://www.abuseipdb.com/categories">category IDs</a>.
     * @see #reportAddress(String, String, AbuseCategory...)
     * @see #reportAddress(String, String, int...) 
     * @return An {@link AbuseReportResult}, which can be <code>SUCCESS</code> or an error.
     */
    @Deprecated
    public AbuseReportResult reportAddress(String address, String comment) {
        return reportAddress(address, comment, 14, 15);
    }

    /**
     * Reports an IP address to <a href="https://www.abuseipdb.com">AbuseIPDB</a>.
     * @param address The IP address to report.
     * @param comment The comment to include with the report.
     * @param categories An array of {@link AbuseCategory abuse categories}.
     * @see #submitReport(String, String, int...)
     * @return An {@link AbuseReportResult}, which can be <code>SUCCESS</code> or an error.
     */
    public AbuseReportResult reportAddress(String address, String comment, AbuseCategory... categories) {
        return reportAddress(
                address,
                comment,
                Arrays.stream(categories)
                        .mapToInt(AbuseCategory::getId)
                        .toArray()
        );
    }

    /**
     * Reports an IP address to <a href="https://www.abuseipdb.com">AbuseIPDB</a>.
     * @param address The IP address to report.
     * @param comment The comment to include with the report.
     * @param categories A list of <a href="">IDs</a> of each category.
     * @see #reportAddress(String, String, AbuseCategory...)
     * @return An {@link AbuseReportResult}, which can be <code>SUCCESS</code> or an error.
     */
    public AbuseReportResult reportAddress(String address, String comment, int... categories) {
        // Check if the address is on cooldown
        if(onCooldown.contains(address)) {
            // If so, return an "ON_COOLDOWN" result
            return AbuseReportResult.ON_COOLDOWN;
        }

        // Report the address and get the response
        JsonObject jsonObject = submitReport(address, comment, categories);

        // Check if the JSON object is null
        if(jsonObject == null) {
            // If so, return an "ERROR" result
            return AbuseReportResult.ERROR;
        }

        // Set the address on cooldown
        onCooldown.add(address);

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
                // If so, log a warning since it isn't on the cooldown list
                plugin.getLogger().warning(
                        "Unable to report abuse for " + address + ": 'On cooldown, not known locally.'"
                );

                // Return an "ON_COOLDOWN" result
                return AbuseReportResult.ON_COOLDOWN;
            }

            // Check if the request failed because it reached the daily rate limit
            if(statusCode == 429) {
                // If so, return an "RATE_LIMIT_EXCEEDED" result
                return AbuseReportResult.RATE_LIMIT_EXCEEDED;
            }

            // Log a warning
            plugin.getLogger().warning(
                    "Unable to report abuse for " + address + ": 'Request failed with status code "
                            + statusCode + " (" + detail + ")'"
            );

            // Return an "ERROR" result
            return AbuseReportResult.ERROR;
        } else {
            // Schedule a task to remove the address from the cooldown list
            plugin.getServer().getScheduler().runTaskLater(
                    plugin, () -> onCooldown.remove(address), 18000L
            );

            // And return a "SUCCESS" result
            return AbuseReportResult.SUCCESS;
        }
    }

    /**
     * Updates the authorization key being used for making requests.
     * @param key The authorization key as a {@link String}.
     */
    public void updateKey(String key) {
        this.key = key;
    }
}
