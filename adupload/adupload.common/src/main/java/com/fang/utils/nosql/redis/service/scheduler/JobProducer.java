package com.fang.utils.nosql.redis.service.scheduler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fang.utils.nosql.redis.JedisTemplate;
import com.fang.utils.nosql.redis.pool.JedisPool;

/**
 * 任务生成的管理器，支持任务的安排与取消。
 * 任务分延时任务与立即执行任务两种, 未来或将支持固定间隔循环执行任务.
 *
 * @author wangzhiyuan
 */
public class JobProducer {

  /**
   * logger
   */
  private static Logger logger = LoggerFactory.getLogger(JobProducer.class);

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
   * JobProducer
   *
   * @param jobName
   *        jobName
   * @param jedisPool
   *        jedisPool
   */
  public JobProducer(String jobName, JedisPool jedisPool) {
    jedisTemplate = new JedisTemplate(jedisPool);
    scheduledJobKey = Keys.getScheduledJobKey(jobName);
    readyJobKey = Keys.getReadyJobKey(jobName);
  }

  /**
   * 提交立即执行的任务。
   *
   * @param job
   *        job
   */
  public void queue(final String job) {
    jedisTemplate.lpush(readyJobKey, job);
  }

  /**
   * 安排延时执行任务.
   *
   * @param job
   *        job
   * @param delay
   *        delay
   * @param timeUnit
   *        timeUnit
   */
  public void schedule(final String job, final long delay, final TimeUnit timeUnit) {
    final long delayTimeMillis = System.currentTimeMillis() + timeUnit.toMillis(delay);
    jedisTemplate.zadd(scheduledJobKey, delayTimeMillis, job);
  }

  /**
   * 尝试取消延时任务, 如果任务不存在或已被触发返回false, 否则返回true.
   *
   * @param job
   *        job
   * @return true/false
   */
  public boolean cancel(final String job) {
    boolean removed = jedisTemplate.zrem(scheduledJobKey, job);

    if (!removed) {
      logger.warn("Can't cancel scheduld job by value {}", job);
    }

    return removed;
  }
}
