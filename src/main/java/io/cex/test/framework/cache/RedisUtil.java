package io.cex.test.framework.cache;


/**
 * @author shenqingyan
 * @create 2017/7/10 16:26
 * @desc redis工具类
 **/
public class RedisUtil {


    private RedisUtil() {
    }
    /**
    * @desc 连接redis
    **/
    public static JRedisClient getClient(){
        return JedisClientHelper.getClient();
    }
    /**
    * @desc get操作
    * @param  key 获取key
    **/
    public static String get(String key) {
        return getClient().get(key);
    }
    /**
    * @desc 设置 key 的过期时间,key 过期后将不再可用
    * @param  key key值
     *@param  expire 过期时间
    **/
    public static long expire(String key, int expire) {
        return getClient().expire(key, expire);
    }

    /**
     * @desc 设置 key 对应value以及过期时间
     * @param  key key值
     * @param  value value值
     *@param  expire 过期时间
     **/
    public static boolean set(String key, String value, int expire) {
        try {
            getClient().set(key, value, expire);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @desc 判断hash中 key的field是否存在
     * @param key key值
     * @param field 域
     */
    public static boolean isHexists(String key, String field) {
        return getClient().hexists(key, field);
    }

    /**
     * @desc 判断key是否存在
     * @param key key值
     */
    public static boolean exists(String key) {
        return getClient().exists(key);
    }

    /**
     * @desc 删除某key
     * @param key key值
     */
    public static boolean del(String key) {
        return getClient().del(key) > 0;
    }

    /**
     * @desc 返回哈希表中指定字段的值
     * @param key key值
     * @param field 域
     */
    public static String hget(String key, String field) {
        return getClient().hget(key, field);
    }

    /**
     * @desc 为哈希表中的字段赋值
     * @param key key值
     * @param field 域
     */
    public static long hset(String key, String field, String value) {
        return getClient().hset(key, field, value);
    }

    /**
     * @desc 为哈希表 key 中的域 field 的值加上增量 increment
     * @param key key值
     * @param field 域
     * @param incrNum 增量
     */
    public static long hincrBy(String key, String field, Integer incrNum) {
        return getClient().hincrBy(key, field, incrNum);
    }

    /**
     * @desc 删除hash中的field
     * @param key key值
     * @param field 域
     */
    public static long hdel(String key, String... field) {
        return getClient().hdel(key, field);
    }

    /**
     * @desc 将一个或多个成员元素及其分数值加入到有序集当中
     * @param key key值
     * @param score 分值
     * @param member 成员元素
     */
    public static long zadd(String key, Integer score, String member) {
        return getClient().zadd(key, score, member);
    }

    /**
     * @desc 获取sorted set中member的score
     * @param key key值
     * @param member 成员元素
     */
    public static double zscore(String key, String member) {
        return getClient().zscore(key, member);
    }

    /**
     * @desc sorted set中member的score增加incrNum
     * @param key key值
     * @param incrNum 增量
     * @param member 成员元素
     */
    public static double zincrby(String key, Integer incrNum, String member) {
        return getClient().zincrby(key, incrNum, member);
    }

    /**
     * @desc 删除sorted set中的member
     * @param key key值
     * @param member 成员元素
     */
    public static long zincrby(String key, String... member) {
        return getClient().zrem(key, member);
    }

}
