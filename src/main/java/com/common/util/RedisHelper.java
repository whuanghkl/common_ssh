package com.common.util;

import java.util.Map;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import com.common.dict.Const;

/**
 * Created by David on 15/4/1.
 */
public class RedisHelper {

	public static Logger logger = Logger.getLogger(RedisHelper.class);

    private RedisHelper() {//每次从池里取新连接
    }

    public static RedisHelper getInstance() {
        RedisHelper instance = new RedisHelper();

        return instance;
    }

    public void saveCache(String k, String v) {

        Jedis jedis = Const.pool.getResource();

        try{
            jedis.set(k, v);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("saveCache", e);
            Const.pool.returnBrokenResource(jedis);
            jedis = null;
        }finally {
            if (jedis != null) {
                Const.pool.returnResource(jedis);
            }
        }

    }

    /***
     * Only set the key if it does not already exist
     *
     * @param k
     * @param v
     * @param time : second
     */
    public void saveExpxKeyCache(String k, String v, long time) {
        saveExpxKeyCache(k, v, "NX", time);
    }

    /***
     * @param k
     * @param v
     * @param nxxx :  NX|XX, NX -- Only set the key if it does not already exist. XX -- Only set the key
     *             if it already exist.
     * @param time : second
     */
    public void saveExpxKeyCache(String k, String v, String nxxx, long time) {
        Jedis jedis = Const.pool.getResource();
        try {
            jedis.set(k, v, nxxx, "EX"/*seconds*/, time);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("saveKeyCache", e);
            Const.pool.returnBrokenResource(jedis);
            jedis = null;
        } finally {
            if (jedis != null) {
                Const.pool.returnResource(jedis);
            }
        }
    }
    public void saveKeyCache(String id, String k, String v) {

        Jedis jedis = Const.pool.getResource();

        try{
            jedis.hset(id, k, v);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("saveKeyCache", e);
            Const.pool.returnBrokenResource(jedis);
            jedis = null;
        }finally {
            if (jedis != null) {
                Const.pool.returnResource(jedis);
            }
        }
    }

    public void saveAllKeyCache(String id, Map kv) {

        Jedis jedis = Const.pool.getResource();

        try{
            jedis.hmset(id, kv);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("saveAllKeyCache", e);
            Const.pool.returnBrokenResource(jedis);
            jedis = null;
        }finally {
            if (jedis != null) {
                Const.pool.returnResource(jedis);
            }
        }

    }

    public void clearKeyCache(String id, String k) {

        Jedis jedis = Const.pool.getResource();

        try{
            jedis.hdel(id, k);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("clearKeyCache", e);
            Const.pool.returnBrokenResource(jedis);
            jedis = null;
        }finally {
            if (jedis != null) {
                Const.pool.returnResource(jedis);
            }
        }

    }

    public String getCache(String k) {

        Jedis jedis = Const.pool.getResource();
        String v = "";

        try{
            v = jedis.get(k);

        }catch (Exception e){
            e.printStackTrace();
            logger.error("getCache", e);
            Const.pool.returnBrokenResource(jedis);
            jedis = null;
        }finally {
            if (jedis != null) {
                Const.pool.returnResource(jedis);
            }
        }

        return v;
    }

    public String getKeyCache(String id,  String k) {

        Jedis jedis = Const.pool.getResource();
        String v = "";

        try{
            v = jedis.hget(id, k);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("getKeyCache", e);
            Const.pool.returnBrokenResource(jedis);
            jedis = null;
        }finally {
            if (jedis != null) {
                Const.pool.returnResource(jedis);
            }
        }

        return v;
    }

    public Map getAllKeyCache(String id) {

        Jedis jedis = Const.pool.getResource();
        Map v = null;

        try{
            v = jedis.hgetAll(id);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("getAllKeyCache", e);
            Const.pool.returnBrokenResource(jedis);
            jedis = null;
        }finally {
            if (jedis != null) {
                Const.pool.returnResource(jedis);
            }
        }
        return v;

    }

    public void clearCache(String id) {

        Jedis jedis = Const.pool.getResource();

        try{
            jedis.del(id);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("clearCache", e);
            Const.pool.returnBrokenResource(jedis);
            jedis = null;
        }finally {
            if (jedis != null) {
                Const.pool.returnResource(jedis);
            }
        }

    }

}
