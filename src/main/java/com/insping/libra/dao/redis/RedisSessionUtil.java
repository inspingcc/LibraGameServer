package com.insping.libra.dao.redis;

import com.insping.log.LibraLog;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

public class RedisSessionUtil {
    private static class LazyHolder {
        private static final RedisSessionUtil instance = new RedisSessionUtil();
    }

    public static RedisSessionUtil getInstance() {
        return LazyHolder.instance;
    }

    AtomicLong index = new AtomicLong(0);
    private JedisPool jedisPool = null;
    // private JedisResourcePool roundRobinGlobalPool = null;
    private String redisKeyPrefix;
    static int useDBIndex = 0;

    private RedisSessionUtil() {
        try {
            init();
        } catch (Exception e) {
            LibraLog.info("RedisSessionUtil:init redis error :" + e.getMessage());
        }
    }

    private void init() throws Exception {
        if (jedisPool == null) {
            Properties properties = new Properties();
            File file = new File(System.getProperty("user.dir") + "/config/redis.properties");
            properties.load(new FileInputStream(file));
            RedisConfig rc = RedisConfig.load(properties);

            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(rc.getMaxActive());
            config.setMaxIdle(rc.getMaxIdle());
            config.setMaxWaitMillis(rc.getMaxWait());
            config.setTestOnBorrow(rc.isTestOnBorrow());
            // config.setTestOnReturn(testOnReturn);
            useDBIndex = rc.getUseIndex();
            jedisPool = new JedisPool(config, rc.getIp(), rc.getPort(), rc.getTimeOut());
            // redisKeyPrefix = PRODUCT_NAME +
            // String.valueOf(ServiceApp.instanceId);
            // ConstKey.init();
            LibraLog.info("init redis successfully");
        }

    }

    /**
     * Handle jedisException, write log and return whether the connection is
     * broken.
     */
    public static boolean handleJedisException(Exception jedisException) {
        if (jedisException instanceof JedisConnectionException) {
            LibraLog.info("Redis connection " + " lost." + jedisException.getMessage());
        } else if (jedisException instanceof JedisDataException) {
            if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1)) {
                LibraLog.info("Redis connection " + " are read-only slave." + jedisException.getMessage());
            } else {
                return false;
            }
        } else {
            LibraLog.info("Jedis exception happen." + jedisException.getMessage());
        }
        return true;
    }

    public Jedis getResource() {
        Jedis jedis = jedisPool.getResource();
        if (jedis != null)
            index.incrementAndGet();
        return jedis;
    }

    @SuppressWarnings("deprecation")
    public void close(Jedis resource) {
        jedisPool.returnResource(resource);
        index.decrementAndGet();
    }

    @SuppressWarnings("deprecation")
    public void closeBroken(Jedis resource) {
        jedisPool.returnBrokenResource(resource);
        index.decrementAndGet();
    }

    // should call when init game
    public boolean testConnection() throws Exception {
        if (getInstance().getResource() != null) {
            return true;
        } else {
            throw new Exception("Redis init fail!");
        }
    }

    public String getRedisKeyPrefix() {
        return redisKeyPrefix;
    }

    public long getNumActive() {
        return index.get();
    }


}
