package io.cex.test.framework.cache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * @author shenqingyan
 * @create 2017/6/23 16:18
 * @desc 连接redis
 **/
public class JedisClientHelper {
    private static JRedisClient client = null;
    static {
        try {
            //获取配置文件路径
            String confPath = System.getProperty("redis.path", "src/main/resources/redis.xml");
            client = JRedisHelperCreator.creatInstance(confPath);
            log.info("----------init redis success config path:" + confPath);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("---------init redis has an exception:" + e.getMessage());
        }
    }

    public static JRedisClient getClient() {
        return client;
    }

}
