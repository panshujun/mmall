package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pan shujun
 * @version V1.0.0
 * Description description
 * @date 2018/12/06 11:24
 */
public class RedisSharded {
    /**
     * Jedis连接池
     * 申明static，保证jedis连接池在tomcat启动的时候加载出来
     */
    private static ShardedJedisPool pool;
    /**
     * 控制jedis连接池和redisServer的最大连接数
     */
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));
    /**
     *最大空闲数，数据库连接的最大空闲时间。超过空闲时间
     * 数据库连接将被标记为不可用，然后被释放。设为0表示无限制
     * 在jedispool中最大的idle状态的jedis实例的个数
     */
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));
    /**
     * 在jedispool中最小的idle状态的jedis实例的个数
     * 是指pool中最少有多少个空闲对象
     */
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));
    /**
     *在borrow一个jedis实例的时候，是否需要进行验证操作
     * 若为true 则表示得到的jedis实例肯定是可以用的
     */
    private static Boolean testOnBorrow = Boolean.valueOf(PropertiesUtil.getProperty("redis.test.borrow","true"));
    /**
     * 在还的时候，是否需要验证操作
     * 在return一个jedis实例的时候，是否需要进行验证操作
     * 若为true 则放回jedisPool实例肯定是可以用的
     */
    private static Boolean testOnReturn = Boolean.valueOf(PropertiesUtil.getProperty("redis.test.return","true"));

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");

    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");

    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));


    // 申明方法
    private static void initPool() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        // 连接耗尽的时候，是否阻塞 false会抛出异常 true阻塞直到超时 会报超时异常默认为true
        jedisPoolConfig.setBlockWhenExhausted(true);
        JedisShardInfo info1 =new JedisShardInfo(redis1Ip,redis1Port,1000*2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip,redis2Port,1000*2);
        List<JedisShardInfo> jedisShardInfoList =new ArrayList<JedisShardInfo>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);
        pool = new ShardedJedisPool(jedisPoolConfig,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);

    }

    // 初始化连接池
    static  {
        initPool();
    }

    /**
     * jedis实例开放
     * @return
     */
    public static ShardedJedis getJedis() {
        return pool.getResource();
    }

    /**
     * 正常放回jedis
     */
    public static void returnResource(ShardedJedis jedis) {
        pool.returnResource(jedis);
    }

    /**
     * 坏连接 jedis
     */
    public static void returnBrokenResource(ShardedJedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
        for (int i=0;i<10;i++){
            jedis.set("Key"+i,"Value"+i);

        }
        returnResource(jedis);
        // 连接池销毁、临时调用
//        pool.destroy();

        System.out.println("program is end ");
    }
}
