package com.fang.utils.nosql.redis.service.scheduler;

import com.fang.utils.nosql.redis.JedisTemplate;
import com.fang.utils.nosql.redis.pool.JedisPool;

/**
 * 支持对当前任务池情况的状态数据查询.
 *
 * @author wangzhiyuan
 */
public class JobStatistics {

  /**
   * jedisTemplate
   */
  private JedisTemplate jedisTemplate;

  /**
   * scheduledJobKey
   */
  private String scheduledJobKey;

  /**
   * readyJobKey
   */
  private String readyJobKey;

  /**
   * lockJobKey
   */
  private String lockJobKey;

  /**
   * dispatchCounterKey
   */
  private String dispatchCounterKey;

  /**
   * retryCounterKey
   */
  private String retryCounterKey;

  /**
   * JobStatistics
   *
   * @param jobName
   *        jobName
   * @param jedisPool
   *        jedisPool
   */
  public JobStatistics(String jobName, JedisPool jedisPool) {
    scheduledJobKey = Keys.getScheduledJobKey(jobName);
    readyJobKey = Keys.getReadyJobKey(jobName);
    lockJobKey = Keys.getLockJobKey(jobName);

    dispatchCounterKey = Keys.getDispatchCounterKey(jobName);
    retryCounterKey = Keys.getRetryCounterKey(jobName);

    jedisTemplate = new JedisTemplate(jedisPool);
  }

  /**
   * 获取已安排但未达到触发条件的Job数量.
   *
   * @return sheduledJobNumber
   */
  public long getScheduledJobNumber() {
    return jedisTemplate.zcard(scheduledJobKey);
  }

  /**
   * 获取已触发但未被客户端取走的Job数量.
   *
   * @return readyJobNumber
   */
  public long getReadyJobNumber() {
    return jedisTemplate.llen(readyJobKey);
  }

  /**
   * 获取高可靠模式下，已被取走执行但未报告完成的Job数量.
   *
   * @return lockJobNumber
   */
  public long getLockJobNumber() {
    return jedisTemplate.zcard(lockJobKey);
  }

  /**
   * 获取已触发的Job总数。
   *
   * @return dispatchCounter
   */
  public long getDispatchCounter() {
    return jedisTemplate.getAsLong(dispatchCounterKey);
  }

  /**
   * 获取高可靠模式下，已重做的Job总数。
   *
   * @return retryCounter
   */
  public long getRetryCounter() {
    return jedisTemplate.getAsLong(retryCounterKey);
  }

  /**
   * 重置所有计数器.
   */
  public void restCounters() {
    jedisTemplate.set(dispatchCounterKey, "0");
    jedisTemplate.set(retryCounterKey, "0");
  }
}
