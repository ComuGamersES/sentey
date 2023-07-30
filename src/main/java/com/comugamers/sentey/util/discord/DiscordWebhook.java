package com.comugamers.sentey.util.discord;

import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import static com.comugamers.sentey.Sentey.getCurrentVersion;

/**
 * Represents a <a href="https://discord.com">Discord</a> webhook.
 */
public class DiscordWebhook {

    private final String url;
    private String content;
    private boolean tts;

    /**
     * Constructs a new instance of this class.
     *
     * @param url The webhook URL from <a href="https://discord.com">Discord</a>.
     */
    public DiscordWebhook(String url) {
        this.url = url;
    }

    /**
     * Sets the message that will be sent through the webhook.
     *
     * @param content The message content as a {@link String}.
     * @return The current instance of this class.
     */
    public DiscordWebhook setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Enables or disables TTS (text-to-speech)
     *
     * @param tts Whether TTS should be enabled or not.
     * @return The current instance of this class.
     */
    public DiscordWebhook setTTS(boolean tts) {
        this.tts = tts;
        return this;
    }

    /**
     * Sends the provided values through the webhook.
     *
     * @throws IOException if an error occurs while
     */
    public void execute() throws IOException {
        if (this.content == null) {
            throw new IllegalArgumentException("No message content was provided!");
        }

        JsonObject json = new JsonObject();
        json.addProperty("content", this.content);
        json.addProperty("tts", this.tts);

        URL url = new URL(this.url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "sentey/" + getCurrentVersion());
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStream stream = connection.getOutputStream();
        stream.write(json.toString().getBytes());
        stream.flush();
        stream.close();

        connection.getInputStream().close();
        connection.disconnect();
    }
}