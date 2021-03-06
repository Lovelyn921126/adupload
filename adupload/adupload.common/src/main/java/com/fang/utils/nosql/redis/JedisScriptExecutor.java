package com.fang.utils.nosql.redis;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import com.fang.utils.nosql.redis.JedisTemplate.JedisAction;
import com.fang.utils.nosql.redis.pool.JedisPool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

import com.google.common.base.Preconditions;

/**
 * 装载并执行Lua Script，
 *
 * 如果服务器上因为集群多台服务器或重启等原因没有装载script，会自动重新装载后重试。
 *
 * 因为涉及到的key未知，暂时不支持Sharding。
 *
 * @author wangzhiyuan
 */
public class JedisScriptExecutor {

  /**
   * logger
   */
  private static Logger logger = LoggerFactory.getLogger(JedisScriptExecutor.class);

  /**
   * jedisTemplate
   */
  private JedisTemplate jedisTemplate;

  /**
   * script
   */
  private String script;

  /**
   * sha1
   */
  private String sha1;

  /**
   * JedisScriptExecutor
   *
   * @param jedisPool
   *        jedisPool
   */
  public JedisScriptExecutor(JedisPool jedisPool) {
    this.jedisTemplate = new JedisTemplate(jedisPool);
  }

  /**
   * JedisScriptExecutor
   *
   * @param jedisTemplate
   *        jedisTemplate
   */
  public JedisScriptExecutor(JedisTemplate jedisTemplate) {
    this.jedisTemplate = jedisTemplate;
  }

  /**
   * 装载Lua Script。
   * 如果Script出错，抛出JedisDataException。
   *
   * @param scriptContent
   *        scriptContent
   * @throws JedisDataException
   *         JedisDataException
   */
  public void load(final String scriptContent) throws JedisDataException {
    sha1 = jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {

      @Override
      public String action(Jedis jedis) {
        return jedis.scriptLoad(scriptContent);
      }
    });
    script = scriptContent;

    logger.debug("Script \"{}\" had been loaded as {}", scriptContent, sha1);
  }

  /**
   * 从文件加载Lua Script, 文件路径格式为Spring Resource的格式.
   *
   * @param scriptPath
   *        scriptPath
   * @throws JedisDataException
   *         JedisDataException
   */
  public void loadFromFile(final String scriptPath) throws JedisDataException {
    String scriptContent;
    try {
      Resource resource = new DefaultResourceLoader().getResource(scriptPath);
      scriptContent = FileUtils.readFileToString(resource.getFile(), "UTF-8");
    } catch (IOException e) {
      throw new IllegalArgumentException(scriptPath + " is not exist.", e);
    }

    load(scriptContent);
  }

  /**
   * 执行Lua Script, 如果Redis服务器上还没装载Script则自动装载并重试。
   * keys与args不允许为null.
   *
   * @param keys
   *        keys
   * @param args
   *        args
   * @return 执行Lua Script
   * @throws IllegalArgumentException
   *         IllegalArgumentException
   */
  public Object execute(final String[] keys, final String[] args) throws IllegalArgumentException {
    Preconditions.checkNotNull(keys, "keys can't be null.");
    Preconditions.checkNotNull(args, "args can't be null.");
    return execute(Arrays.asList(keys), Arrays.asList(args));
  }

  /**
   * 执行Lua Script, 如果Redis服务器上还没装载Script则自动装载并重试。
   * keys与args不允许为null.
   *
   * @param keys
   *        keys
   * @param args
   *        args
   * @return 执行Lua Script
   * @throws IllegalArgumentException
   *         IllegalArgumentException
   */
  public Object execute(final List<String> keys, final List<String> args) throws IllegalArgumentException {
    Preconditions.checkNotNull(keys, "keys can't be null.");
    Preconditions.checkNotNull(args, "args can't be null.");

    return jedisTemplate.execute(new JedisAction<Object>() {

      @Override
      public Object action(Jedis jedis) {
        try {
          return jedis.evalsha(sha1, keys, args);
        } catch (JedisDataException e) {
          logger.warn(
              "Script {} is not loaded in server yet or the script is wrong, try to reload and run it again.",
              script, e);
          return jedis.eval(script, keys, args);
        }
      }
    });
  }
}
