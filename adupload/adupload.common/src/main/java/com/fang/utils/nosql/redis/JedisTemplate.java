package com.fang.utils.nosql.redis;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fang.utils.nosql.redis.pool.JedisPool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

/**
 * JedisTemplate 提供了一个template方法，负责对Jedis连接的获取与归还。
 * JedisAction<T> 和 JedisActionNoResult两种回调接口，适用于有无返回值两种情况。
 * PipelineAction 与 PipelineActionResult两种接口，适合于pipeline中批量传输命令的情况。
 *
 * 同时提供一些JedisOperation中定义的 最常用函数的封装, 如get/set/zadd等。
 *
 * @author wangzhiyuan
 */
public class JedisTemplate {

  /**
   * logger
   */
  private static Logger logger = LoggerFactory.getLogger(JedisTemplate.class);

  /**
   * jedisPool
   */
  private JedisPool jedisPool;

  /**
   * 构造
   *
   * @param jedisPool
   *        jedisPool
   */
  public JedisTemplate(JedisPool jedisPool) {
    this.jedisPool = jedisPool;
  }

  /**
   * Callback interface for template.
   *
   * @author wangzhiyuan
   * @param <T>
   *        泛型
   */
  public interface JedisAction<T> {

    /**
     * action
     *
     * @param jedis
     *        jedis
     * @return T类型
     */
    T action(Jedis jedis);
  }

  /**
   * Callback interface for template without result.
   *
   * @author wangzhiyuan
   */
  public interface JedisActionNoResult {

    /**
     * action
     *
     * @param jedis
     *        jedis
     */
    void action(Jedis jedis);
  }

  /**
   * Callback interface for template.
   *
   * @author wangzhiyuan
   */
  public interface PipelineAction {

    /**
     * action
     *
     * @param pipeline
     *        pipeline
     * @return List<Object>s
     */
    List<Object> action(Pipeline pipeline);
  }

  /**
   * Callback interface for template without result.
   *
   * @author wangzhiyuan
   */
  public interface PipelineActionNoResult {

    /**
     * action
     *
     * @param pipeline
     *        pipeline
     */
    void action(Pipeline pipeline);
  }

  /**
   * Execute with a call back action with result.
   *
   * @param jedisAction
   *        jedisAction
   * @param <T>
   *        泛型
   * @return T
   * @throws JedisException
   *         JedisException
   */
  public <T> T execute(JedisAction<T> jedisAction) throws JedisException {
    Jedis jedis = null;
    boolean broken = false;
    try {
      jedis = jedisPool.getResource();
      return jedisAction.action(jedis);
    } catch (JedisException e) {
      return execute(jedisAction, 3);
      // broken = handleJedisException(e);
      // throw e;
    } finally {
      closeResource(jedis, broken);
    }
  }

  /**
   * Execute with a call back action with result.
   *
   * @param jedisAction
   *        jedisAction
   * @param tryTimes
   *        尝试次数
   * @param <T>
   *        泛型
   * @return T
   * @throws JedisException
   *         JedisException
   */
  public <T> T execute(JedisAction<T> jedisAction, int tryTimes) throws JedisException {
    Jedis jedis = null;
    boolean broken = false;
    try {
      jedis = jedisPool.getResource();
      return jedisAction.action(jedis);
    } catch (JedisException e) {
      if (tryTimes != 0) {
        tryTimes--;
        return execute(jedisAction, tryTimes);
      }
      broken = handleJedisException(e);
      throw e;
    } finally {
      closeResource(jedis, broken);
    }
  }

  /**
   * Execute with a call back action without result.
   *
   * @param jedisAction
   *        jedisAction
   * @throws JedisException
   *         JedisException
   */
  public void execute(JedisActionNoResult jedisAction) throws JedisException {
    Jedis jedis = null;
    boolean broken = false;
    try {
      jedis = jedisPool.getResource();
      jedisAction.action(jedis);
    } catch (JedisException e) {
      broken = handleJedisException(e);
      throw e;
    } finally {
      closeResource(jedis, broken);
    }
  }

  /**
   * Execute with a call back action with result in pipeline.
   *
   * @param pipelineAction
   *        pipelineAction
   * @return List<Object>
   * @throws JedisException
   *         JedisException
   */
  public List<Object> execute(PipelineAction pipelineAction) throws JedisException {
    Jedis jedis = null;
    boolean broken = false;
    try {
      jedis = jedisPool.getResource();
      Pipeline pipeline = jedis.pipelined();
      pipelineAction.action(pipeline);
      return pipeline.syncAndReturnAll();
    } catch (JedisException e) {
      broken = handleJedisException(e);
      throw e;
    } finally {
      closeResource(jedis, broken);
    }
  }

  /**
   * Execute with a call back action without result in pipeline.
   *
   * @param pipelineAction
   *        pipelineAction
   * @throws JedisException
   *         JedisException
   */
  public void execute(PipelineActionNoResult pipelineAction) throws JedisException {
    Jedis jedis = null;
    boolean broken = false;
    try {
      jedis = jedisPool.getResource();
      Pipeline pipeline = jedis.pipelined();
      pipelineAction.action(pipeline);
      pipeline.sync();
    } catch (JedisException e) {
      broken = handleJedisException(e);
      throw e;
    } finally {
      closeResource(jedis, broken);
    }
  }

  /**
   * Return the internal JedisPool.
   *
   * @return jedisPool
   */
  public JedisPool getJedisPool() {
    return jedisPool;
  }

  /**
   * Handle jedisException, write log and return whether the connection is broken.
   *
   * @param jedisException
   *        jedisException
   * @return true/false
   */
  protected boolean handleJedisException(JedisException jedisException) {
    if (jedisException instanceof JedisConnectionException) {
      logger.error("Redis connection " + jedisPool.getAddress() + " lost.", jedisException);
    } else if (jedisException instanceof JedisDataException) {
      if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1)) {
        logger.error("Redis connection " + jedisPool.getAddress() + " are read-only slave.", jedisException);
      } else {
        // dataException, isBroken=false
        return false;
      }
    } else {
      logger.error("Jedis exception happen.", jedisException);
    }
    return true;
  }

  /**
   * Return jedis connection to the pool, call different return methods depends on the
   * conectionBroken status.
   *
   * @param jedis
   *        jedis
   * @param conectionBroken
   *        conectionBroken
   */
  protected void closeResource(Jedis jedis, boolean conectionBroken) {
    try {
      if (conectionBroken) {
        jedisPool.returnBrokenResource(jedis);
      } else {
        jedisPool.returnResource(jedis);
      }
    } catch (Exception e) {
      logger.error("return back jedis failed, will fore close the jedis.", e);
      JedisUtils.destroyJedis(jedis);
    }

  }

  // / Common Actions ///

  /**
   * Redis DEL 命令用于删除已存在的键。不存在的 key 会被忽略。
   *
   * @param keys
   *        keys
   * @return 如果被删除键的数量等于key的个数，返回true，否则返回false
   */
  public Boolean del(final String... keys) {
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return jedis.del(keys) == keys.length ? true : false;
      }
    });
  }

  /**
   * Redis EXISTS 命令用于检查给定 key 是否存在。
   *
   * @param key
   *        key
   * @return 若 key 存在返回 true ，否则返回false 。
   */
  public Boolean exists(final String key) {
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return jedis.exists(key);
      }
    });
  }

  /**
   * Redis Keys 命令用于查找所有符合给定模式 pattern 的 key
   *
   * @param pattern
   *        pattern
   * @return 符合给定模式的 key 列表 (Array)。
   */
  public Set<String> keys(final String pattern) {
    return execute(new JedisAction<Set<String>>() {

      @Override
      public Set<String> action(Jedis jedis) {
        return jedis.keys(pattern);
      }
    });
  }

  /**
   * Redis Type 命令用于返回 key 所储存的值的类型。
   *
   * @param key
   *        key
   * @return 返回 key 的数据类型，数据类型有：<br/>
   *
   *         none (key不存在)<br/>
   *         string (字符串)<br/>
   *         list (列表)<br/>
   *         set (集合)<br/>
   *         zset (有序集)<br/>
   *         hash (哈希表)
   */
  public String type(final String key) {
    return execute(new JedisAction<String>() {

      @Override
      public String action(Jedis jedis) {
        return jedis.type(key);
      }
    });
  }

  /**
   * Redis TTL 命令以秒为单位返回 key 的剩余过期时间。
   *
   * @param key
   *        key
   * @return 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key 的剩余生存时间。<br/>
   *
   *         注意：在 Redis 2.8 以前，当 key 不存在，或者 key 没有设置剩余生存时间时，命令都返回 -1 。
   */
  public Long ttl(final String key) {
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.ttl(key);
      }
    });
  }

  /**
   * Redis Expire 命令用于设置 key 的过期时间。key 过期后将不再可用。(-1,表示取消过期时间)
   *
   * @param key
   *        key
   * @param seconds
   *        seconds
   * @return 设置成功返回 1 。 当 key 不存在或者不能为 key 设置过期时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的过期时间)返回 0 。
   */
  public Boolean expire(final String key, final int seconds) {
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return jedis.expire(key, seconds) == 1 ? true : false;
      }
    });
  }

  /**
   * Redis PERSIST 命令用于移除给定 key 的过期时间，使得 key 永不过期。
   *
   * @param key
   *        key
   * @return 当过期时间移除成功时，返回 1 。 如果 key 不存在或 key 没有设置过期时间，返回 0 。
   */
  public Boolean persist(final String key) {
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return jedis.persist(key) == 1 ? true : false;
      }
    });
  }

  /**
   * Redis Renamenx 命令用于在新的 key 不存在时修改 key 的名称 。
   *
   * @param oldkey
   *        oldkey
   * @param newkey
   *        newkey
   * @return 修改成功时，返回 1 。 如果 NEW_KEY_NAME 已经存在，返回 0 。 。
   */
  public Long renamenx(final String oldkey, final String newkey) {
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.renamenx(oldkey, newkey);
      }
    });
  }

  /**
   * Redis Flushdb 命令用于清空当前数据库中的所有 key。
   */
  public void flushDB() {
    execute(new JedisActionNoResult() {

      @Override
      public void action(Jedis jedis) {
        jedis.flushDB();
      }
    });
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
    return execute(new JedisAction<String>() {

      @Override
      public String action(Jedis jedis) {
        return jedis.get(key);
      }
    });
  }

  /**
   * Redis Get 命令用于获取指定 key 的值，此值是Long类型。如果 key 不存在，返回 nil 。如果key 储存的值不是Long类型，返回一个错误。
   *
   * @param key
   *        key
   * @return 返回 key 的值，如果 key 不存在，返回 null 。如果key 储存的值不是Long类型，返回一个错误。
   */
  public Long getAsLong(final String key) {
    String result = get(key);
    return result != null ? Long.valueOf(result) : null;
  }

  /**
   * Redis Get 命令用于获取指定 key 的值，此值是Integer类型。如果 key 不存在，返回 null 。如果key 储存的值不是Integer类型，返回一个错误。
   *
   * @param key
   *        key
   * @return 返回 key 的值，如果 key 不存在，返回 null 。如果key 储存的值不是Integer类型，返回一个错误。
   */
  public Integer getAsInt(final String key) {
    String result = get(key);
    return result != null ? Integer.valueOf(result) : null;
  }

  /**
   * Redis Mget 命令返回所有(一个或多个)给定 key 的值。 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 nil 。
   *
   * @param keys
   *        keys
   * @return 一个包含所有给定 key 的值的列表。
   */
  public List<String> mget(final String... keys) {
    return execute(new JedisAction<List<String>>() {

      @Override
      public List<String> action(Jedis jedis) {
        return jedis.mget(keys);
      }
    });
  }

  /**
   * Redis SET 命令用于设置给定 key 的值。如果 key 已经存储其他值， SET 就覆写旧值，且无视类型。
   * The string can't be longer than 1073741824 bytes (1 GB).
   *
   * @param key
   *        key
   * @param value
   *        value
   * @return 设置成功时返回 true,否则返回false
   */
  public Boolean set(final String key, final String value) {
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return JedisUtils.isStatusOk(jedis.set(key, value));
      }
    });
  }

  /**
   * The command is exactly equivalent to the following group of commands:
   * {@link #set(String, String) SET} + {@link #expire(String, int) EXPIRE}.
   * The operation is atomic.
   *
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
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return jedis.setex(key, seconds, value).equals("OK") ? true : false;
      }
    });
  }

  /**
   * SETNX works exactly like {@link #setNX(String, String) SET} with the only
   * difference that if the key already exists no operation is performed.
   * SETNX actually means "SET if Not eXists".
   *
   * return true if the key was set.
   *
   * Redis Setnx（SET if Not eXists） 命令在指定的 key 不存在时，为 key 设置指定的值.
   *
   * @param key
   *        key
   * @param value
   *        value
   * @return 设置成功时返回 true,否则返回false
   */
  public Boolean setnx(final String key, final String value) {
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return jedis.setnx(key, value) == 1 ? true : false;
      }
    });
  }

  /**
   * The command is exactly equivalent to the following group of commands:
   * {@link #setex(String, String, int) SETEX} + {@link #sexnx(String, String) SETNX}.
   * The operation is atomic.
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
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        String result = jedis.set(key, value, "NX", "EX", seconds);
        return JedisUtils.isStatusOk(result);
      }
    });
  }

  /**
   * Redis Getset 命令用于设置指定 key 的值，并返回 key 旧的值。<br/>
   * The string can't be longer than 1073741824 bytes (1 GB).
   *
   * @param key
   *        key
   * @param value
   *        value
   * @return 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 nil 。<br/>
   *         当 key 存在但不是字符串类型时，返回一个错误。
   */
  public String getSet(final String key, final String value) {
    return execute(new JedisAction<String>() {

      @Override
      public String action(Jedis jedis) {
        return jedis.getSet(key, value);
      }
    });
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
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.incr(key);
      }
    });
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
  public Long incrBy(final String key, final long increment) {
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.incrBy(key, increment);
      }
    });
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
    return execute(new JedisAction<Double>() {

      @Override
      public Double action(Jedis jedis) {
        return jedis.incrByFloat(key, increment);
      }
    });
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
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.decr(key);
      }
    });
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
  public Long decrBy(final String key, final long decrement) {
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.decrBy(key, decrement);
      }
    });
  }

  // / Hash Actions ///

  /**
   * Redis Hget 命令用于返回哈希表中指定字段的值。
   *
   * @param key
   *        key
   * @param fieldName
   *        fieldName
   * @return 返回给定字段的值。如果给定的字段或 key 不存在时，返回 nil 。
   */
  public String hget(final String key, final String fieldName) {
    return execute(new JedisAction<String>() {

      @Override
      public String action(Jedis jedis) {
        return jedis.hget(key, fieldName);
      }
    });
  }

  /**
   * Redis Hmget 命令用于返回哈希表中，一个给定字段的值。<br/>
   * 如果指定的字段不存在于哈希表，那么返回一个 nil 值。
   *
   * @param key
   *        key
   * @param fieldsNames
   *        fieldsNames
   * @return 一个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样。
   */
  public List<String> hmget(final String key, final String... fieldsNames) {
    return execute(new JedisAction<List<String>>() {

      @Override
      public List<String> action(Jedis jedis) {
        return jedis.hmget(key, fieldsNames);
      }
    });
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
    return execute(new JedisAction<Map<String, String>>() {

      @Override
      public Map<String, String> action(Jedis jedis) {
        return jedis.hgetAll(key);
      }
    });
  }

  /**
   * Redis Hset 命令用于为哈希表中的字段赋值 。<br/>
   * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。<br/>
   * 如果字段已经存在于哈希表中，旧值将被覆盖。
   *
   * @param key
   *        key
   * @param fieldName
   *        fieldName
   * @param value
   *        value
   */
  public void hset(final String key, final String fieldName, final String value) {
    execute(new JedisActionNoResult() {

      /**
       * 如果字段是哈希表中的一个新建字段，并且值设置成功，返回 1 。 如果哈希表中域字段已经存在且旧值已被新值覆盖，返回 0 。
       */
      @Override
      public void action(Jedis jedis) {
        jedis.hset(key, fieldName, value);
      }
    });
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
    return execute(new JedisAction<Boolean>() {

      /**
       * 如果命令执行成功，返回 OK 。
       */
      @Override
      public Boolean action(Jedis jedis) {
        return JedisUtils.isStatusOk(jedis.hmset(key, map));
      }
    });

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
    return execute(new JedisAction<Boolean>() {

      /**
       * 设置成功，返回 1 。 如果给定字段已经存在且没有操作被执行，返回 0 。
       */
      @Override
      public Boolean action(Jedis jedis) {
        return jedis.hsetnx(key, fieldName, value) == 1 ? true : false;
      }
    });
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
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.hincrBy(key, fieldName, increment);
      }
    });
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
    return execute(new JedisAction<Double>() {

      @Override
      public Double action(Jedis jedis) {
        return jedis.hincrByFloat(key, fieldName, increment);
      }
    });
  }

  /**
   * Redis Hdel 命令用于删除哈希表 key 中的一个或多个指定字段，不存在的字段将被忽略。
   *
   * @param key
   *        key
   * @param fieldsNames
   *        fieldsNames
   * @return 被成功删除字段的数量，不包括被忽略的字段。
   */
  public Long hdel(final String key, final String... fieldsNames) {
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.hdel(key, fieldsNames);
      }
    });
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
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return jedis.hexists(key, fieldName);
      }
    });
  }

  /**
   * Redis Hkeys 命令用于获取哈希表中的所有字段名。
   *
   * @param key
   *        key
   * @return 包含哈希表中所有字段的列表。 当 key 不存在时，返回一个空列表。
   */
  public Set<String> hkeys(final String key) {
    return execute(new JedisAction<Set<String>>() {

      @Override
      public Set<String> action(Jedis jedis) {
        return jedis.hkeys(key);
      }
    });
  }

  /**
   * Redis Hlen 命令用于获取哈希表中字段的数量。
   *
   * @param key
   *        key
   * @return 哈希表中字段的数量。 当 key 不存在时，返回 0 。
   */
  public Long hlen(final String key) {
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.hlen(key);
      }
    });
  }

  /**
   * Redis hscan 用于迭代遍历指定哈希类型（Hash type）对象中的字段（Field）及关联的值。
   *
   * @param key
   *        key
   * @param cursor
   *        游标
   * @param pattern
   *        匹配模式
   * @param count
   *        限定数量
   * @return 返回的元素数组包含两部份，对于哈希对象中返回的每个元素，都输出一个字段及一个值。
   */
  public ScanResult<Entry<String, String>> hscan(final String key, final String cursor, final String pattern, final int count) {
    final ScanParams params = new ScanParams();
    params.match(pattern == null || pattern.length() == 0 ? "*" : pattern);
    params.count(count);
    return execute(new JedisAction<ScanResult<Entry<String, String>>>() {

      @Override
      public ScanResult<Entry<String, String>> action(Jedis jedis) {
        return jedis.hscan(key, cursor, params);
      }
    });
  }

  // / List Actions ///

  /**
   * Redis Lpush 命令将一个或多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
   *
   * @param key
   *        key
   * @param values
   *        values
   * @return 执行 LPUSH 命令后，列表的长度。
   */
  public Long lpush(final String key, final String... values) {
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.lpush(key, values);
      }
    });
  }

  /**
   * Redis Rpop 命令用于移除并返回列表的最后一个元素。
   *
   * @param key
   *        key
   * @return 列表的最后一个元素。 当列表不存在时，返回 nil 。
   */
  public String rpop(final String key) {
    return execute(new JedisAction<String>() {

      @Override
      public String action(Jedis jedis) {
        return jedis.rpop(key);
      }
    });
  }

  /**
   * Redis Lpop 命令用于移除并返回列表的第一个元素。
   *
   * @param key
   *        key
   * @return 列表的最后一个元素。 当列表不存在时，返回 nil 。
   */
  public String lpop(final String key) {
    return execute(new JedisAction<String>() {

      @Override
      public String action(Jedis jedis) {
        return jedis.lpop(key);
      }
    });
  }

  /**
   * Redis Brpop 命令移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
   *
   * @param key
   *        key
   * @return 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
   */
  public String brpop(final String key) {
    return execute(new JedisAction<String>() {

      @Override
      public String action(Jedis jedis) {
        @SuppressWarnings("deprecation")
        List<String> nameValuePair = jedis.brpop(key);
        if (nameValuePair != null) {
          return nameValuePair.get(1);
        } else {
          return null;
        }
      }
    });
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
    return execute(new JedisAction<String>() {

      @Override
      public String action(Jedis jedis) {
        List<String> nameValuePair = jedis.brpop(timeout, key);
        if (nameValuePair != null) {
          return nameValuePair.get(1);
        } else {
          return null;
        }
      }
    });
  }

  /**
   * Redis Brpoplpush 命令从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
   *
   * @param sourceKey
   *        sourceKey
   * @param destinationKey
   *        destinationKey
   * @return 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素的值，第二个元素是等待时长
   */
  public String rpoplpush(final String sourceKey, final String destinationKey) {
    return execute(new JedisAction<String>() {

      @Override
      public String action(Jedis jedis) {
        return jedis.rpoplpush(sourceKey, destinationKey);
      }
    });
  }

  /**
   * Redis Brpoplpush 命令从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
   *
   * @param source
   *        source
   * @param destination
   *        destination
   * @param timeout
   *        timeout
   * @return 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素的值，第二个元素是等待时长
   */
  public String brpoplpush(final String source, final String destination, final int timeout) {
    return execute(new JedisAction<String>() {

      @Override
      public String action(Jedis jedis) {
        return jedis.brpoplpush(source, destination, timeout);
      }
    });
  }

  /**
   * Redis Llen 命令用于返回列表的长度。 如果列表 key 不存在，则 key 被解释为一个空列表，返回 0 。 如果 key 不是列表类型，返回一个错误。
   *
   * @param key
   *        key
   * @return 列表的长度。
   */
  public Long llen(final String key) {
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.llen(key);
      }
    });
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
    return execute(new JedisAction<String>() {

      @Override
      public String action(Jedis jedis) {
        return jedis.lindex(key, index);
      }
    });
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
    return execute(new JedisAction<List<String>>() {

      @Override
      public List<String> action(Jedis jedis) {
        return jedis.lrange(key, start, end);
      }
    });
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
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return JedisUtils.isStatusOk(jedis.ltrim(key, start, end));
      }
    });
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
    execute(new JedisActionNoResult() {

      @Override
      public void action(Jedis jedis) {
        jedis.ltrim(key, 0, size - 1);
      }
    });
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
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        Long count = jedis.lrem(key, 1, value);
        return (count == 1);
      }
    });
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
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        Long count = jedis.lrem(key, 0, value);
        return (count > 0);
      }
    });
  }

  /**
   * Redis Lset 通过索引来设置元素的值。
   *
   * 当索引参数超出范围，或对一个空列表进行 LSET 时，返回一个错误。
   *
   * @param key
   *        key
   * @param index
   *        index
   * @param value
   *        value
   * @return 操作成功返回 true ，否则返回错误信息。
   */
  public Boolean lset(final String key, final long index, final String value) {
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return JedisUtils.isStatusOk(jedis.lset(key, index, value));
      }
    });
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
  public Boolean sadd(final String key, final String... member) {
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return jedis.sadd(key, member) == 1 ? true : false;
      }
    });
  }

  /**
   * Redis Scard 命令返回集合中元素的数量。 <br/>
   * 集合的数量。 当集合 key 不存在时，返回 0 。
   *
   * @param key
   *        key
   * @return 集合的数量。 当集合 key 不存在时，返回 0 。
   */
  public Long scard(final String key) {
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.scard(key);
      }
    });
  }

  /**
   * Redis Spop 命令用于移除并返回集合中的一个随机元素。 <br/>
   * 被移除的随机元素。 当集合不存在或是空集时，返回 nil 。
   *
   * @param key
   *        key
   * @return 被移除的随机元素。 当集合不存在或是空集时，返回 nil 。
   */
  public String spop(final String key) {
    return execute(new JedisAction<String>() {

      @Override
      public String action(Jedis jedis) {
        return jedis.spop(key);
      }
    });
  }

  /**
   * Redis Smembers 命令返回集合中的所有的成员。 不存在的集合 key 被视为空集合。
   *
   * @param key
   *        key
   * @return 集合中的所有成员。
   */
  public Set<String> smembers(final String key) {
    return execute(new JedisAction<Set<String>>() {

      @Override
      public Set<String> action(Jedis jedis) {
        return jedis.smembers(key);
      }
    });
  }

  /**
   * Redis Sismember 命令判断成员元素是否是集合的成员。
   *
   * @param key
   *        key
   * @param member
   *        集合中的所有成员。
   * @return 如果成员元素是集合的成员，返回 true 。 如果成员元素不是集合的成员，或 key 不存在，返回false 。
   */
  public Boolean sismember(final String key, final String member) {
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return jedis.sismember(key, member);
      }
    });
  }

  /**
   * Redis Srem 命令用于移除集合中的一个或多个成员元素，不存在的成员元素会被忽略。<br/>
   * 当 key 不是集合类型，返回一个错误。<br/>
   *
   * @param key
   *        key
   * @param member
   *        member
   * @return 被添加到集合中的新元素的数量=1,返回true，不包括被忽略的元素。
   */
  public Boolean srem(final String key, final String... member) {
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return jedis.srem(key, member) == 1 ? true : false;
      }
    });
  }

  /**
   * Redis sscan 用于迭代遍历指定集合类型（Sets type）对象中的元素。
   *
   * @param key
   *        key
   * @param cursor
   *        游标
   * @param pattern
   *        匹配模式
   * @param count
   *        限定数量
   * @return 返回的元素数组为一个集合成员的列表。
   */
  public ScanResult<String> sscan(final String key, final String cursor, final String pattern, final int count) {
    final ScanParams params = new ScanParams();
    params.match(pattern == null || pattern.length() == 0 ? "*" : pattern);
    params.count(count);
    return execute(new JedisAction<ScanResult<String>>() {

      @Override
      public ScanResult<String> action(Jedis jedis) {
        return jedis.sscan(key, cursor, params);
      }
    });
  }

  // / Ordered Set Actions ///

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
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return jedis.zadd(key, score, member) == 1 ? true : false;
      }
    });
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
    return execute(new JedisAction<Double>() {

      @Override
      public Double action(Jedis jedis) {
        return jedis.zscore(key, member);
      }
    });
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
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.zrank(key, member);
      }
    });
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
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.zrevrank(key, member);
      }
    });
  }

  /**
   * Redis Zcount 命令用于计算有序集合中指定分数区间的成员数量。
   *
   * @param key
   *        key
   * @param min
   *        min
   * @param max
   *        max
   * @return 分数值在 min 和 max 之间的成员的数量。
   */
  public Long zcount(final String key, final double min, final double max) {
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.zcount(key, min, max);
      }
    });
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
    return execute(new JedisAction<Set<String>>() {

      @Override
      public Set<String> action(Jedis jedis) {
        return jedis.zrange(key, start, end);
      }
    });
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
    return execute(new JedisAction<Set<Tuple>>() {

      @Override
      public Set<Tuple> action(Jedis jedis) {
        return jedis.zrangeWithScores(key, start, end);
      }
    });
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
    return execute(new JedisAction<Set<String>>() {

      @Override
      public Set<String> action(Jedis jedis) {
        return jedis.zrevrange(key, start, end);
      }
    });
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
    return execute(new JedisAction<Set<Tuple>>() {

      @Override
      public Set<Tuple> action(Jedis jedis) {
        return jedis.zrevrangeWithScores(key, start, end);
      }
    });
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
    return execute(new JedisAction<Set<String>>() {

      @Override
      public Set<String> action(Jedis jedis) {
        return jedis.zrangeByScore(key, min, max);
      }
    });
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
    return execute(new JedisAction<Set<Tuple>>() {

      @Override
      public Set<Tuple> action(Jedis jedis) {
        return jedis.zrangeByScoreWithScores(key, min, max);
      }
    });
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
    return execute(new JedisAction<Set<String>>() {

      @Override
      public Set<String> action(Jedis jedis) {
        return jedis.zrevrangeByScore(key, max, min);
      }
    });
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
    return execute(new JedisAction<Set<Tuple>>() {

      @Override
      public Set<Tuple> action(Jedis jedis) {
        return jedis.zrevrangeByScoreWithScores(key, max, min);
      }
    });
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
    return execute(new JedisAction<Boolean>() {

      @Override
      public Boolean action(Jedis jedis) {
        return jedis.zrem(key, member) == 1 ? true : false;
      }
    });
  }

  /**
   * Redis Zremrangebyscore 命令用于移除有序集中，指定分数（score）区间内的所有成员。
   *
   * @param key
   *        key
   * @param start
   *        start
   * @param end
   *        end
   * @return 被移除成员的数量。
   */
  public Long zremByScore(final String key, final double start, final double end) {
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.zremrangeByScore(key, start, end);
      }
    });
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
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.zremrangeByRank(key, start, end);
      }
    });
  }

  /**
   * Redis Zcard 命令用于计算集合中元素的数量。
   *
   * @param key
   *        key
   * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0
   */
  public Long zcard(final String key) {
    return execute(new JedisAction<Long>() {

      @Override
      public Long action(Jedis jedis) {
        return jedis.zcard(key);
      }
    });
  }
}
