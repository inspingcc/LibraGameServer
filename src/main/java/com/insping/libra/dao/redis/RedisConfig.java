package com.insping.libra.dao.redis;

import com.insping.libra.world.LibraConfig;

import java.lang.reflect.Field;
import java.util.Properties;

public class RedisConfig {

    int maxActive;
    int maxIdle;
    long maxWait;
    boolean testOnBorrow;
    String ip;
    int port;
    int timeOut;
    int useIndex;
    int allIndex;

    public static RedisConfig load(Properties properties) throws Exception {
        RedisConfig config = new RedisConfig();
        Field[] fields = RedisConfig.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String str = properties.getProperty(field.getName());
            if (str == null) {
                continue;
            }
            field.setAccessible(true);
            LibraConfig.loadOneProperty(field, str, config);
        }
        return config;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getUseIndex() {
        return useIndex;
    }

    public void setUseIndex(int useIndex) {
        this.useIndex = useIndex;
    }

    public int getAllIndex() {
        return allIndex;
    }

    public void setAllIndex(int allIndex) {
        this.allIndex = allIndex;
    }


}
