package com.comugamers.sentey.common;

public interface Service {

    void start();

    default void stop() {}
}
