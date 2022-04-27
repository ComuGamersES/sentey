package com.comugamers.sentey.common.connection.redis.standalone;

import com.comugamers.sentey.common.connection.Connection;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.logging.Level;

public class RedisConnection implements Connection<Jedis> {

    private final String hostname;
    private final int port;
    private final String password;
    private JedisPool jedisPool;

    public RedisConnection(String hostname, int port, String password) {
        this.hostname = hostname;
        this.port = port;
        this.password = password;
    }

    @Override
    public void connect() {

        JedisPoolConfig jedisConfig = new JedisPoolConfig();
        jedisConfig.setMaxIdle(20);
        jedisConfig.setMinIdle(6);
        jedisConfig.setMaxWait(Duration.ofMillis(-1));
        jedisConfig.setMaxTotal(128);
        jedisConfig.setTestWhileIdle(true);
        jedisConfig.setMinEvictableIdleTime(Duration.ofMillis(60000));
        jedisConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(30000));
        jedisConfig.setNumTestsPerEvictionRun(-1);

        if (password == null){
            jedisPool = new JedisPool(jedisConfig, hostname, port, 1700);
        } else {
            jedisPool = new JedisPool(jedisConfig, hostname, port, 1700, password);
        }

    }

    @Override
    public Jedis get() {
        synchronized (this) {
            if (jedisPool == null) {
                connect();

                return get();
            }

            return jedisPool.getResource();
        }
    }

    @Override
    public void close() {
        jedisPool.close();
        jedisPool.destroy();
    }
}
