package com.comugamers.sentey.common.connection;

import java.io.Closeable;

public interface Connection<C> extends Closeable {

    void connect();

    C get();

}
