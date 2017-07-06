package com.common.util;

import com.common.dict.Const;
import com.string.widget.util.ValueWidget;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

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
        if (ValueWidget.isNullOrEmpty(v)) {
            return;
        }
        if (null == Const.pool) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = Const.pool.getResource();
        } catch (redis.clients.jedis.exceptions.JedisConnectionException e) {
            e.printStackTrace();
            return;
        }

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
        if (null == Const.pool) {
            return;
        }
        if (null == v) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = Const.pool.getResource();
        } catch (redis.clients.jedis.exceptions.JedisConnectionException e) {
            e.printStackTrace();
            return;
        }
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
        if (ValueWidget.isNullOrEmpty(v)) {
            return;
        }
        if (null == Const.pool) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = Const.pool.getResource();
        } catch (redis.clients.jedis.exceptions.JedisConnectionException e) {
            e.printStackTrace();
            return;
        }

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

    public void saveKeyCacheAndExpire(String id, String k, String v) {
        saveKeyCacheAndExpire(id, k, v, 24 * 15);
    }

    public void saveKeyCacheAndExpire(String id, String k, String v, int hours) {
        if (ValueWidget.isNullOrEmpty(v)) {
            return;
        }
        if (null == Const.pool) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = Const.pool.getResource();
        } catch (redis.clients.jedis.exceptions.JedisConnectionException e) {
            e.printStackTrace();
            return;
        }
        try {
            jedis.hset(id, k, v);
            jedis.expire(id, 60 * 60 * hours);

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

    public void setExpire(String id, int hours) {
        if (null == Const.pool) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = Const.pool.getResource();
        } catch (redis.clients.jedis.exceptions.JedisConnectionException e) {
            e.printStackTrace();
            return;
        }
        jedis.expire(id, 60 * 60 * hours);
    }
    public void saveAllKeyCache(String id, Map kv) {
        if (ValueWidget.isNullOrEmpty(id) || ValueWidget.isNullOrEmpty(kv)) {
            return;
        }
        if (null == Const.pool) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = Const.pool.getResource();
        } catch (redis.clients.jedis.exceptions.JedisConnectionException e) {
            e.printStackTrace();
            return;
        }

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
        if (null == Const.pool) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = Const.pool.getResource();
        } catch (redis.clients.jedis.exceptions.JedisConnectionException e) {
            e.printStackTrace();
            return;
        }

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
        if (null == Const.pool) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = Const.pool.getResource();
        } catch (redis.clients.jedis.exceptions.JedisConnectionException e) {
            e.printStackTrace();
            return null;
        }
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
        if (null == Const.pool) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = Const.pool.getResource();
        } catch (redis.clients.jedis.exceptions.JedisConnectionException e) {
            e.printStackTrace();
            return null;
        }
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
        if (null == Const.pool) {
            return new HashMap();
        }
        Jedis jedis = null;
        try {
            jedis = Const.pool.getResource();
        } catch (redis.clients.jedis.exceptions.JedisConnectionException e) {
            e.printStackTrace();
            return null;
        }
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
        if (null == Const.pool) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = Const.pool.getResource();
        } catch (redis.clients.jedis.exceptions.JedisConnectionException e) {
            e.printStackTrace();
            return;
        }

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
