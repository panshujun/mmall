package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;


/**
 * @author Pan shujun
 * @version V1.0.0
 * Description description
 * @date 2018/011/28 13:43
 */
@Slf4j
public class RedisPoolUtil {


    /**
     * 有效期
     * 为给定 key 设置有效期，当 key 过期时(生存时间为 0 )，它会被自动删除
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime) {
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("setExkey:{},exTime:{},errorMessage:{}",key ,exTime,e);

            RedisPool.returnBrokenResource(jedis);

            return result;
        }

        RedisPool.returnResource(jedis);

        return result;
    }


    /**
     * exTime单位是秒
     * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。
     * 如果 key 已经存在， SETEX 命令将覆写旧值。
     */
    public static String setEX(String key, String value, int exTime) {
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, exTime,value);
        } catch (Exception e) {
            log.error("setExkey:{} value:{} exTime:{},errorMessage:{}",key ,value,exTime,e);

            RedisPool.returnBrokenResource(jedis);

            return result;
        }

        RedisPool.returnResource(jedis);

        return result;
    }



    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} errorMessage:{}",key ,value,e);

            RedisPool.returnBrokenResource(jedis);

            return result;
        }

        RedisPool.returnResource(jedis);

        return result;
    }



    public static String get(String key) {
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} ,errorMessage:{}",key ,e);

            RedisPool.returnBrokenResource(jedis);

            return result;
        }

        RedisPool.returnResource(jedis);

        return result;
    }


    public static Long del(String key) {
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} ,errorMessage:{}",key ,e);

            RedisPool.returnBrokenResource(jedis);

            return result;
        }

        RedisPool.returnResource(jedis);

        return result;
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();

        RedisPoolUtil.set("keyTest","valueTest");

        String value = RedisPoolUtil.get("keyTest");

        RedisPoolUtil.setEX("keyTest","valueTest",60*10);

        RedisPoolUtil.expire("keyTest",60*20);

        RedisPoolUtil.del("keyTest");

        System.out.println("end");
    }
}
