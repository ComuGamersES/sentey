# Sentey
Protect your Spigot server against IP forwarding exploits, as well as blocking unknown BungeeCord and/or Velocity proxies.

## But firewalls are a thing!
Even though a properly configured firewall is 100 times better than this, having this as a second option in case of a firewall misconfiguration won't hurt anyone (or well, as long as it is configured properly).

Besides, some people may not be able to access or configure their firewall system, so plugins like this are probably the best option for them.

## How it works
When proxies such as BungeeCord have the IP forwarding option enabled, they need to send the IP address of the player to Spigot proxies through the [handshake packet (0x00)](https://wiki.vg/Protocol#Handshake). If they don't, the IP address of the player would be the same as the proxy's for the Spigot server. This introduces a variety of exploits since Spigot does *not* sanitize the IP address. The way this plugin works is by sending the player's spoofed IP address through a variety of filters such as:

- Checking if the IP address is not null or empty
- - Checking if the IP address is malformed
- Checking if the spoofed address is a local, loopback or site local address.

You can find more information on this type of exploits in [this writeup](https://github.com/wodxgod/Griefing-Methods/blob/master/Exploitation/UUID%20Spoofing.md) made by [wodxgod](https://github.com/wodxgod).

The plugin also offers an option for filtering the handshake IP address - which is essentially the IP address of the proxy. By default, it is set to set up mode to prevent blocking all connections to the server. Server administrators may configure this filter by using the `/sentey trusted-proxies` command.

## Taking action
The plugin offers a variety of default actions when a server list ping is received or when an unauthorized connection attempt happens - however, external plugins may register custom actions using the plugin's API.

### Server List Ping
Alerts for server list pings may be sent through a [Discord webhook](https://discord.com) or an in-game message. You can configure those alerts at the `alerts` section:
```yaml
    # The actions taken when a ping is received
    actions:
      alerts:
        # Whether to send a message through the chat to online staff or not
        enabled: true

        # The message sent to online staff
        message: "&5&l>> &fServer list ping received from &c%address%&f, is the firewall properly configured?"
      webhook:
        # Whether to send a message to a Discord webhook or not
        enabled: false

        # Whether to enable TTS or not. If enabled, the message will be read aloud.
        # Probably annoying, not gonna lie. I don't even know why I'm making this an option.
        tts: false

        # The Discord webhook URL
        url: "https://discord.com/api/webhooks/a-valid-id/and-a-real-webhook-token"

        # The message that will be sent
        message: "[`%serverAddress%`] | Server list ping received from `%address%`"
```

### Login
You may run commands, disallow the connection attempt and send alerts through a [Discord webhook](https://discord.com) or an in-game message. You can also configure those alerts at the `alerts` section (like the previous Server List Ping example):
```yaml
    # Actions to take when an unauthorized connection attempt is detected.
    actions:
      disallow-connection:
        # Whether to disallow it or not. Leaving this enabled is recommended.
        enabled: true

        # The message sent to the attacker
        reason: |-
          Your connection has been blocked.
          Please contact the server administrator for more information.
      commands:
        # Whether to run a list of commands or not
        enabled: false

        # The list of commands to run
        list:
          - "minecraft:ban %player% Unauthorized connection attempt"
          - "minecraft:ban-ip %proxyAddress% Unauthorized connection attempt"
      alerts:
        # Whether to send a message through the chat to online players or not
        enabled: true

        # The message sent to online staff
        message: "&5&l>> &fUnauthorized connection attempt made by &c%player%&f. Handshake made from &c%proxyAddress% &7(%detectionType%)"
      webhook:
        # Whether to send a message to a Discord webhook or not
        enabled: true

        # Whether to enable TTS or not. If enabled, the message will be read aloud.
        # Probably annoying, not gonna lie. I don't even know why I'm making this an option.
        tts: false

        # The Discord webhook URL
        url: "https://discord.com/api/webhooks/a-valid-id/and-a-real-webhook-token"

        # The message that will be sent
        message: "[`%serverAddress%`] | Unauthorized connection attempt from `%player%` (UUID: `%uuid%`; real IP address: `%proxyAddress%`; 'spoofed' IP address: `%address%`; detection type: `%detectionType%`)"
```

## Detecting port scans
The plugin also offers a way of detecting server list pings which may be caused by external programs such as [nmap](https://nmap.org/). You can enable it here:
```yaml
  # Server list ping related settings.
  # The server list ping event is normally fired when a player requests the MOTD of the server,
  # but it can also be fired by external programs such as nmap.
  server-list-ping:
    # Whether to enable listening for the server list ping event or not
    enabled: false
```

### Hiding pings from trusted proxies
Ping alerts from trusted proxies are hidden by default - although you can enable them by changing this setting under the `server-list-ping` path:
```yaml
    # List of internal filters
    filters:
      # Whether to ignore trusted proxies. 'true' is recommended.
      ignore-trusted-proxies: true
```

