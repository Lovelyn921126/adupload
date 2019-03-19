package com.fang.utils.nosql.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.fang.utils.nosql.redis.JedisTemplate.JedisAction;
import com.fang.utils.nosql.redis.JedisTemplate.JedisActionNoResult;
import com.fang.utils.nosql.redis.JedisTemplate.PipelineAction;
import com.fang.utils.nosql.redis.JedisTemplate.PipelineActionNoResult;
import com.fang.utils.nosql.redis.pool.JedisPool;

import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Hashing;

/**
 * 用于分布式的redis,且key的分布算法为一致性哈希算法
 * JedisShardedTemplate 提供了一个template方法，负责对Jedis连接的获取与归还。
 * JedisAction<T> 和 JedisActionNoResult两种回调接口，适用于有无返回值两种情况。
 * PipelineAction 与 PipelineActionResult两种接口，适合于pipeline中批量传输命令的情况。
 * 同时提供一些JedisOperation中定义的 最常用函数的封装, 如get/set/zadd等。
 *
 * @author wangzhiyaun
 */
public class JedisShardedTemplate {

  /**
   * Hashing
   */
  private final Hashing algo = Hashing.MURMUR_HASH;

  /**
   * nodes
   */
  private TreeMap<Long, JedisTemplate> nodes = new TreeMap<Long, JedisTemplate>();

  /**
   * singleTemplate
   */
  private JedisTemplate singleTemplate = null;

  /**
   * JedisShardedTemplate
   *
   * @param jedisPools
   *        jedisPools
   */
  public JedisShardedTemplate(JedisPool... jedisPools) {
    if (jedisPools.length == 1) {
      singleTemplate = new JedisTemplate(jedisPools[0]);
    } else {
      initNodes(jedisPools);
    }
  }

  /**
   * JedisShardedTemplate
   *
   * @param jedisPools
   *        jedisPools
   */
  public JedisShardedTemplate(List<JedisPool> jedisPools) {
    this(jedisPools.toArray(new JedisPool[jedisPools.size()]));
  }

  /**
   * initNodes
   *
   * @param jedisPools
   *        jedisPools
   */
  private void initNodes(JedisPool... jedisPools) {
    for (int i = 0; i != jedisPools.length; i++) {
      // more entry the make the hash ring be more balance
      for (int n = 0; n < 128; n++) {
        final JedisPool jedisPool = jedisPools[i];
        nodes.put(this.algo.hash("SHARD-" + i + "-NODE-" + n), new JedisTemplate(jedisPool));
      }
    }
  }

  /**
   * Hash the key and get the jedisTemplate from the hash ring. Get idea from Jedis's Sharded.java
   *
   * @param key
   *        key
   * @return JedisTemplate
   */
  public JedisTemplate getShard(String key) {
    if (singleTemplate != null) {
      return singleTemplate;
    }

    SortedMap<Long, JedisTemplate> tail = nodes.tailMap(algo.hash(key));
    if (tail.isEmpty()) {
      // the last node, back to first.
      return nodes.get(nodes.firstKey());
    }
    return tail.get(tail.firstKey());
  }

  /**
   * Execute the action.
   *
   * @param key
   *        the action must process only this key, or this key is a sharding key.
   * @param jedisAction
   *        jedisAction
   * @param <T>
   *        T
   * @return execute
   * @throws JedisException
   *         JedisException
   */
  public <T> T execute(String key, JedisAction<T> jedisAction) throws JedisException {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.execute(jedisAction);
  }

  /**
   * Execute the action.
   *
   * @param key
   *        key the action must process only this key, or this key is a sharding key.
   * @param jedisAction
   *        jedisAction
   * @throws JedisException
   *         JedisException
   */
  public void execute(String key, JedisActionNoResult jedisAction) throws JedisException {
    JedisTemplate jedisTemplate = getShard(key);
    jedisTemplate.execute(jedisAction);
  }

  /**
   * Execute the pipeline.
   *
   * @param key
   *        the action must process only this key, or this key is a sharding key.
   * @param pipelineAction
   *        pipelineAction
   * @return Execute the pipeline
   * @throws JedisException
   *         JedisException
   */
  public List<Object> execute(String key, PipelineAction pipelineAction) throws JedisException {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.execute(pipelineAction);
  }

  /**
   * Execute the pipeline.
   *
   * @param key
   *        the action must process only this key, or this key is a sharding key.
   * @param pipelineAction
   *        pipelineAction
   * @throws JedisException
   *         JedisException
   */
  public void execute(String key, PipelineActionNoResult pipelineAction) throws JedisException {
    JedisTemplate jedisTemplate = getShard(key);
    jedisTemplate.execute(pipelineAction);
  }

  // / Common Actions ///

  /**
   * Redis DEL 命令用于删除已存在的键。不存在的 key 会被忽略。
   *
   * @param key
   *        key
   * @return 如果被删除键的数量等于key的个数，返回true，否则返回false
   */
  public Boolean del(final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.del(key);
  }

  /**
   * Redis DEL 命令用于删除已存在的键。不存在的 key 会被忽略。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @return 如果被删除键的数量等于key的个数，返回true，否则返回false
   */
  public Boolean del(final String shardingKey, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.del(key);
  }

  // / String Actions ///

  /**
   * Redis Get 命令用于获取指定 key 的值。如果 key 不存在，返回 nil 。如果key 储存的值不是字符串类型，返回一个错误。
   *
   * @param key
   *        key
   * @return 返回 key 的值，如果 key 不存在时，返回 nil。 如果 key 不是字符串类型，那么返回一个错误。
   */
  public String get(final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.get(key);
  }

  /**
   * Redis Get 命令用于获取指定 key 的值。如果 key 不存在，返回 null 。如果key 储存的值不是字符串类型，返回一个错误。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @return 返回 key 的值，如果 key 不存在时，返回 nil。 如果 key 不是字符串类型，那么返回一个错误。
   */
  public String get(final String shardingKey, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.get(key);
  }

  /**
   * Redis Get 命令用于获取指定 key 的值，此值是Long类型。如果 key 不存在，返回 nil 。如果key 储存的值不是Long类型，返回一个错误。
   *
   * @param key
   *        key
   * @return 返回 key 的值，如果 key 不存在，返回 null 。如果key 储存的值不是Long类型，返回一个错误。
   */
  public Long getAsLong(final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.getAsLong(key);
  }

  /**
   * Redis Get 命令用于获取指定 key 的值，此值是Long类型。如果 key 不存在，返回 null 。如果key 储存的值不是Long类型，返回一个错误。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @return 返回 key 的值，如果 key 不存在，返回 null 。如果key 储存的值不是Long类型，返回一个错误。
   */
  public Long getAsLong(final String shardingKey, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.getAsLong(key);
  }

  /**
   * Redis Get 命令用于获取指定 key 的值，此值是Integer类型。如果 key 不存在，返回 null 。如果key 储存的值不是Integer类型，返回一个错误。
   *
   * @param key
   *        key
   * @return 返回 key 的值，如果 key 不存在，返回 null 。如果key 储存的值不是Integer类型，返回一个错误。
   */
  public Integer getAsInt(final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.getAsInt(key);
  }

  /**
   * Redis Get 命令用于获取指定 key 的值，此值是Integer类型。如果 key 不存在，返回 null 。如果key 储存的值不是Integer类型，返回一个错误。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @return 返回 key 的值，如果 key 不存在，返回 null 。如果key 储存的值不是Integer类型，返回一个错误。
   */
  public Integer getAsInt(final String shardingKey, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.getAsInt(key);
  }

  /**
   * Redis SET 命令用于设置给定 key 的值。如果 key 已经存储其他值， SET 就覆写旧值，且无视类型。
   *
   * @param key
   *        key
   * @param value
   *        value
   * @return 设置成功时返回 true,否则返回false
   */
  public Boolean set(final String key, final String value) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.set(key, value);
  }

  /**
   * Redis SET 命令用于设置给定 key 的值。如果 key 已经存储其他值， SET 就覆写旧值，且无视类型。<br/>
   * SET 在设置操作成功完成时，才返回 OK 。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param value
   *        value
   * @return 设置成功时返回 true,否则返回false
   */
  public Boolean set(final String shardingKey, final String key, final String value) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.set(key, value);
  }

  /**
   * Redis Setex 命令为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值。
   *
   * @param key
   *        key
   * @param value
   *        value
   * @param seconds
   *        seconds
   * @return 设置成功时返回 true,否则返回false
   */
  public Boolean setex(final String key, final String value, final int seconds) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.setex(key, value, seconds);
  }

  /**
   * Redis Setex 命令为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param value
   *        value
   * @param seconds
   *        seconds
   * @return 设置成功时返回 true,否则返回false
   */
  public Boolean setex(final String shardingKey, final String key, final String value, final int seconds) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.setex(key, value, seconds);
  }

  /**
   * Redis Setnx（SET if Not eXists） 命令在指定的 key 不存在时，为 key 设置指定的值.
   *
   * @param key
   *        key
   * @param value
   *        value
   * @return 设置成功时返回 true,否则返回false
   */
  public Boolean setnx(final String key, final String value) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.setnx(key, value);
  }

  /**
   * Redis Setnx（SET if Not eXists） 命令在指定的 key 不存在时，为 key 设置指定的值.
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param value
   *        value
   * @return 设置成功时返回 true,否则返回false
   */
  public Boolean setnx(final String shardingKey, final String key, final String value) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.setnx(key, value);
  }

  /**
   * setnx + setex
   *
   * @param key
   *        key
   * @param value
   *        value
   * @param seconds
   *        seconds
   * @return 设置成功时返回 true,否则返回false
   */
  public Boolean setnxex(final String key, final String value, final int seconds) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.setnxex(key, value, seconds);
  }

  /**
   * setnx + setex
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param value
   *        value
   * @param seconds
   *        seconds
   * @return 设置成功时返回 true,否则返回false
   */
  public Boolean setnxex(final String shardingKey, final String key, final String value, final int seconds) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.setnxex(key, value, seconds);
  }

  /**
   * Redis Getset 命令用于设置指定 key 的值，并返回 key 旧的值。
   *
   * @param key
   *        key
   * @param value
   *        value
   * @return 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 nil 。<br/>
   *         当 key 存在但不是字符串类型时，返回一个错误。
   */
  public String getSet(final String key, final String value) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.getSet(key, value);
  }

  /**
   * Redis Getset 命令用于设置指定 key 的值，并返回 key 旧的值。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param value
   *        value
   * @return 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 nil 。<br/>
   *         当 key 存在但不是字符串类型时，返回一个错误。
   */
  public String getSet(final String shardingKey, final String key, final String value) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.getSet(key, value);
  }

  /**
   * Redis Incr 命令将 key 中储存的数字值增一。<br/>
   * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。<br/>
   * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。<br/>
   * 本操作的值限制在 64 位(bit)有符号数字表示之内。
   *
   * @param key
   *        key
   * @return 执行 INCR 命令之后 key 的值。
   */
  public Long incr(final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.incr(key);
  }

  /**
   * Redis Incr 命令将 key 中储存的数字值增一。<br/>
   * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。<br/>
   * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。<br/>
   * 本操作的值限制在 64 位(bit)有符号数字表示之内。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @return 执行 INCR 命令之后 key 的值。
   */
  public Long incr(final String shardingKey, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.incr(key);
  }

  /**
   * Redis Incrby 命令将 key 中储存的数字加上指定的增量值。<br/>
   * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。<br/>
   * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。<br/>
   * 本操作的值限制在 64 位(bit)有符号数字表示之内。
   *
   * @param key
   *        key
   * @param increment
   *        increment
   * @return 加上指定的增量值之后， key 的值。
   */
  public Long incrBy(final String key, final Long increment) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.incrBy(key, increment);
  }

  /**
   * Redis Incrby 命令将 key 中储存的数字加上指定的增量值。<br/>
   * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。<br/>
   * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。<br/>
   * 本操作的值限制在 64 位(bit)有符号数字表示之内。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param increment
   *        increment
   * @return 加上指定的增量值之后， key 的值。
   */
  public Long incrBy(final String shardingKey, final String key, final Long increment) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.incrBy(key, increment);
  }

  /**
   * Redis Incrbyfloat 命令为 key 中所储存的值加上指定的浮点数增量值。<br/>
   * 如果 key 不存在，那么 INCRBYFLOAT 会先将 key 的值设为 0 ，再执行加法操作。
   *
   * @param key
   *        key
   * @param increment
   *        increment
   * @return 执行命令之后 key 的值。
   */
  public Double incrByFloat(final String key, final double increment) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.incrByFloat(key, increment);
  }

  /**
   * Redis Incrbyfloat 命令为 key 中所储存的值加上指定的浮点数增量值。<br/>
   * 如果 key 不存在，那么 INCRBYFLOAT 会先将 key 的值设为 0 ，再执行加法操作。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param increment
   *        increment
   * @return 执行命令之后 key 的值。
   */
  public Double incrByFloat(final String shardingKey, final String key, final double increment) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.incrByFloat(key, increment);
  }

  /**
   * Redis Decr 命令将 key 中储存的数字值减一。<br/>
   * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。<br/>
   * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。<br/>
   * 本操作的值限制在 64 位(bit)有符号数字表示之内。
   *
   * @param key
   *        key
   * @return 执行命令之后 key 的值。
   */
  public Long decr(final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.decr(key);
  }

  /**
   * Redis Decr 命令将 key 中储存的数字值减一。<br/>
   * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。<br/>
   * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。<br/>
   * 本操作的值限制在 64 位(bit)有符号数字表示之内。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @return 执行命令之后 key 的值。
   */
  public Long decr(final String shardingKey, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.decr(key);
  }

  /**
   * Redis Decrby 命令将 key 所储存的值减去指定的减量值。<br/>
   * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。<br/>
   * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。<br/>
   * 本操作的值限制在 64 位(bit)有符号数字表示之内。
   *
   * @param key
   *        key
   * @param decrement
   *        decrement
   * @return 减去指定减量值之后， key 的值。
   */
  public Long decrBy(final String key, final Long decrement) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.decrBy(key, decrement);
  }

  /**
   * Redis Decrby 命令将 key 所储存的值减去指定的减量值。<br/>
   * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。<br/>
   * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。<br/>
   * 本操作的值限制在 64 位(bit)有符号数字表示之内。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param decrement
   *        decrement
   * @return 减去指定减量值之后， key 的值。
   */
  public Long decrBy(final String shardingKey, final String key, final Long decrement) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.decrBy(key, decrement);
  }

  // / Hash Actions ///

  /**
   * Redis Hget 命令用于返回哈希表中指定字段的值。
   *
   * @param key
   *        key
   * @param field
   *        field
   * @return 返回给定字段的值。如果给定的字段或 key 不存在时，返回 nil 。
   */
  public String hget(final String key, final String field) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.hget(key, field);
  }

  /**
   * Redis Hget 命令用于返回哈希表中指定字段的值。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param field
   *        field
   * @return 返回给定字段的值。如果给定的字段或 key 不存在时，返回 nil 。
   */
  public String hget(final String shardingKey, final String key, final String field) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.hget(key, field);
  }

  /**
   * Redis Hmget 命令用于返回哈希表中，一个给定字段的值。<br/>
   * 如果指定的字段不存在于哈希表，那么返回一个 nil 值。
   *
   * @param key
   *        key
   * @param field
   *        field
   * @return 一个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样。
   */
  public List<String> hmget(final String key, final String field) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.hmget(key, field);
  }

  /**
   * Redis Hmget 命令用于返回哈希表中，一个或多个给定字段的值。<br/>
   * 如果指定的字段不存在于哈希表，那么返回一个 nil 值。
   *
   * @param key
   *        key
   * @param fields
   *        fields
   * @return 一个包含多个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样。
   */
  public List<String> hmget(final String key, final String[] fields) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.hmget(key, fields);
  }

  /**
   * Redis Hmget 命令用于返回哈希表中，一个给定字段的值。<br/>
   * 如果指定的字段不存在于哈希表，那么返回一个 nil 值。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param field
   *        field
   * @return 一个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样。
   */
  public List<String> hmget(final String shardingKey, final String key, final String field) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.hmget(key, field);
  }

  /**
   * Redis Hmget 命令用于返回哈希表中，一个或多个给定字段的值。<br/>
   * 如果指定的字段不存在于哈希表，那么返回一个 nil 值。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param fields
   *        fields
   * @return 一个包含多个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样。
   */
  public List<String> hmget(final String shardingKey, final String key, final String[] fields) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.hmget(key, fields);
  }

  /**
   * Redis Hgetall 命令用于返回哈希表中，所有的字段和值。<br/>
   * 在返回值里，紧跟每个字段名(field name)之后是字段的值(value)，所以返回值的长度是哈希表大小的两倍。
   *
   * @param key
   *        key
   * @return 以列表形式返回哈希表的字段及字段值。 若 key 不存在，返回空列表。
   */
  public Map<String, String> hgetAll(final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.hgetAll(key);
  }

  /**
   * Redis Hgetall 命令用于返回哈希表中，所有的字段和值。<br/>
   * 在返回值里，紧跟每个字段名(field name)之后是字段的值(value)，所以返回值的长度是哈希表大小的两倍。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @return 以列表形式返回哈希表的字段及字段值。 若 key 不存在，返回空列表。
   */
  public Map<String, String> hgetAll(final String shardingKey, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.hgetAll(key);
  }

  /**
   * Redis Hset 命令用于为哈希表中的字段赋值 。<br/>
   * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。<br/>
   * 如果字段已经存在于哈希表中，旧值将被覆盖。
   *
   * @param key
   *        key
   * @param field
   *        field
   * @param value
   *        value
   */
  public void hset(final String key, final String field, final String value) {
    JedisTemplate jedisTemplate = getShard(key);
    jedisTemplate.hset(key, field, value);
  }

  /**
   * Redis Hset 命令用于为哈希表中的字段赋值 。<br/>
   * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。<br/>
   * 如果字段已经存在于哈希表中，旧值将被覆盖。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param field
   *        field
   * @param value
   *        value
   */
  public void hset(final String shardingKey, final String key, final String field, final String value) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    jedisTemplate.hset(key, field, value);
  }

  /**
   * Redis Hmset 命令用于同时将多个 field-value (字段-值)对设置到哈希表中。<br/>
   * 此命令会覆盖哈希表中已存在的字段。<br/>
   * 如果哈希表不存在，会创建一个空哈希表，并执行 HMSET 操作。
   *
   * @param key
   *        key
   * @param map
   *        map
   * @return 如果命令执行成功，返回 true.否则返回false
   */
  public Boolean hmset(final String key, final Map<String, String> map) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.hmset(key, map);
  }

  /**
   * Redis Hmset 命令用于同时将多个 field-value (字段-值)对设置到哈希表中。<br/>
   * 此命令会覆盖哈希表中已存在的字段。<br/>
   * 如果哈希表不存在，会创建一个空哈希表，并执行 HMSET 操作。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param map
   *        map
   * @return 如果命令执行成功，返回 true.否则返回false
   */
  public Boolean hmset(final String shardingKey, final String key, final Map<String, String> map) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.hmset(key, map);
  }

  /**
   * Redis Hsetnx 命令用于为哈希表中不存在的的字段赋值 。<br/>
   * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。<br/>
   * 如果字段已经存在于哈希表中，操作无效。<br/>
   * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
   *
   * @param key
   *        key
   * @param fieldName
   *        fieldName
   * @param value
   *        value
   * @return 设置成功，返回 true 。 如果给定字段已经存在且没有操作被执行，返回 false
   */
  public Boolean hsetnx(final String key, final String fieldName, final String value) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.hsetnx(key, fieldName, value);
  }

  /**
   * Redis Hsetnx 命令用于为哈希表中不存在的的字段赋值 。<br/>
   * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。<br/>
   * 如果字段已经存在于哈希表中，操作无效。<br/>
   * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param fieldName
   *        fieldName
   * @param value
   *        value
   * @return 设置成功，返回 true 。 如果给定字段已经存在且没有操作被执行，返回 false
   */
  public Boolean hsetnx(final String shardingKey, final String key, final String fieldName, final String value) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.hsetnx(key, fieldName, value);
  }

  /**
   * Redis Hincrby 命令用于为哈希表中的字段值加上指定增量值。<br/>
   * 增量也可以为负数，相当于对指定字段进行减法操作。<br/>
   * 如果哈希表的 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。<br/>
   * 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。<br/>
   * 对一个储存字符串值的字段执行 HINCRBY 命令将造成一个错误。<br/>
   * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
   *
   * @param key
   *        key
   * @param fieldName
   *        fieldName
   * @param increment
   *        increment
   * @return 执行 HINCRBY 命令之后，哈希表中字段的值。
   */
  public Long hincrBy(final String key, final String fieldName, final long increment) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.hincrBy(key, fieldName, increment);
  }

  /**
   * Redis Hincrby 命令用于为哈希表中的字段值加上指定增量值。<br/>
   * 增量也可以为负数，相当于对指定字段进行减法操作。<br/>
   * 如果哈希表的 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。<br/>
   * 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。<br/>
   * 对一个储存字符串值的字段执行 HINCRBY 命令将造成一个错误。<br/>
   * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param fieldName
   *        fieldName
   * @param increment
   *        increment
   * @return 执行 HINCRBY 命令之后，哈希表中字段的值。
   */
  public Long hincrBy(final String shardingKey, final String key, final String fieldName, final long increment) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.hincrBy(key, fieldName, increment);
  }

  /**
   * Redis Hincrbyfloat 命令用于为哈希表中的字段值加上指定浮点数增量值。<br/>
   * 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。
   *
   * @param key
   *        key
   * @param fieldName
   *        fieldName
   * @param increment
   *        increment
   * @return 执行 Hincrbyfloat 命令之后，哈希表中字段的值。
   */
  public Double hincrByFloat(final String key, final String fieldName, final double increment) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.hincrByFloat(key, fieldName, increment);
  }

  /**
   * Redis Hincrbyfloat 命令用于为哈希表中的字段值加上指定浮点数增量值。<br/>
   * 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param fieldName
   *        fieldName
   * @param increment
   *        increment
   * @return 执行 Hincrbyfloat 命令之后，哈希表中字段的值。
   */
  public Double hincrByFloat(final String shardingKey, final String key, final String fieldName,
                             final double increment) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.hincrByFloat(key, fieldName, increment);
  }

  /**
   * Redis Hdel 命令用于删除哈希表 key 中的一个指定字段，不存在的字段将被忽略。
   *
   * @param key
   *        key
   * @param fieldsName
   *        fieldsName
   * @return 被成功删除字段的数量，不包括被忽略的字段。
   */
  public Long hdel(final String key, final String fieldsName) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.hdel(key, fieldsName);
  }

  /**
   * Redis Hdel 命令用于删除哈希表 key 多个指定字段，不存在的字段将被忽略。
   *
   * @param key
   *        key
   * @param fieldsNames
   *        fieldsNames
   * @return 被成功删除字段的数量，不包括被忽略的字段。
   */
  public Long hdel(final String key, final String[] fieldsNames) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.hdel(key, fieldsNames);
  }

  /**
   * Redis Hdel 命令用于删除哈希表 key 中的一个指定字段，不存在的字段将被忽略。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param fieldsName
   *        fieldsName
   * @return 被成功删除字段的数量，不包括被忽略的字段。
   */
  public Long hdel(final String shardingKey, final String key, final String fieldsName) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.hdel(key, fieldsName);
  }

  /**
   * Redis Hdel 命令用于删除哈希表 key 多个指定字段，不存在的字段将被忽略。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param fieldsNames
   *        fieldsNames
   * @return 被成功删除字段的数量，不包括被忽略的字段。
   */
  public Long hdel(final String shardingKey, final String key, final String[] fieldsNames) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.hdel(key, fieldsNames);
  }

  /**
   * Redis Hexists 命令用于查看哈希表的指定字段是否存在。
   *
   * @param key
   *        key
   * @param fieldName
   *        fieldName
   * @return 如果哈希表含有给定字段，返回 true 。 如果哈希表不含有给定字段，或 key 不存在，返回 false 。
   */
  public Boolean hexists(final String key, final String fieldName) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.hexists(key, fieldName);
  }

  /**
   * Redis Hexists 命令用于查看哈希表的指定字段是否存在。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param fieldName
   *        fieldName
   * @return 如果哈希表含有给定字段，返回 true 。 如果哈希表不含有给定字段，或 key 不存在，返回 false 。
   */
  public Boolean hexists(final String shardingKey, final String key, final String fieldName) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.hexists(key, fieldName);
  }

  /**
   * Redis Hkeys 命令用于获取哈希表中的所有字段名。
   *
   * @param key
   *        key
   * @return 包含哈希表中所有字段的列表。 当 key 不存在时，返回一个空列表。
   */
  public Set<String> hkeys(final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.hkeys(key);
  }

  /**
   * Redis Hkeys 命令用于获取哈希表中的所有字段名。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @return 包含哈希表中所有字段的列表。 当 key 不存在时，返回一个空列表。
   */
  public Set<String> hkeys(final String shardingKey, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.hkeys(key);
  }

  /**
   * Redis Hlen 命令用于获取哈希表中字段的数量。
   *
   * @param key
   *        key
   * @return 哈希表中字段的数量。 当 key 不存在时，返回 0 。
   */
  public Long hlen(final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.hlen(key);
  }

  /**
   * Redis Hlen 命令用于获取哈希表中字段的数量。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @return 哈希表中字段的数量。 当 key 不存在时，返回 0 。
   */
  public Long hlen(final String shardingKey, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.hlen(key);
  }

  // / List Actions ///

  /**
   * Redis Lpush 命令将一个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
   *
   * @param key
   *        key
   * @param value
   *        value
   * @return 执行 LPUSH 命令后，列表的长度。
   */
  public Long lpush(final String key, final String value) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.lpush(key, value);
  }

  /**
   * Redis Lpush 命令将多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
   *
   * @param key
   *        key
   * @param values
   *        values
   * @return 执行 LPUSH 命令后，列表的长度。
   */
  public Long lpush(final String key, final String[] values) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.lpush(key, values);
  }

  /**
   * Redis Lpush 命令将一个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param value
   *        value
   * @return 执行 LPUSH 命令后，列表的长度。
   */
  public Long lpush(final String shardingKey, final String key, final String value) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.lpush(key, value);
  }

  /**
   * Redis Lpush 命令将多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param values
   *        values
   * @return 执行 LPUSH 命令后，列表的长度。
   */
  public Long lpush(final String shardingKey, final String key, final String[] values) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.lpush(key, values);
  }

  /**
   * Redis Rpop 命令用于移除并返回列表的最后一个元素。
   *
   * @param key
   *        key
   * @return 列表的最后一个元素。 当列表不存在时，返回 nil 。
   */
  public String rpop(final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.rpop(key);
  }

  /**
   * Redis Rpop 命令用于移除并返回列表的最后一个元素。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @return 列表的最后一个元素。 当列表不存在时，返回 nil 。
   */
  public String rpop(final String shardingKey, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.rpop(key);
  }

  /**
   * Redis Brpop 命令移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
   *
   * @param key
   *        key
   * @return 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
   */
  public String brpop(final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.brpop(key);
  }

  /**
   * Redis Brpop 命令移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @return 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
   */
  public String brpop(final String shardingKey, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.brpop(key);
  }

  /**
   * Redis Brpop 命令移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
   *
   * @param timeout
   *        timeout
   * @param key
   *        key
   * @return 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
   */
  public String brpop(final int timeout, final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.brpop(timeout, key);
  }

  /**
   * Redis Brpop 命令移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
   *
   * @param shardingKey
   *        shardingKey
   * @param timeout
   *        timeout
   * @param key
   *        key
   * @return 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
   */
  public String brpop(final String shardingKey, final int timeout, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.brpop(timeout, key);
  }

  /**
   * Redis Llen 命令用于返回列表的长度。 如果列表 key 不存在，则 key 被解释为一个空列表，返回 0 。 如果 key 不是列表类型，返回一个错误。
   *
   * @param key
   *        key
   * @return 列表的长度。
   */
  public Long llen(final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.llen(key);
  }

  /**
   * Redis Llen 命令用于返回列表的长度。 如果列表 key 不存在，则 key 被解释为一个空列表，返回 0 。 如果 key 不是列表类型，返回一个错误。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @return 列表的长度。
   */
  public Long llen(final String shardingKey, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.llen(key);
  }

  /**
   * Redis Lindex 命令用于通过索引获取列表中的元素。你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
   *
   * @param key
   *        key
   * @param index
   *        index
   * @return 列表中下标为指定索引值的元素。 如果指定索引值不在列表的区间范围内，返回 nil 。
   */
  public String lindex(final String key, final long index) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.lindex(key, index);
  }

  /**
   * Redis Lindex 命令用于通过索引获取列表中的元素。你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param index
   *        index
   * @return 列表中下标为指定索引值的元素。 如果指定索引值不在列表的区间范围内，返回 nil 。
   */
  public String lindex(final String shardingKey, final String key, final long index) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.lindex(key, index);
  }

  /**
   * Redis Lrange 返回列表中指定区间内的元素，<br/>
   * 区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。 <br/>
   * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
   *
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 一个列表，包含指定区间内的元素。
   */
  public List<String> lrange(final String key, final int start, final int end) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.lrange(key, start, end);
  }

  /**
   * Redis Lrange 返回列表中指定区间内的元素，<br/>
   * 区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。 <br/>
   * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 一个列表，包含指定区间内的元素。
   */
  public List<String> lrange(final String shardingKey, final String key, final int start, final int end) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.lrange(key, start, end);
  }

  /**
   * Redis Ltrim 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。<br/>
   * 下标 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 <br/>
   * 也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
   *
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 命令执行成功时，返回 true 。否则返回false
   */
  public Boolean ltrim(final String key, final int start, final int end) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.ltrim(key, start, end);
  }

  /**
   * Redis Ltrim 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。<br/>
   * 下标 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 <br/>
   * 也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 命令执行成功时，返回 true 。否则返回false
   */
  public Boolean ltrim(final String shardingKey, final String key, final int start, final int end) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.ltrim(key, start, end);
  }

  /**
   * Redis Ltrim 对一个列表进行修剪(trim).<br/>
   * 从左开始，保留size位元素，其他的删除
   *
   * @param key
   *        key
   * @param size
   *        size
   */
  public void ltrimFromLeft(final String key, final int size) {
    JedisTemplate jedisTemplate = getShard(key);
    jedisTemplate.ltrimFromLeft(key, size);
  }

  /**
   * Redis Ltrim 对一个列表进行修剪(trim).<br/>
   * 从左开始，保留size位元素，其他的删除
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param size
   *        size
   */
  public void ltrimFromLeft(final String shardingKey, final String key, final int size) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    jedisTemplate.ltrimFromLeft(key, size);
  }

  /**
   * Redis Lrem 从表头向表末尾搜索，移除列表中第一个与参数 VALUE 相等的元素。
   *
   * @param key
   *        key
   * @param value
   *        value
   * @return 如果被移除数量为1，返回true
   */
  public Boolean lremFirst(final String key, final String value) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.lremFirst(key, value);
  }

  /**
   * Redis Lrem 从表头向表末尾搜索，移除列表中第一个与参数 VALUE 相等的元素。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param value
   *        value
   * @return 如果被移除数量为1，返回true
   */
  public Boolean lremFirst(final String shardingKey, final String key, final String value) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.lremFirst(key, value);
  }

  /**
   * Redis Lrem 移除表中所有与 VALUE 相等的值。
   *
   * @param key
   *        key
   * @param value
   *        value
   * @return 如果被移除数量大于0，返回true
   */
  public Boolean lremAll(final String key, final String value) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.lremAll(key, value);
  }

  /**
   * Redis Lrem 移除表中所有与 VALUE 相等的值。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param value
   *        value
   * @return 如果被移除数量大于0，返回true
   */
  public Boolean lremAll(final String shardingKey, final String key, final String value) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.lremAll(key, value);
  }

  // / Set Actions ///

  /**
   * Redis Sadd 命令将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略。<br/>
   * 假如集合 key 不存在，则创建一个只包含添加的元素作成员的集合。<br/>
   * 当集合 key 不是集合类型时，返回一个错误。<br/>
   *
   * @param key
   *        key
   * @param member
   *        member
   * @return 被添加到集合中的新元素的数量=1,返回true，不包括被忽略的元素。
   */
  public Boolean sadd(final String key, final String member) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.sadd(key, member);
  }

  /**
   * Redis Sadd 命令将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略。<br/>
   * 假如集合 key 不存在，则创建一个只包含添加的元素作成员的集合。<br/>
   * 当集合 key 不是集合类型时，返回一个错误。<br/>
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param member
   *        member
   * @return 被添加到集合中的新元素的数量=1,返回true，不包括被忽略的元素。
   */
  public Boolean sadd(final String shardingKey, final String key, final String member) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.sadd(key, member);
  }

  /**
   * Redis Smembers 命令返回集合中的所有的成员。 不存在的集合 key 被视为空集合。
   *
   * @param key
   *        key
   * @return 集合中的所有成员。
   */
  public Set<String> smembers(final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.smembers(key);
  }

  /**
   * Redis Smembers 命令返回集合中的所有的成员。 不存在的集合 key 被视为空集合。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @return 集合中的所有成员。
   */
  public Set<String> smembers(final String shardingKey, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.smembers(key);
  }

  // / Sorted Set Actions ///

  /**
   * Redis Zadd 命令用于将一个或多个成员元素及其分数值加入到有序集当中。<br/>
   * 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。<br/>
   * 分数值可以是整数值或双精度浮点数。<br/>
   * 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。<br/>
   * 当 key 存在但不是有序集类型时，返回一个错误。
   *
   * @param key
   *        key
   * @param score
   *        score
   * @param member
   *        member
   * @return return true for add new element, false for only update the score.
   */
  public Boolean zadd(final String key, final double score, final String member) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zadd(key, score, member);
  }

  /**
   * Redis Zadd 命令用于将一个或多个成员元素及其分数值加入到有序集当中。<br/>
   * 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。<br/>
   * 分数值可以是整数值或双精度浮点数。<br/>
   * 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。<br/>
   * 当 key 存在但不是有序集类型时，返回一个错误。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param score
   *        score
   * @param member
   *        member
   * @return return true for add new element, false for only update the score.
   */
  public Boolean zadd(final String shardingKey, final String key, final double score, final String member) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zadd(key, score, member);
  }

  /**
   * Redis Zscore 命令返回有序集中，成员的分数值。 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 nil
   *
   * @param key
   *        key
   * @param member
   *        member
   * @return 成员的分数值.
   */
  public Double zscore(final String key, final String member) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zscore(key, member);
  }

  /**
   * Redis Zscore 命令返回有序集中，成员的分数值。 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 nil
   *
   * @param shardingKey
   *        shardingKey
   * @param shardingKey
   * @param key
   *        key
   * @param member
   *        member
   * @return 成员的分数值.
   */
  public Double zscore(final String shardingKey, final String key, final String member) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zscore(key, member);
  }

  /**
   * Redis Zrank 返回有序集中指定成员的排名。其中有序集成员按分数值递增(从小到大)顺序排列。
   *
   * @param key
   *        key
   * @param member
   *        member
   * @return 如果成员是有序集 key 的成员，返回 member 的排名。 如果成员不是有序集 key 的成员，返回 nil 。
   */
  public Long zrank(final String key, final String member) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zrank(key, member);
  }

  /**
   * Redis Zrank 返回有序集中指定成员的排名。其中有序集成员按分数值递增(从小到大)顺序排列。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param member
   *        member
   * @return 如果成员是有序集 key 的成员，返回 member 的排名。 如果成员不是有序集 key 的成员，返回 nil 。
   */
  public Long zrank(final String shardingKey, final String key, final String member) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zrank(key, member);
  }

  /**
   * Redis Zrevrank 命令返回有序集中成员的排名。其中有序集成员按分数值递减(从大到小)排序。<br/>
   * 排名以 0 为底，也就是说， 分数值最大的成员排名为 0 。<br/>
   * 使用 ZRANK 命令可以获得成员按分数值递增(从小到大)排列的排名。
   *
   * @param key
   *        key
   * @param member
   *        member
   * @return 如果成员是有序集 key 的成员，返回成员的排名。 如果成员不是有序集 key 的成员，返回 nil 。
   */
  public Long zrevrank(final String key, final String member) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zrevrank(key, member);
  }

  /**
   * Redis Zrevrank 命令返回有序集中成员的排名。其中有序集成员按分数值递减(从大到小)排序。<br/>
   * 排名以 0 为底，也就是说， 分数值最大的成员排名为 0 。<br/>
   * 使用 ZRANK 命令可以获得成员按分数值递增(从小到大)排列的排名。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param member
   *        member
   * @return 如果成员是有序集 key 的成员，返回成员的排名。 如果成员不是有序集 key 的成员，返回 nil 。
   */
  public Long zrevrank(final String shardingKey, final String key, final String member) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zrevrank(key, member);
  }

  /**
   * Redis Zcount 命令用于计算有序集合中指定分数区间的成员数量。
   *
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 分数值在 min 和 max 之间的成员的数量。
   */
  public Long zcount(final String key, final double start, final double end) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zcount(key, start, end);
  }

  /**
   * Redis Zcount 命令用于计算有序集合中指定分数区间的成员数量。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 分数值在 min 和 max 之间的成员的数量。
   */
  public Long zcount(final String shardingKey, final String key, final double start, final double end) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zcount(key, start, end);
  }

  /**
   * Redis Zrange 返回有序集中，指定区间内的成员。<br/>
   * 其中成员的位置按分数值递增(从小到大)来排序。<br/>
   * 具有相同分数值的成员按字典序(lexicographical order )来排列。<br/>
   * 如果你需要成员按<br/>
   * 值递减(从大到小)来排列，请使用 ZREVRANGE 命令。<br/>
   * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。<br/>
   * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
   *
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<String> zrange(final String key, final int start, final int end) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zrange(key, start, end);
  }

  /**
   * Redis Zrange 返回有序集中，指定区间内的成员。<br/>
   * 其中成员的位置按分数值递增(从小到大)来排序。<br/>
   * 具有相同分数值的成员按字典序(lexicographical order )来排列。<br/>
   * 如果你需要成员按<br/>
   * 值递减(从大到小)来排列，请使用 ZREVRANGE 命令。<br/>
   * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。<br/>
   * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<String> zrange(final String shardingKey, final String key, final int start, final int end) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zrange(key, start, end);
  }

  /**
   * zrangeWithScores
   *
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<Tuple> zrangeWithScores(final String key, final int start, final int end) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zrangeWithScores(key, start, end);
  }

  /**
   * zrangeWithScores
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<Tuple> zrangeWithScores(final String shardingKey, final String key, final int start, final int end) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zrangeWithScores(key, start, end);
  }

  /**
   * Redis Zrevrange 命令返回有序集中，指定区间内的成员。<br/>
   * 其中成员的位置按分数值递减(从大到小)来排列。<br/>
   * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order)排列。<br/>
   * 除了成员按分数值递减的次序排列这一点外， ZREVRANGE 命令的其他方面和 ZRANGE 命令一样。
   *
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<String> zrevrange(final String key, final int start, final int end) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zrevrange(key, start, end);
  }

  /**
   * Redis Zrevrange 命令返回有序集中，指定区间内的成员。<br/>
   * 其中成员的位置按分数值递减(从大到小)来排列。<br/>
   * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order)排列。<br/>
   * 除了成员按分数值递减的次序排列这一点外， ZREVRANGE 命令的其他方面和 ZRANGE 命令一样。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<String> zrevrange(final String shardingKey, final String key, final int start, final int end) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zrevrange(key, start, end);
  }

  /**
   * zrevrangeWithScores
   *
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<Tuple> zrevrangeWithScores(final String key, final int start, final int end) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zrevrangeWithScores(key, start, end);
  }

  /**
   * zrevrangeWithScores
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<Tuple> zrevrangeWithScores(final String shardingKey, final String key, final int start, final int end) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zrevrangeWithScores(key, start, end);
  }

  /**
   * Redis Zrangebyscore 返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列。<br/>
   * 具有相同分数值的成员按字典序来排列(该属性是有序集提供的，不需要额外的计算)。<br/>
   * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
   *
   * @param key
   *        key
   * @param min
   *        min
   * @param max
   *        max
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<String> zrangeByScore(final String key, final double min, final double max) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zrangeByScore(key, min, max);
  }

  /**
   * Redis Zrangebyscore 返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列。<br/>
   * 具有相同分数值的成员按字典序来排列(该属性是有序集提供的，不需要额外的计算)。<br/>
   * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param min
   *        min
   * @param max
   *        max
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<String> zrangeByScore(final String shardingKey, final String key, final double min, final double max) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zrangeByScore(key, min, max);
  }

  /**
   * Redis Zrangebyscore 返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列。<br/>
   * 具有相同分数值的成员按字典序来排列(该属性是有序集提供的，不需要额外的计算)。<br/>
   * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
   *
   * @param key
   *        key
   * @param min
   *        min
   * @param max
   *        max
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zrangeByScoreWithScores(key, min, max);
  }

  /**
   * Redis Zrangebyscore 返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列。<br/>
   * 具有相同分数值的成员按字典序来排列(该属性是有序集提供的，不需要额外的计算)。<br/>
   * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param min
   *        min
   * @param max
   *        max
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<Tuple> zrangeByScoreWithScores(final String shardingKey, final String key, final double min,
                                            final double max) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zrangeByScoreWithScores(key, min, max);
  }

  /**
   * Redis Zrevrangebyscore 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。<br/>
   * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order )排列。<br/>
   * 除了成员按分数值递减的次序排列这一点外， ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE 命令一样。
   *
   * @param key
   *        key
   * @param max
   *        max
   * @param min
   *        min
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zrevrangeByScore(key, max, min);
  }

  /**
   * Redis Zrevrangebyscore 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。<br/>
   * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order )排列。<br/>
   * 除了成员按分数值递减的次序排列这一点外， ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE 命令一样。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param max
   *        max
   * @param min
   *        min
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<String> zrevrangeByScore(final String shardingKey, final String key, final double max, final double min) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zrevrangeByScore(key, max, min);
  }

  /**
   * Redis Zrevrangebyscore 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。<br/>
   * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order )排列。<br/>
   * 除了成员按分数值递减的次序排列这一点外， ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE 命令一样。
   *
   * @param key
   *        key
   * @param max
   *        max
   * @param min
   *        min
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zrevrangeByScoreWithScores(key, max, min);
  }

  /**
   * Redis Zrevrangebyscore 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。<br/>
   * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order )排列。<br/>
   * 除了成员按分数值递减的次序排列这一点外， ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE 命令一样。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param max
   *        max
   * @param min
   *        min
   * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
   */
  public Set<Tuple> zrevrangeByScoreWithScores(final String shardingKey, final String key, final double max,
                                               final double min) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zrevrangeByScoreWithScores(key, max, min);
  }

  /**
   * Redis Zrem 命令用于移除有序集中的一个或多个成员，不存在的成员将被忽略。<br/>
   * 当 key 存在但不是有序集类型时，返回一个错误。
   *
   * @param key
   *        key
   * @param member
   *        member
   * @return 被成功移除的成员的数量为1时返回true，不包括被忽略的成员
   */
  public Boolean zrem(final String key, final String member) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zrem(key, member);
  }

  /**
   * Redis Zrem 命令用于移除有序集中的一个或多个成员，不存在的成员将被忽略。<br/>
   * 当 key 存在但不是有序集类型时，返回一个错误。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param member
   *        member
   * @return 被成功移除的成员的数量为1时返回true，不包括被忽略的成员
   */
  public Boolean zrem(final String shardingKey, final String key, final String member) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zrem(key, member);
  }

  /**
   * Redis Zremrangebyscore 命令用于移除有序集中，指定分数（score）区间内的所有成员。
   *
   * @param key
   *        key
   * @param min
   *        min
   * @param max
   *        max
   * @return 被移除成员的数量。
   */
  public Long zremByScore(final String key, final double min, final double max) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zremByScore(key, min, max);
  }

  /**
   * Redis Zremrangebyscore 命令用于移除有序集中，指定分数（score）区间内的所有成员。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param min
   *        min
   * @param max
   *        max
   * @return 被移除成员的数量。
   */
  public Long zremByScore(final String shardingKey, final String key, final double min, final double max) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zremByScore(key, min, max);
  }

  /**
   * Redis Zremrangebyrank 命令用于移除有序集中，指定排名(rank)区间内的所有成员。
   *
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 被移除成员的数量。
   */
  public Long zremByRank(final String key, final long start, final long end) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zremByRank(key, start, end);
  }

  /**
   * Redis Zremrangebyrank 命令用于移除有序集中，指定排名(rank)区间内的所有成员。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 被移除成员的数量。
   */
  public Long zremByRank(final String shardingKey, final String key, final long start, final long end) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zremByRank(key, start, end);
  }

  /**
   * Redis Zcard 命令用于计算集合中元素的数量。
   *
   * @param key
   *        key
   * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0
   */
  public Long zcard(final String key) {
    JedisTemplate jedisTemplate = getShard(key);
    return jedisTemplate.zcard(key);
  }

  /**
   * Redis Zcard 命令用于计算集合中元素的数量。
   *
   * @param shardingKey
   *        shardingKey
   * @param key
   *        key
   * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0
   */
  public Long zcard(final String shardingKey, final String key) {
    JedisTemplate jedisTemplate = getShard(shardingKey);
    return jedisTemplate.zcard(key);
  }
}
