package com.comugamers.sentey.common.connection.redis.sentry;

import com.comugamers.sentey.common.connection.Connection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.time.Duration;
import java.util.Set;

public class RedisSentryConnection implements Connection<Jedis> {

    private final String masterName;
    private final Set<String> hosts;
    private final String password;
    private JedisSentinelPool jedisSentinelPool;

    public RedisSentryConnection(String masterName, Set<String> hosts, String password) {
        this.masterName = masterName;
        this.hosts = hosts;
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
            jedisSentinelPool = new JedisSentinelPool(masterName, hosts, jedisConfig);
        } else {
            jedisSentinelPool = new JedisSentinelPool(masterName, hosts, jedisConfig, password);
        }

    }

    @Override
    public Jedis get() {
        synchronized (this) {
            if (jedisSentinelPool == null) {
                connect();

                return get();
            }

            return jedisSentinelPool.getResource();
        }
    }

    @Override
    public void close() {
        jedisSentinelPool.close();
        jedisSentinelPool.destroy();
    }
}
