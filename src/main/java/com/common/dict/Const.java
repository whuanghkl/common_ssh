package com.common.dict;

import com.file.hw.props.GenericReadPropsUtil;
import com.string.widget.util.ValueWidget;
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisSentinelPool;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
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


    public static Set<String> sentinels = new HashSet<String>();
    public static JedisSentinelPool pool;

    static {
        initRedisConnect();
    }

    public static void connectRedisServer() {
        initRedisConnect();
    }

    public static void initRedisConnect() {
        String sentinelIp = "59.110.236.186:26379";//"123.57.78.161:26379"
        String password = "re1230314acc";
        System.out.println("重新连接 :" + sentinelIp);
        initRedisConnect(sentinelIp, password);
    }

    private static void initRedisConnect(String sentinelIp, String password) {
        boolean hasPassword = true;
        try {
            Properties properties = GenericReadPropsUtil.getProperties(true, "config/redis_sentinel.properties");
            if (null != properties) {
                String sentinelIpTmp = properties.getProperty("sentinel.ip");
                if (!ValueWidget.isNullOrEmpty(sentinelIpTmp)) {
                    sentinelIp = sentinelIpTmp;
                }
                String paswdTmp = properties.getProperty("sentinel.password");
                if (Constant2.NULL_PARAMETER.equals(paswdTmp)) {
                    hasPassword = false;
                } else if (!ValueWidget.isNullOrEmpty(paswdTmp)) {
                    password = paswdTmp;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sentinels.add(sentinelIp);
        try {
            createSentinelPool(password, hasPassword);
        } catch (redis.clients.jedis.exceptions.JedisConnectionException e) {
            e.printStackTrace();
            logger.error("JedisSentinelPool() error", e);
            createSentinelPool(password, hasPassword);
//            throw new LogicBusinessException(e.getMessage(), e);
        }
    }

    private static void createSentinelPool(String password, boolean hasPassword) {
        try {
            if (hasPassword) {
                pool = new JedisSentinelPool("mymaster", sentinels, password);
            } else {
                pool = new JedisSentinelPool("mymaster", sentinels);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("createSentinelPool error", e);
        }
    }

    /***
     * .json
     */
    public static final String STUB_FILE_SUFFIX = ".xml";
    public static final String STUB_FILE_SUFFIX_JSON = ".json";
}
