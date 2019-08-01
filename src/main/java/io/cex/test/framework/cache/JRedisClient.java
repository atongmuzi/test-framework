package io.cex.test.framework.cache;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class JRedisClient implements JedisCommands{

    private ShardedJedisPool pool = null;
    private String keyPrefix = null;

    public JRedisClient(ShardedJedisPool pool,String keyPrefix){
        this.keyPrefix = keyPrefix;
        this.pool = pool;
    }

    public Long ttl(String key){
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.ttl(keyPrefix + key);
            }
            return jedis.ttl(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    public String set(String key, String value) {
        String result = null;
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                result = jedis.set(keyPrefix + key, value);
            }else{
                result = jedis.set(key, value);
            }
        }finally{
            pool.returnResource(jedis);
        }
        return result;
    }

    /**
     *
     * @author   Yangning
     * 说明    ：          如果存在则替换原有值 ，如果没有则设置新值
     * @Date	 2014	2014-8-27		下午9:43:37
     * @param key
     * @param value
     * @param second   秒
     * @return String
     * @throws
     */
    public String set(String key, String value,int second) {
        String result = setOnlyExist(key,value,second);
        if(null == result || !result.equals("OK")){
            result = setOnlyNotExist(key,value,second);
        }
        return result;
    }

    /**
     *
     * @author   Yangning
     * 说明    ：         仅在不存在此key时设置成功
     * @Date	 2014	2014-8-27		下午7:45:16
     * @param key
     * @param value
     * @param second
     * @return String
     * @throws
     */
    public String setOnlyNotExist(String key, String value, int second) {
        return this.set(key,value,"NX","EX",second);
    }

    /**
     *
     * @author   Yangning
     * 说明    ：         仅在存在此key时设置成功
     * @Date	 2014	2014-8-27		下午7:45:16
     * @param key
     * @param value
     * @param second
     * @return String
     * @throws
     */
    public String setOnlyExist(String key, String value, int second) {
        return this.set(key,value,"XX","EX",second);
    }

    /**
     *
     * @author   Yangning
     * 说明    ：          原始方法
     * @Date	 2014	2014-8-27		下午6:55:18
     * @param nxxx
     *            NX|XX, NX -- Only set the key if it does not already exist. XX
     *            -- Only set the key if it already exist.
     * @param expx
     *            EX|PX, expire time units: EX = seconds; PX = milliseconds
     * @param time
     *            expire time in the units of {@param #expx}
     * @throws
     */
    public String set(String key, String value, String nxxx, String expx,
                      long time) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.set(keyPrefix + key, value, nxxx, expx, time);
            }
            return jedis.set(key, value, nxxx, expx, time);
        }finally{
            pool.returnResource(jedis);
        }
    }
    public String get(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.get(keyPrefix+key);
            }
            return jedis.get(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    public long len(String key){
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.llen(keyPrefix+key);
            }
            return jedis.llen(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    public Boolean exists(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.exists(keyPrefix+key);
            }
            return jedis.exists(key);
        }finally{
            pool.returnResource(jedis);
        }
    }
    public Long persist(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.persist(keyPrefix+key);
            }
            return jedis.persist(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    public Long expire(String key, int seconds) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.expire(keyPrefix+key,seconds);
            }
            return jedis.expire(key, seconds);
        }finally{
            pool.returnResource(jedis);
        }
    }

    public Long incrBy(String key, long integer) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.incrBy(keyPrefix+key, integer);
            }
            return jedis.incrBy(key, integer);
        }finally{
            pool.returnResource(jedis);
        }
    }

    public Long incr(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.incr(keyPrefix+key);
            }
            return jedis.incr(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    public List<String> lrange(String key,int start,int end){
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.lrange(keyPrefix+key, start, end);
            }
            return jedis.lrange(key, start, end);
        }finally{
            pool.returnResource(jedis);
        }
    }

    public String lpop(String key){
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.lpop(keyPrefix+key);
            }
            return jedis.lpop(key);
        }finally{
            pool.returnResource(jedis);
        }


    }

    public Long lpush(String key,String ...strings){
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();

            if(null != keyPrefix){
                return jedis.lpush(keyPrefix + key, strings);
            }
            return jedis.lpush(key,strings);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public String type(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.type(keyPrefix + key);
            }
            return jedis.type(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.expireAt(keyPrefix + key,unixTime);
            }
            return jedis.expireAt(key,unixTime);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Boolean setbit(String key, long offset, boolean value) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.setbit(keyPrefix + key,offset,value);
            }
            return jedis.setbit(key,offset,value);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Boolean setbit(String key, long offset, String value) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.setbit(keyPrefix + key,offset,value);
            }
            return jedis.setbit(key,offset,value);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Boolean getbit(String key, long offset) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.getbit(keyPrefix+key,offset);
            }
            return jedis.getbit(key,offset);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long setrange(String key, long offset, String value) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.setrange(keyPrefix+key,offset, value);
            }
            return jedis.setrange(key,offset, value);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public String getrange(String key, long startOffset, long endOffset) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.getrange(keyPrefix+key,startOffset, endOffset);
            }
            return jedis.getrange(key,startOffset, endOffset);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public String getSet(String key, String value) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.getSet(keyPrefix+key,value);
            }
            return jedis.getSet(key,value);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long setnx(String key, String value) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.setnx(keyPrefix+key,value);
            }
            return jedis.setnx(key,value);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public String setex(String key, int seconds, String value) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.setex(keyPrefix+key,seconds,value);
            }
            return jedis.setex(key,seconds,value);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long decrBy(String key, long integer) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.decrBy(keyPrefix+key,integer);
            }
            return jedis.decrBy(keyPrefix+key,integer);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long decr(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.decr(keyPrefix+key);
            }
            return jedis.decr(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long append(String key, String value) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.append(keyPrefix+key,value);
            }
            return jedis.append(key,value);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public String substr(String key, int start, int end) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.substr(keyPrefix+key,start,end);
            }
            return jedis.substr(key,start,end);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Long hset(String key, String field, String value) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.hset(keyPrefix+key,field,value);
            }
            return jedis.hset(key,field,value);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public String hget(String key, String field) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.hget(keyPrefix+key,field);
            }
            return jedis.hget(key,field);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long hsetnx(String key, String field, String value) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.hsetnx(keyPrefix+key,field,value);
            }
            return jedis.hsetnx(key,field,value);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.hmset(keyPrefix+key,hash);
            }
            return jedis.hmset(key,hash);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.hmget(keyPrefix+key,fields);
            }
            return jedis.hmget(key,fields);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.hincrBy(keyPrefix+key,field,value);
            }
            return jedis.hincrBy(key,field,value);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Boolean hexists(String key, String field) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.hexists(keyPrefix+key,field);
            }
            return jedis.hexists(key,field);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long hdel(String key, String... field) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.hdel(keyPrefix+key,field);
            }
            return jedis.hdel(key,field);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long hlen(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.hlen(keyPrefix+key);
            }
            return jedis.hlen(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Set<String> hkeys(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.hkeys(keyPrefix+key);
            }
            return jedis.hkeys(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public List<String> hvals(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.hvals(keyPrefix+key);
            }
            return jedis.hvals(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.hgetAll(keyPrefix+key);
            }
            return jedis.hgetAll(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long rpush(String key, String... string) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.rpush(keyPrefix+key,string);
            }
            return jedis.rpush(key,string);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long llen(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.llen(keyPrefix+key);
            }
            return jedis.llen(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.lrange(keyPrefix+key,start,end);
            }
            return jedis.lrange(key,start,end);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public String ltrim(String key, long start, long end) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.ltrim(keyPrefix+key,start,end);
            }
            return jedis.ltrim(key,start,end);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public String lindex(String key, long index) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.lindex(keyPrefix+key,index);
            }
            return jedis.lindex(key,index);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public String lset(String key, long index, String value) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.lset(keyPrefix+key,index,value);
            }
            return jedis.lset(key,index,value);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long lrem(String key, long count, String value) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.lrem(keyPrefix+key,count,value);
            }
            return jedis.lrem(key,count,value);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public String rpop(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.rpop(keyPrefix+key);
            }
            return jedis.rpop(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long sadd(String key, String... member) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.sadd(keyPrefix+key,member);
            }
            return jedis.sadd(key,member);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Set<String> smembers(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.smembers(keyPrefix+key);
            }
            return jedis.smembers(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long srem(String key, String... member) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.srem(keyPrefix+key,member);
            }
            return jedis.srem(key,member);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public String spop(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.spop(keyPrefix+key);
            }
            return jedis.spop(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long scard(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.scard(keyPrefix+key);
            }
            return jedis.scard(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Boolean sismember(String key, String member) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.sismember(keyPrefix+key,member);
            }
            return jedis.sismember(key,member);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public String srandmember(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.srandmember(keyPrefix+key);
            }
            return jedis.srandmember(key);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Long strlen(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.strlen(keyPrefix+key);
            }
            return jedis.strlen(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long zadd(String key, double score, String member) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zadd(keyPrefix+key,score,member);
            }
            return jedis.zadd(key,score,member);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zadd(keyPrefix+key,scoreMembers);
            }
            return jedis.zadd(key,scoreMembers);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrange(keyPrefix+key,start,end);
            }
            return jedis.zrange(key,start,end);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Long zrem(String key, String... member) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrem(keyPrefix+key,member);
            }
            return jedis.zrem(key,member);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Double zincrby(String key, double score, String member) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zincrby(keyPrefix+key,score,member);
            }
            return jedis.zincrby(key,score,member);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long zrank(String key, String member) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrank(keyPrefix+key,member);
            }
            return jedis.zrank(key,member);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long zrevrank(String key, String member) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrevrank(keyPrefix+key,member);
            }
            return jedis.zrevrank(key,member);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrevrange(keyPrefix+key,start,end);
            }
            return jedis.zrevrange(key,start,end);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrangeWithScores(keyPrefix+key,start,end);
            }
            return jedis.zrangeWithScores(key,start,end);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrevrangeWithScores(keyPrefix+key,start,end);
            }
            return jedis.zrevrangeWithScores(key,start,end);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long zcard(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zcard(keyPrefix+key);
            }
            return jedis.zcard(key);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Double zscore(String key, String member) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zscore(keyPrefix+key,member);
            }
            return jedis.zscore(key,member);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public List<String> sort(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.sort(keyPrefix+key);
            }
            return jedis.sort(key);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public List<String> sort(String key, SortingParams sortingParameters) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.sort(keyPrefix+key,sortingParameters);
            }
            return jedis.sort(key,sortingParameters);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long zcount(String key, double min, double max) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zcount(keyPrefix+key,min,max);
            }
            return jedis.zcount(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long zcount(String key, String min, String max) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zcount(keyPrefix+key,min,max);
            }
            return jedis.zcount(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrangeByScore(keyPrefix+key,min,max);
            }
            return jedis.zrangeByScore(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrangeByScore(keyPrefix+key,min,max);
            }
            return jedis.zrangeByScore(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrevrangeByScore(keyPrefix+key,min,max);
            }
            return jedis.zrevrangeByScore(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max,
                                     int offset, int count) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrangeByScore(keyPrefix+key,min,max);
            }
            return jedis.zrangeByScore(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrevrangeByScore(keyPrefix+key,min,max);
            }
            return jedis.zrevrangeByScore(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max,
                                     int offset, int count) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrangeByScore(keyPrefix+key,min,max);
            }
            return jedis.zrangeByScore(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min,
                                        int offset, int count) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrevrangeByScore(keyPrefix+key,min,max);
            }
            return jedis.zrevrangeByScore(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrangeByScoreWithScores(keyPrefix+key,min,max);
            }
            return jedis.zrangeByScoreWithScores(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
                                                 double min) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrevrangeByScoreWithScores(keyPrefix+key,min,max);
            }
            return jedis.zrevrangeByScoreWithScores(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min,
                                              double max, int offset, int count) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrangeByScoreWithScores(keyPrefix+key,min,max);
            }
            return jedis.zrangeByScoreWithScores(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min,
                                        int offset, int count) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrevrangeByScore(keyPrefix+key,min,max);
            }
            return jedis.zrevrangeByScore(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrangeByScoreWithScores(keyPrefix+key,min,max);
            }
            return jedis.zrangeByScoreWithScores(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max,
                                                 String min) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrevrangeByScoreWithScores(keyPrefix+key,min,max);
            }
            return jedis.zrevrangeByScoreWithScores(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min,
                                              String max, int offset, int count) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrangeByScoreWithScores(keyPrefix+key,min,max);
            }
            return jedis.zrangeByScoreWithScores(key,min,max);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
                                                 double min, int offset, int count) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrevrangeByScoreWithScores(keyPrefix+key,min,max,offset,count);
            }
            return jedis.zrevrangeByScoreWithScores(key,min,max,offset,count);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max,
                                                 String min, int offset, int count) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zrevrangeByScoreWithScores(keyPrefix+key,min,max,offset,count);
            }
            return jedis.zrevrangeByScoreWithScores(key,min,max,offset,count);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long zremrangeByRank(String key, long start, long end) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zremrangeByRank(keyPrefix+key,start,end);
            }
            return jedis.zremrangeByRank(key,start,end);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long zremrangeByScore(String key, double start, double end) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zremrangeByScore(keyPrefix+key,start,end);
            }
            return jedis.zremrangeByScore(key,start,end);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Long zremrangeByScore(String key, String start, String end) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zremrangeByScore(keyPrefix+key,start,end);
            }
            return jedis.zremrangeByScore(key,start,end);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Long linsert(String key, LIST_POSITION where, String pivot,
                        String value) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.linsert(keyPrefix+key,where,pivot,value);
            }
            return jedis.linsert(key,where,pivot,value);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Long lpushx(String key, String... string) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.lpushx(keyPrefix+key,string);
            }
            return jedis.lpushx(key,string);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long rpushx(String key, String... string) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.rpushx(keyPrefix+key,string);
            }
            return jedis.rpushx(key,string);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public List<String> blpop(String arg) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.blpop(keyPrefix+arg);
            }
            return jedis.blpop(arg);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public List<String> brpop(String arg) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.brpop(keyPrefix+arg);
            }
            return jedis.brpop(arg);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Long del(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.del(keyPrefix+key);
            }
            return jedis.del(key);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public String echo(String string) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.echo(keyPrefix+string);
            }
            return jedis.echo(string);
        }finally{
            pool.returnResource(jedis);
        }


    }

    @Override
    public Long move(String key, int dbIndex) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.move(keyPrefix+key,dbIndex);
            }
            return jedis.move(key,dbIndex);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long bitcount(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.bitcount(keyPrefix+key);
            }
            return jedis.bitcount(key);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public Long bitcount(String key, long start, long end) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.bitcount(keyPrefix+key,start,end);
            }
            return jedis.bitcount(key,start,end);
        }finally{
            pool.returnResource(jedis);
        }


    }

    @Override
    @Deprecated
    public ScanResult<Entry<String, String>> hscan(String key, int cursor) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.hscan(keyPrefix+key,cursor);
            }
            return jedis.hscan(key,cursor);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    @Deprecated
    public ScanResult<String> sscan(String key, int cursor) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.sscan(keyPrefix+key,cursor);
            }
            return jedis.sscan(key,cursor);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    @Deprecated
    public ScanResult<Tuple> zscan(String key, int cursor) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zscan(keyPrefix+key,cursor);
            }
            return jedis.zscan(key,cursor);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.hscan(keyPrefix+key,cursor);
            }
            return jedis.hscan(key,cursor);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public ScanResult<String> sscan(String key, String cursor) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.sscan(keyPrefix+key,cursor);
            }
            return jedis.sscan(key,cursor);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.zscan(keyPrefix+key,cursor);
            }
            return jedis.zscan(key,cursor);
        }finally{
            pool.returnResource(jedis);
        }
    }

    @Override
    public Long pfadd(String key, String... elements) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.pfadd(keyPrefix+key,elements);
            }
            return jedis.pfadd(key,elements);
        }finally{
            pool.returnResource(jedis);
        }

    }

    @Override
    public long pfcount(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            if(null != keyPrefix){
                return jedis.pfadd(keyPrefix+key);
            }
            return jedis.pfadd(key);
        }finally{
            pool.returnResource(jedis);
        }
    }
}
