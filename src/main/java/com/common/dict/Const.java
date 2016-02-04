package com.common.dict;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.JedisSentinelPool;

/**
 */
public class Const {

    public static final String qq_token_url = "https://graph.qq.com/oauth2.0/token";
    public static final String qq_openid_url = "https://graph.qq.com/oauth2.0/me";
    public static final String qq_userinfo_url = "https://graph.qq.com/user/get_user_info";
    public static final String qq_callback = "http://develop.chanjet.com:8090/login/thirdLogin";

    public static Set<String> sentinels = new HashSet<String>();
    public static JedisSentinelPool pool;

    static{

            sentinels.add("10.18.4.2:26379");
            sentinels.add("10.18.4.3:26379");
            sentinels.add("10.18.4.4:26379");
            sentinels.add("10.18.4.5:26379");
            pool = new JedisSentinelPool("master_chanjet", sentinels, "LKJ&*^)H");
        
    }

}
