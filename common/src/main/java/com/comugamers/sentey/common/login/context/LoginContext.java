package com.comugamers.sentey.common.login.context;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

import java.net.InetAddress;

/**
 * Represents the context of a login attempt.
 * @author Pabszito
 */
public class LoginContext {

    private final Player player;
    private final PlayerLoginEvent rawLoginEvent;
    private final InetAddress spoofedAddress;
    private final InetAddress handshakeAddress;

    public LoginContext(PlayerLoginEvent rawLoginEvent) {
        this(rawLoginEvent.getPlayer(), rawLoginEvent, rawLoginEvent.getAddress(), rawLoginEvent.getRealAddress());
    }

    public LoginContext(Player player, PlayerLoginEvent rawLoginEvent, InetAddress spoofedAddress, InetAddress handshakeAddress) {
        this.player = player;
        this.rawLoginEvent = rawLoginEvent;
        this.spoofedAddress = spoofedAddress;
        this.handshakeAddress = handshakeAddress;
    }

    /**
     * Gets the player that is attempting to log in.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the spoofed address, which is the one sent by the proxy
     * for IP forwarding.
     */
    public InetAddress getSpoofedAddress() {
        return spoofedAddress;
    }

    /**
     * Gets the handshake address, which is the address of the proxy server.
     */
    public InetAddress getHandshakeAddress() {
        return handshakeAddress;
    }

    /**
     * Whether the spoofed IP address is valid or not.
     * @return True if the spoofed IP address is not null.
     */
    public boolean isValidSpoofedAddress() {
        return spoofedAddress != null;
    }

    /**
     * Whether the handshake IP address is valid. Should always be true,
     * but I don't even trust Minecraft anymore.
     * @return True if the handshake IP address is not null.
     */
    public boolean isValidHandshakeAddress() {
        return handshakeAddress != null;
    }

    /**
     * The raw {@link PlayerLoginEvent} that was fired.
     */
    public PlayerLoginEvent getRawLoginEvent() {
        return rawLoginEvent;
    }
}
