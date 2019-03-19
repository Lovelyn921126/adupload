package com.fang.utils.nosql.redis.service.scheduler;

import java.util.List;

import com.fang.utils.concurrent.ThreadUtil;
import com.fang.utils.nosql.redis.JedisScriptExecutor;
import com.fang.utils.nosql.redis.JedisTemplate;
import com.fang.utils.nosql.redis.pool.JedisPool;

import redis.clients.jedis.exceptions.JedisConnectionException;

import com.google.common.collect.Lists;

/**
 * 高级的使用Lua脚本取回任务，支持高可靠性和批量取回任务，但不会阻塞，如果没有任务即时返回。
 *
 * 在高可靠模式下，任务在返回给客户端的同时，会放入lock table中，客户完成任务后必须调用ack()删除任务，否则Dispatcher会将超时未完成的任务放入队列重新执行.
 *
 * @author wangzhiyuan
 */
public class AdvancedJobConsumer {

  /**
   * DEFAULT_BATCH_POP_LUA_FILE_PATH
   */
  public static final String DEFAULT_BATCH_POP_LUA_FILE_PATH = "classpath:com/fang/**/redis/batchpop.lua";

  /**
   * DEFAULT_SINGLE_POP_LUA_FILE_PATH
   */
  public static final String DEFAULT_SINGLE_POP_LUA_FILE_PATH = "classpath:com/fang/**/redis/singlepop.lua";

  /**
   * DEFAULT_CONNECTION_RETRY_MILLS
   */
  public static final int DEFAULT_CONNECTION_RETRY_MILLS = 5000;

  /**
   * DEFAULT_RELIABLE
   */
  public static final boolean DEFAULT_RELIABLE = false;

  /**
   * DEFAULT_BATCH_SIZE
   */
  public static final int DEFAULT_BATCH_SIZE = 10;

  /**
   * DEFAULT_RELIABLE
   */
  private boolean reliable = DEFAULT_RELIABLE;

  /**
   * DEFAULT_BATCH_SIZE
   */
  private int batchSize = DEFAULT_BATCH_SIZE;

  /**
   * jedisTemplate
   */
  private JedisTemplate jedisTemplate;

  /**
   * singlePopScriptExecutor
   */
  private JedisScriptExecutor singlePopScriptExecutor;

  /**
   * batchPopScriptExecutor
   */
  private JedisScriptExecutor batchPopScriptExecutor;

  /**
   * batchPopScriptPath
   */
  private String batchPopScriptPath = DEFAULT_BATCH_POP_LUA_FILE_PATH;

  /**
   * singlePopScriptPath
   */
  private String singlePopScriptPath = DEFAULT_SINGLE_POP_LUA_FILE_PATH;

  /**
   * readyJobKey
   */
  private String readyJobKey;

  /**
   * lockJobKey
   */
  private String lockJobKey;

  /**
   * keys
   */
  private List<String> keys;

  /**
   * AdvancedJobConsumer
   *
   * @param jobName
   *        jobName
   * @param jedisPool
   *        jedisPool
   */
  public AdvancedJobConsumer(String jobName, JedisPool jedisPool) {
    readyJobKey = Keys.getReadyJobKey(jobName);
    lockJobKey = Keys.getLockJobKey(jobName);
    keys = Lists.newArrayList(readyJobKey, lockJobKey);

    jedisTemplate = new JedisTemplate(jedisPool);
    singlePopScriptExecutor = new JedisScriptExecutor(jedisPool);
    batchPopScriptExecutor = new JedisScriptExecutor(jedisPool);
  }

  /**
   * 初始化脚本，在popup前必须被调用.
   */
  public void init() {
    singlePopScriptExecutor.loadFromFile(singlePopScriptPath);
    batchPopScriptExecutor.loadFromFile(batchPopScriptPath);
  }

  /**
   * 即时返回任务, 如有任务返回的同时将其放入lock job set，如无任务返回null.
   * 如发生redis连接异常, 线程会sleep 5秒后返回null，
   * 如果发生redis数据错误如lua脚本错误，抛出异常.
   *
   * @return job
   */
  public String popupJob() {
    String job = null;
    try {
      long currTime = System.currentTimeMillis();
      List<String> args = Lists.newArrayList(String.valueOf(currTime), String.valueOf(reliable));
      job = (String) singlePopScriptExecutor.execute(keys, args);
    } catch (JedisConnectionException e) {
      ThreadUtil.sleep(DEFAULT_CONNECTION_RETRY_MILLS);
    }

    return job;
  }

  /**
   * 即时批量跑回任务, 如有任务返回的同时将其放入lock job set，如无任务返回空的List.
   * 如发生redis连接异常, 线程会sleep 5秒后返回null，
   * 如果发生redis数据错误如lua脚本错误，抛出异常.
   *
   * @return job 列表
   */
  @SuppressWarnings("unchecked")
  public List<String> popupJobs() {
    List<String> jobs = null;
    try {
      long currTime = System.currentTimeMillis();
      List<String> args = Lists.newArrayList(String.valueOf(currTime), String.valueOf(batchSize),
          String.valueOf(reliable));
      jobs = (List<String>) batchPopScriptExecutor.execute(keys, args);
    } catch (JedisConnectionException e) {
      ThreadUtil.sleep(DEFAULT_CONNECTION_RETRY_MILLS);
    }

    return jobs;
  }

  /**
   * 在高可靠模式下，报告任务完成, 从lock table set中删除任务.
   *
   * @param job
   *        job
   */
  public void ackJob(String job) {
    jedisTemplate.zrem(lockJobKey, job);
  }

  /**
   * 设置不在默认路径的lua script path，按Spring Resource的URL格式.
   *
   * @param batchPopScriptPath
   *        batchPopScriptPath
   */
  public void setBatchPopScriptPath(String batchPopScriptPath) {
    this.batchPopScriptPath = batchPopScriptPath;
  }

  /**
   * 设置不在默认路径的lua script path，按Spring Resource的URL格式.
   *
   * @param singlePopScriptPath
   *        singlePopScriptPath
   */
  public void setSinglePopScriptPath(String singlePopScriptPath) {
    this.singlePopScriptPath = singlePopScriptPath;
  }

  /**
   * 设置是否高可靠模式。
   *
   * @param reliable
   *        reliable
   */
  public void setReliable(boolean reliable) {
    this.reliable = reliable;
  }

  /**
   * 设置批量取回任务数量.
   *
   * @param batchSize
   *        batchSize
   */
  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }
}
