package com.mmall.util;

import com.mmall.common.RedisSharded;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

/**
 * @author Pan shujun
 * @version V1.0.0
 * Description description
 * @date 2018/011/28 13:43
 */
@Slf4j
public class RedisShardedPoolUtil {


    /**
     * 有效期
     * 为给定 key 设置有效期，当 key 过期时(生存时间为 0 )，它会被自动删除
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime) {
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisSharded.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("setExkey:{},exTime:{},errorMessage:{}",key ,exTime,e);

            RedisSharded.returnBrokenResource(jedis);

            return result;
        }

        RedisSharded.returnResource(jedis);

        return result;
    }


    /**
     * exTime单位是秒
     * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。
     * 如果 key 已经存在， SETEX 命令将覆写旧值。
     */
    public static String setEX(String key, String value, int exTime) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisSharded.getJedis();
            result = jedis.setex(key, exTime,value);
        } catch (Exception e) {
            log.error("setExkey:{} value:{} exTime:{},errorMessage:{}",key ,value,exTime,e);

            RedisSharded.returnBrokenResource(jedis);

            return result;
        }

        RedisSharded.returnResource(jedis);

        return result;
    }



    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisSharded.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} errorMessage:{}",key ,value,e);

            RedisSharded.returnBrokenResource(jedis);

            return result;
        }

        RedisSharded.returnResource(jedis);

        return result;
    }



    public static String get(String key) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisSharded.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} ,errorMessage:{}",key ,e);

            RedisSharded.returnBrokenResource(jedis);

            return result;
        }

        RedisSharded.returnResource(jedis);

        return result;
    }


    public static Long del(String key) {
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisSharded.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} ,errorMessage:{}",key ,e);

            RedisSharded.returnBrokenResource(jedis);

            return result;
        }

        RedisSharded.returnResource(jedis);

        return result;
    }

    public static void main(String[] args) {
        ShardedJedis jedis = RedisSharded.getJedis();

        RedisShardedPoolUtil.set("keyTest","valueTest");

        String value = RedisShardedPoolUtil.get("keyTest");

        RedisShardedPoolUtil.setEX("keyTest","valueTest",60*10);

        RedisShardedPoolUtil.expire("keyTest",60*20);

        RedisShardedPoolUtil.del("keyTest");

        System.out.println("end");
    }
}
