# Sentey
Protect your Spigot server against IP forwarding exploits, block unknown BungeeCord and Velocity proxies and even 
create a Honeypot if you want.

## Use a firewall if you can!
A firewall is 100 times better than this, so try to use Sentey as a second option in case of an accidental firewall 
misconfiguration or to create a honeypot. Some people may not be able to access or configure their firewall system, 
so plugins like this are probably the best option for them.

## How it works
When proxies such as BungeeCord or Velocity have the IP forwarding option enabled, they need to send the IP address of 
the player to Spigot servers through the [handshake packet](https://wiki.vg/Protocol#Handshake). If they don't, the IP 
address of the player would be the same as the proxy's in the Spigot server. Having the IP forwarding option without a 
proper firewall may sometimes cause bugs on some plugins (especially security-related ones that depend on the player's 
IP address) since Spigot does *not* care if the IP address is valid. Seriously, take a look at this:

![Console output](https://i.imgur.com/LvNbYI4.png)

![EssentialsX](https://i.imgur.com/rutkmoA.png)

The way this plugin works is by sending the player's spoofed IP address through a variety of filters such as:

- Checking if the IP address is not null or empty
- Checking if the IP address is malformed
- Checking if the spoofed address is a local, loopback or site local address.

You can find more information on how the [handshake packet](https://wiki.vg/Protocol#Handshake) is abused on 
[this writeup](https://github.com/wodxgod/Griefing-Methods/blob/master/Exploitation/UUID%20Spoofing.md) made by 
[wodxgod](https://github.com/wodxgod).

The plugin also offers an option for filtering the handshake IP address - which is essentially the IP address of the 
proxy. By default, it is on set up mode to prevent blocking all connections to the server. Administrators may configure 
this filter by using the `/sentey trusted-proxies` command.

## Detecting port scans
The plugin also offers a way of detecting server list pings which may be caused by external programs such as 
[nmap](https://nmap.org/). You can enable it here:
```yaml
  # Server list ping related settings.
  # The server list ping event is normally fired when a player requests the MOTD of the server,
  # but it can also be fired by external programs such as nmap.
  server-list-ping:
    # Whether to enable listening for the server list ping event or not
    enabled: false
```

### Hiding pings from trusted proxies
Ping alerts from trusted proxies are hidden by default - although you can enable them by changing this setting 
under the `server-list-ping` path:
```yaml
    # List of internal filters
    filters:
      # Whether to ignore trusted proxies. 'true' is recommended.
      ignore-trusted-proxies: true
```

## Taking action
The plugin offers a variety of default actions when a server list ping is received or when an unauthorized connection 
attempt happens - however, external plugins may register custom actions using the plugin's API.

### Server List Ping
Alerts for server list pings may be sent through a [Discord webhook](https://discord.com) or an in-game message. You can 
configure those alerts at the `alerts` section:
```yaml
    # The actions taken when a ping is received
    actions:
      alerts:
        # Whether to send a message through the chat to online staff or not
        enabled: true

        # The message sent to online staff
        message: "&b&lSENTEY >> &fServer list ping received from &c%address%&f, is the firewall properly configured?"
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
You may run commands, disallow the connection attempt and send alerts through a [Discord webhook](https://discord.com) 
or an in-game message. You can also configure those alerts at the `alerts` section (like the previous Server List Ping 
example):
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
        message: "&b&lSENTEY >> &fUnauthorized connection attempt made by &c%player%&f. Handshake made from &c%proxyAddress% &7(%detectionType%)"
      webhook:
        # Whether to send a message to a Discord webhook or not
        enabled: false

        # Whether to enable TTS or not. If enabled, the message will be read aloud.
        # Probably annoying, not gonna lie. I don't even know why I'm making this an option.
        tts: false

        # The Discord webhook URL
        url: "https://discord.com/api/webhooks/a-valid-id/and-a-real-webhook-token"

        # The message that will be sent
        message: "[`%serverAddress%`] | Unauthorized connection attempt from `%player%` (UUID: `%uuid%`; real IP address: `%proxyAddress%`; 'spoofed' IP address: `%address%`; detection type: `%detectionType%`)"
```

## Developer API
The plugin's API is fairly simple. To get started, get the plugin's main instance:
```java
Sentey sentey = (Sentey) Bukkit.getServer().getPluginManager().getPlugin("Sentey");
```

### Ping actions
You may create a new ping action by creating a class that implements the `PingAction` interface:
```java
public class MyPingAction implements PingAction {
    
    @Override
    public void handle(InetAddress address) {
        // For example, print the address to the console
        // Please use JavaPlugin#getLogger() on an actual implementation
        Bukkit.getLogger().info("Ping from " + address.getHostAddress());
    }
}
```

You may control registered ping actions by using the following methods:
```java
// Using this as an example:
PingAction action = new MyPingAction();

// Adds a ping action
sentey.addPingAction(action);

// Removes a ping action - kind of unsafe, but you can do it if you want
sentey.removePingAction(action);

// Checks if a ping action is registered
if(sentey.isPingActionRegistered(action)) {
    Bukkit.getLogger().info("Yes, it is!");
} else {
    Bukkit.getLogger().info("No, it is not.");
}
        
// Gets the raw list of login actions
List<PingAction> loginActions = sentey.getPingActions();
```

### Ping filters
You may create a new ping filter by creating a class that implements the `PingFilter` interface:
```java
public class MyPingFilter implements PingFilter {

    @Override
    public boolean isClean(InetAddress address) {
        // For example, check for loopback addresses
        if (address.isLoopbackAddress()) {
            // If the address is a loopback address, return false
            return false;
        }

        // The address is clean!
        return true;
    }
}
```

You may control registered ping filters by using the following methods:
```java
// Using this as an example:
PingFilter filter = new MyPingFilter();

// Adds a ping filter
sentey.addPingFilter(filter);

// Removes a ping filter - kind of unsafe, but you can do it if you want
sentey.removePingFilter(filter);

// Checks if a ping filter is registered
if(sentey.isPingFilterRegistered(filter)) {
    Bukkit.getLogger().info("Yes, it is!");
} else {
    Bukkit.getLogger().info("No, it is not.");
}

// Gets the raw list of ping filters
List<PingFilter> pingFilters = sentey.getPingFilters();
```

### Login filters
For login filters, you may create a class that implements the `LoginFilter` interface:
```java
public class MyLoginFilter implements LoginFilter {

    @Override
    public String getName() {
        return "MY_CUSTOM_LOGIN_FILTER"; // I recommend using a format like "MALFORMED_ADDRESS_STRING" for those
    }

    @Override
    public boolean isClean(LoginContext context) {
        // For example, check for loopback addresses
        if (address.isLoopbackAddress()) {
            // If the address is a loopback address, return false to deny the login attempt
            return false;
        }

        // If everything is okay, return true to allow the login attempt
        return true;
    }
}
``` 

You may control registered login filters by using the following methods:
```java
// Using this as an example:
LoginFilter filter = new MyLoginFilter();

// Adds a login filter
sentey.addLoginFilter(filter);

// Removes a login filter - kind of unsafe, but you can do it if you want
sentey.removeLoginFilter(filter);

// Checks if a login filter is registered
if(sentey.isLoginFilterRegistered(filter)) {
    Bukkit.getLogger().info("Yes, it is!");
} else {
    Bukkit.getLogger().info("No, it is not.");
}
        
// Gets the raw list of login filters
List<LoginFilter> loginFilters = sentey.getLoginFilters();
```

### Login actions
You may create a new login action by creating a class that implements the `LoginAction` interface:
```java
public class MyLoginAction implements LoginAction {

    @Override
    public void handle(LoginContext context, String detection) {
        // For example, print the address to the console. 
        // Please use JavaPlugin#getLogger() on an actual implementation
        Bukkit.getLogger().info("----- My custom login action! -----")
        Bukkit.getLogger().info("Unauthorized login attempt detected by filter " + detection);
        Bukkit.getLogger().info("Spoofed address: " + context.isValidSpoofedAddress() ? context.getSpoofedAddress().getHostAddress() : "null");
        Bukkit.getLogger().info("Handshake address:" + context.getHandshakeAddress().getHostAddress());
        Bukkit.getLogger().info("Player name: " + context.getPlayer().getName());
        Bukkit.getLogger().info("-----------------------------------");
    }
}
```

You may control registered login actions by using the following methods:
```java
// Using this as an example:
LoginAction action = new MyLoginAction();

// Adds a login action
sentey.addLoginAction(action);

// Removes a login action - kind of unsafe, but you can do it if you want
sentey.removeLoginAction(action);

// Checks if a login action is registered
if(sentey.isLoginActionRegistered(action)) {
    Bukkit.getLogger().info("Yes, it is!");
} else {
    Bukkit.getLogger().info("No, it is not.");
}
        
// Gets the raw list of login actions
List<LoginAction> loginActions = sentey.getLoginActions();
```
