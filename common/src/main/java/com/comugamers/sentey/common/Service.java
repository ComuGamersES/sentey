package com.comugamers.sentey.common;

/**
 * Represents a service, which may be from a Bukkit plugin or even
 * from a completely unrelated type of project.
 * @author Pabszito
 */
public interface Service {

    /**
     * Starts the specified {@link Service}.
     */
    void start();

    /**
     * Stops the specified {@link Service}.
     */
    default void stop() {}
}
