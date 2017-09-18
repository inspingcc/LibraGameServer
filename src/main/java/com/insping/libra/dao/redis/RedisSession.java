package com.insping.libra.dao.redis;

import com.insping.log.LibraLog;
import redis.clients.jedis.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class RedisSession {

    protected Jedis jedis = null;
    protected boolean autoClose;
    protected RedisConnectionMode mode;

    public enum RedisConnectionMode {
        NOT_POOL, POOL, CODIS
    }

    public RedisSession() {
        this(true);
    }

    public RedisSession(boolean autoClose) {
        jedis = RedisSessionUtil.getInstance().getResource();
        this.autoClose = autoClose;
        mode = RedisConnectionMode.POOL;
        jedis.select(RedisSessionUtil.useDBIndex);
    }

    public Jedis getJedis() {
        return jedis;
    }

    public boolean isAutoClose() {
        return autoClose;
    }

    public RedisConnectionMode getMode() {
        return mode;
    }

    public void close() {
        if (mode == RedisConnectionMode.NOT_POOL || mode == RedisConnectionMode.CODIS) {
            jedis.close();
        } else if (mode == RedisConnectionMode.POOL) {
            try {
                RedisSessionUtil.getInstance().close(jedis);
            } catch (Exception e) {
                RedisSessionUtil.getInstance().closeBroken(jedis);
                LibraLog.info("RedisSession-close" + e.getMessage());
            }
        }
    }

    public String info(String str) {
        StringBuffer sb = new StringBuffer().append("\n");
        sb.append("=======================redis info=========================\n");
        sb.append(str).append("\n");
        sb.append(jedis.info());
        sb.append("=======================redis info=========================\n");
        // LogManager.redisLog(sb.toString());
        return sb.toString();
    }

    public String set(String key, String field) {
        try {
            String ret = jedis.set(key, field);
            // 增加统计
            info("<set>key=" + key + "|field=" + field);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public String setEx(String key, int seconds, String value) {
        try {
            String ret = jedis.setex(key, seconds, value);
            // 增加统计
            info("<setEx>key=" + key + "|seconds=" + seconds + "|field=" + value);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    // 设置失效时间
    public long keyLife(String key, int seconds) {
        try {
            long ret = jedis.expire(key, seconds);
            // 增加统计
            info("<keyLife>key=" + key + "|seconds=" + seconds);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public String set(byte[] key, byte[] field) {
        try {
            String ret = jedis.set(key, field);
            // 增加统计
            info("<set>key=" + key + "|field=" + field);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public int hsetnx(String key, String field, String value) {
        try {
            long result = jedis.hsetnx(key, field, value);
            // 增加统计
            info("<hsetnx>key=" + key + "|field=" + field + "|value=" + value);
            return (int) result;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public Set<String> sMember(String key) {
        try {
            Set<String> set = jedis.smembers(key);
            // 增加统计
            info("<sMember>key=" + key);
            return set;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long sRem(String key, String... value) {
        try {
            // 增加统计
            info("<sRem>key=" + key);
            return jedis.srem(key, value);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public boolean sIsMember(String key, String member) {
        try {
            // 增加统计
            info("<sIsMember>key=" + key + "|member=" + member);
            return jedis.sismember(key, member);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long sAdd(String key, String... value) {
        try {
            // 增加统计
            info("<sAdd>key=" + key + "|value=" + value.length);
            return jedis.sadd(key, value);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void del(byte[] key) {
        try {
            // 增加统计
            info("<del>key=" + key.length);
            jedis.del(key);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void del(String... keys) {
        try {
            // 增加统计
            info("<del>key=" + keys.length);
            jedis.del(keys);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void del(String key) {
        try {
            // 增加统计
            info("<del>key=" + key);
            jedis.del(key);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long lPush(byte[] key, byte[] value) {
        long ret = 0;
        try {
            ret = jedis.lpush(key, value);
            // 增加统计
            info("<lPush>key=" + key.length + "|value=" + value.length);
        } catch (Exception e) {
            RedisSessionUtil.getInstance().closeBroken(jedis);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return ret;
    }

    public long lPush(String key, String value) {
        long ret = 0;
        try {
            ret = jedis.lpush(key, value);
            // 增加统计
            info("<lPush>key=" + key + "|value=" + value);
        } catch (Exception e) {
            RedisSessionUtil.getInstance().closeBroken(jedis);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return ret;
    }

    public List<String> lRange(String key, long start, long end) {
        List<String> ret = null;
        try {
            ret = jedis.lrange(key, start, end);
            // 增加统计
            info("<lRange>key=" + key + "|start=" + start + "|end=" + end);
        } catch (Exception e) {
            RedisSessionUtil.getInstance().closeBroken(jedis);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return ret;
    }

    public String rPop(String key) {
        String ret = null;
        try {
            ret = jedis.rpop(key);
            // 增加统计
            info("<rPop>key=" + key);
        } catch (Exception e) {
            RedisSessionUtil.getInstance().closeBroken(jedis);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return ret;
    }

    public String lPop(String key) {
        String ret = null;
        try {
            ret = jedis.lpop(key);
            // 增加统计
            info("<lPop>key=" + key);
        } catch (Exception e) {
            RedisSessionUtil.getInstance().closeBroken(jedis);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return ret;
    }

    public String rPoplPush(String skey, String dkey) {
        String ret = null;
        try {
            ret = jedis.rpoplpush(skey, dkey);
            // 增加统计
            info("<rPoplPush>key=" + skey + "|dkey=" + dkey);
        } catch (Exception e) {
            RedisSessionUtil.getInstance().closeBroken(jedis);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return ret;
    }

    public void lRem(byte[] key, int count, byte[] field) {
        try {
            jedis.lrem(key, count, field);
            // 增加统计
            info("<lRem>key=" + key.length + "|count=" + count + "|field=" + field.length);
        } finally {
            if (autoClose)
                close();
        }
    }

    public Set<String> keys(String pattern) {
        Set<String> ret;
        try {
            ret = jedis.keys(pattern);
            // 增加统计
            info("<keys>pattern=" + pattern);
        } finally {
            if (autoClose)
                close();
        }
        return ret;
    }

    public String lIndex(String key, long count) {
        String ret;
        try {
            ret = jedis.lindex(key, count);
            // 增加统计
            info("<lIndex>key=" + key + "|count=" + count);
        } finally {
            if (autoClose)
                close();
        }
        return ret;
    }

    public long rPush(String key, String value) {
        long ret = 0;
        try {
            ret = jedis.rpush(key, value);
            // 增加统计
            info("<rPush>key=" + key + "|value=" + value);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return ret;
    }

    public String get(String key) {
        try {
            String ret = jedis.get(key);
            // 增加统计
            info("<get>key=" + key);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public byte[] get(byte[] key) {
        try {
            byte[] ret = jedis.get(key);
            // 增加统计
            info("<get>key=" + key.length);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long incrBy(String key, long incValue) {
        try {
            long ret = jedis.incrBy(key, incValue);
            // 增加统计
            info("<incrBy>key=" + key + "|incValue=" + incValue);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long incr(String key) {
        try {
            long ret = jedis.incr(key);
            // 增加统计
            info("<incrBy>key=" + key);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long decr(String key) {
        try {
            long ret = jedis.decr(key);
            // 增加统计
            info("<decr>key=" + key);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public byte[] hGet(byte[] key, byte[] field) {
        try {
            byte[] ret = jedis.hget(key, field);
            // 增加统计
            info("<hGet>key=" + key.length + "|field=" + field.length);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public String hGet(String key, String field) {
        try {
            String ret = jedis.hget(key, field);
            // 增加统计
            info("<hGet>key=" + key + "|field=" + field);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public List<String> hMGet(String key, String... field) {
        try {
            List<String> ret = jedis.hmget(key, field);
            // 增加统计
            info("<hMGet>key=" + key + "|field=" + field.length);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public List<String> hMGet(String key, List<String> fields) {
        try {
            String[] arr = new String[fields.size()];
            int i = 0;
            for (String item : fields) {
                arr[i++] = item;
            }
            List<String> ret = jedis.hmget(key, arr);
            // 增加统计
            info("<hMGet>key=" + key + "|field=" + fields.size());
            return ret;
        } finally {
            if (autoClose) {
                close();
            }

        }
    }

    public Map<byte[], byte[]> hMGetAll(byte[] key) {
        try {
            Map<byte[], byte[]> ret = jedis.hgetAll(key);
            // 增加统计
            info("<hMGetAll>key=" + key);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public Map<String, String> hMGetAll(String key) {
        try {
            Map<String, String> ret = jedis.hgetAll(key);
            // 增加统计
            info("<hMGetAll>key=" + key);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void setExpireTime(String key, int seconds) {
        try {
            jedis.expire(key, seconds);
            // 增加统计
            info("<setExpireTime>key=" + key + "|seconds=" + seconds);
        } finally {
            if (autoClose) {
                close();
            }

        }
    }

    public String hMSet(String key, String nestedKey, String value) {
        Map<String, String> map = new HashMap<>();
        try {
            map.put(nestedKey, value);
            String ret = jedis.hmset(key, map);
            // 增加统计
            info("<hMSet>key=" + key + "|nestedKey=" + nestedKey + "|value=" + value);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public String hMSet(String key, Map<String, String> map) {
        try {
            String ret = jedis.hmset(key, map);
            // 增加统计
            info("<hMSet>key=" + key + "|map=" + map.size());
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void mset(Map<String, String> map) {
        if (map == null || map.size() <= 0) {
            return;
        }
        String keyValues[] = new String[map.size() * 2];
        int index = 0;
        for (Map.Entry<String, String> mapEntry : map.entrySet()) {
            keyValues[index++] = mapEntry.getKey();
            keyValues[index++] = mapEntry.getValue();
        }
        try {
            jedis.mset(keyValues);
            // 增加统计
            info("<mset>map=" + map.size());
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public List<String> mget(String... keys) {
        List<String> lst = new ArrayList<String>();
        try {
            lst = jedis.mget(keys);
            // 增加统计
            info("<mget>map=" + keys.length);
        } catch (Exception e) {
            LibraLog.info("mget" + e.getMessage());
        } finally {
            if (autoClose) {
                close();
            }
        }
        return lst;
    }

    public void mset(byte[]... keysvalues) {
        try {
            jedis.mset(keysvalues);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long hDel(String key, List<String> fields) {
        // 增加统计
        info("<hDel>key=" + key + "|fields=" + fields.size());
        return hDel(key, fields.toArray(new String[fields.size()]));
    }

    public long hDel(String key, String... fields) {
        try {
            long ret = jedis.hdel(key, fields);
            // 增加统计
            info("<hDel>key=" + key + "|fields=" + fields.length);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long hSet(byte[] key, byte[] field, byte[] value) {
        try {
            long ret = jedis.hset(key, field, value);
            // 增加统计
            info("<hSet>key=" + key.length + "|field=" + field.length + "|value=" + value.length);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long hSet(String key, String field, String value) {
        try {
            long ret = jedis.hset(key, field, value);
            // 增加统计
            info("<hSet>key=" + key + "|field=" + field + "|value=" + value);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public Set<byte[]> hKeys(byte[] key) {
        Set<byte[]> fields = new HashSet<>();
        try {
            fields = jedis.hkeys(key);
            // 增加统计
            info("<hKeys>key=" + key.length);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return fields;
    }

    public Set<String> hKeys(String key) {
        Set<String> fields = new HashSet<>();
        try {
            fields = jedis.hkeys(key);
            // 增加统计
            info("<hKeys>key=" + key);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return fields;
    }

    public long hLen(String key) {
        try {
            long length = jedis.hlen(key);
            // 增加统计
            info("<hLen>key=" + key);
            return length;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public boolean hExists(String key, String field) {
        try {
            boolean ret = jedis.hexists(key, field);
            // 增加统计
            info("<hExists>key=" + key + "|field=" + field);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long pushListFromLeft(String key, String... members) {
        try {
            long ret = jedis.lpush(key, members);
            // 增加统计
            info("<pushListFromLeft>key=" + key + "|members=" + members.length);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public String lSet(String key, long index, String members) {
        try {
            String ret = jedis.lset(key, index, members);
            // 增加统计
            info("<lSet>key=" + key + "|index=" + index + "|members=" + members);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public List<String> getRangeList(String key, int start, int end) {
        try {
            List<String> retList = jedis.lrange(key, start, end);
            // 增加统计
            info("<getRangeList>key=" + key + "|start=" + start + "|end=" + end);
            return retList;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long getLength(String key) {
        try {
            long length = jedis.llen(key);
            // 增加统计
            info("<getLength>key=" + key);
            return length;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public List<byte[]> getRangeList(byte[] key, int start, int end) {
        try {
            List<byte[]> retList = jedis.lrange(key, start, end);
            // 增加统计
            info("<getRangeList>key=" + key.length + "|start=" + start + "|end=" + end);
            return retList;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void lRem(String key, int count, String value) {
        try {
            jedis.lrem(key, count, value);
            // 增加统计
            info("<lRem>key=" + key + "|count=" + count + "|value=" + value);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void lTrim(String key, int start, int end) {
        try {
            jedis.ltrim(key, start, end);
            // 增加统计
            info("<lTrim>key=" + key + "|start=" + start + "|end=" + end);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public String deleteListFromRight(String key) {
        try {
            String retList = jedis.rpop(key);
            // 增加统计
            info("<deleteListFromRight>key=" + key);
            return retList;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long hincrBy(String key, String field, long num) {
        try {
            // 增加统计
            info("<hincrBy>key=" + key + "|field=" + field + "|num=" + num);
            return jedis.hincrBy(key, field, num);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long zAdd(String key, String member, long score) {
        try {
            // 增加统计
            info("<zAdd>key=" + key + "|member=" + member + "|score=" + score);
            return jedis.zadd(key, (double) score, member);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long zAddMember(String key, Map<String, Double> scoreMembers) {
        try {
            // 增加统计
            info("<zAddMember>key=" + key + "|scoreMembers=" + scoreMembers.size());
            return jedis.zadd(key, scoreMembers);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public HashMap<String, Double> zRangByAsc(String key, long start, long end) {
        try {
            HashMap<String, Double> ret = new HashMap<>();
            Set<Tuple> result = jedis.zrangeWithScores(key, start, end);
            if (result != null && result.size() > 0) {
                for (Tuple member : result) {
                    ret.put(member.getElement(), member.getScore());
                }
            }
            // 增加统计
            info("<zRangByAsc>key=" + key + "|start=" + start + "|end=" + end);
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public Set<Tuple> zRevrangeByAscSet(String key, long start, long end) {
        try {
            // HashMap<String, Double> ret = new HashMap<>();
            // Set<Tuple> result = jedis.zrangeWithScores(key, start, end);
            Set<Tuple> result = jedis.zrevrangeWithScores(key, start, end);
            // if (result != null && result.size() > 0) {
            // for (Tuple member : result) {
            // ret.put(member.getElement(), member.getScore());
            // }
            // }
            // 增加统计
            info("<zRangByAsc>key=" + key + "|start=" + start + "|end=" + end);
            return result;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void subscribe(JedisPubSub listener, List<String> channels) {
        try {
            // 增加统计
            info("<subscribe>listener=" + listener + "|channels=" + channels.size());
            jedis.subscribe(listener, channels.toArray(new String[]{}));
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long publish(String channel, String msg) {
        try {
            // 增加统计
            info("<publish>channel=" + channel + "|msg=" + msg);
            return jedis.publish(channel, msg);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    public void setMode(RedisConnectionMode mode) {
        this.mode = mode;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public static void clearRedis(String key) {
        LibraLog.info("<REDISTIME-clearRedis>开始=(" + (System.currentTimeMillis()) + ")");
        RedisSession jedis = new RedisSession(false);
        long now = System.currentTimeMillis();
        try {
            LibraLog.info("REDIS-kartKeys=" + now);
            Set<String> kartKeys = jedis.keys(key + "*");
            LibraLog.info("REDIS-kartKeys=" + kartKeys.size());
            if (kartKeys != null && kartKeys.size() > 0) {
                jedis.del((String[]) kartKeys.toArray(new String[kartKeys.size()]));
            }
            LibraLog.info("REDIS-clearKartRedis-allclear");
        } catch (Exception e) {
            LibraLog.info("REDIS-clearRedis" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
            LibraLog.info("<REDISTIME-clearRedis>time=(" + (System.currentTimeMillis() - now) + ")毫秒");
        }
    }

    public Map<String, String> getBatch(Collection<String> list) {
        return null;
        /*
		 * Map<String, String> result = new HashMap<>(); Map<String,
		 * Response<String>> responses = new HashedMap(); try { Pipeline
		 * pipeline = jedis.pipelined(); for (String l : list) { String key =
		 * redisKey.suffix(l); responses.put(l, pipeline.get(key)); }
		 * pipeline.sync(); for (String key : responses.keySet()) { String v =
		 * responses.get(key).get(); if (StringUtils.isNotBlank(v)) {
		 * result.put(key, v); } } } finally { if (autoClose) { jedis.close(); }
		 * } return result;
		 */
    }

    public void sBatch(byte[]... keysvalues) {
        try {
            // 使用pipeline hmset
            Pipeline p = jedis.pipelined();
            p.mset(keysvalues);
            p.sync();
        } catch (Exception e) {
            LibraLog.info("sBatch -error");
        } finally {
            if (autoClose) {
                jedis.close();
            }
        }
    }

    public void sBatch(String... keysvalues) {
        try {
            // 使用pipeline hmset
            Pipeline p = jedis.pipelined();
            p.mset(keysvalues);
            p.sync();
        } catch (Exception e) {
            LibraLog.info("sBatch -error");
        } finally {
            if (autoClose) {
                jedis.close();
            }
        }
    }

    public void hmBatch(byte[] key, Map<byte[], byte[]> data) {
        try {
            // 使用pipeline hmset
            Pipeline p = jedis.pipelined();
            p.hmset(key, data);
            p.sync();
        } catch (Exception e) {
            LibraLog.info("hmBatch -error");
        } finally {
            if (autoClose) {
                jedis.close();
            }
        }
    }

    public void hmBatch(String key, Map<String, String> data) {
        try {
            // 使用pipeline hmset
            Pipeline p = jedis.pipelined();
            p.hmset(key, data);
            p.sync();
        } catch (Exception e) {
            LibraLog.info("hmBatch -error");
        } finally {
            if (autoClose) {
                jedis.close();
            }
        }
    }

    public void hmDel(String key, String... field) {
        try {
            // 使用pipeline hmset
            Pipeline p = jedis.pipelined();
            p.hdel(key, field);
            p.sync();
        } catch (Exception e) {
            LibraLog.info("hmDel -error");
        } finally {
            if (autoClose) {
                jedis.close();
            }
        }
    }

    public Map<String, Response<Map<String, String>>> hgBatch(String... keys) {
        Map<String, Response<Map<String, String>>> responses = new HashMap<String, Response<Map<String, String>>>(
                keys.length);
        try {
            // 使用pipeline hmset
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                responses.put(key, p.hgetAll(key));
            }
            p.sync();

        } catch (Exception e) {
            LibraLog.info("hgBatch -error");
        } finally {
            if (autoClose) {
                jedis.close();
            }
        }
        return responses;
    }

    // public SqlData getSqlData(String key) {
    // SqlData data = null;
    // byte[] bytes = null;
    // try {
    // bytes = jedis.get(key.getBytes("utf-8"));
    // data = (SqlData) unserialize(bytes);
    // info("<getSqlData>key=" + key + "| data =" + (data == null ? null :
    // "exist!"));
    // } catch (Exception e) {
    // LibraLog.info("REDIS-getSqlData", e);
    // } finally {
    // if (autoClose)
    // close();
    // }
    //
    // return data;
    // }
    //
    // public void putSqlData(String key, SqlData data) {
    // try {
    // if (data == null)
    // return;
    // byte[] bytes = serialize(data);
    // jedis.set(key.getBytes("utf-8"), bytes);
    // info("<putSqlData>key=" + key);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
