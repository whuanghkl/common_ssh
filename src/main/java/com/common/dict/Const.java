package com.common.dict;

import com.common.bean.exception.LogicBusinessException;
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 */
public class Const {
    public static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String ATTRIBUTE_ALLOW_ORIGIN_DTO = "allowOriginDto";
    protected static Logger logger = Logger.getLogger(Const.class);
    public static final String qq_token_url = "https://graph.qq.com/oauth2.0/token";
    public static final String qq_openid_url = "https://graph.qq.com/oauth2.0/me";
    public static final String qq_userinfo_url = "https://graph.qq.com/user/get_user_info";
    public static final String qq_callback = "http://develop.chanjet.com:8090/login/thirdLogin";

    public static Set<String> sentinels = new HashSet<String>();
    public static JedisSentinelPool pool;

    static{

        sentinels.add("123.57.78.161:26379");
        try {
            pool = new JedisSentinelPool("mymaster", sentinels, "re1230314acc");
        } catch (redis.clients.jedis.exceptions.JedisConnectionException e) {
            e.printStackTrace();
            logger.error("JedisSentinelPool() error", e);
            throw new LogicBusinessException(e.getMessage(), e);
        }
        
    }

}
