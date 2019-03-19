package com.fang.utils.nosql.redis.service.scheduler;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fang.utils.concurrent.ThreadUtil;
import com.fang.utils.concurrent.ThreadUtil.WrapExceptionRunnable;
import com.fang.utils.nosql.redis.JedisScriptExecutor;
import com.fang.utils.nosql.redis.pool.JedisPool;
import com.google.common.collect.Lists;

/**
 * 定时分发任务的管理器。
 * 定时从scheduled job sorted set中取出到期的任务放入ready job list，并在高可靠模式下，将lock job 中 已超时的任务重新放入 ready job.
 * 线程池可自行创建，也可以从外部传入共用。
 *
 * @author wangzhiyuan
 */
public class JobDispatcher implements Runnable {

  /**
   * DEFAULT_DISPATCH_LUA_FILE
   */
  public static final String DEFAULT_DISPATCH_LUA_FILE = "classpath:com/fang/**/redis/dispatch.lua";

  /**
   * DEFAULT_INTERVAL_MILLIS
   */
  public static final long DEFAULT_INTERVAL_MILLIS = 1000;

  /**
   * DEFAULT_RELIABLE
   */
  public static final boolean DEFAULT_RELIABLE = false;

  /**
   * DEFAULT_JOB_TIMEOUT_SECONDS
   */
  public static final long DEFAULT_JOB_TIMEOUT_SECONDS = 60;

  /**
   * logger
   */
  private static Logger logger = LoggerFactory.getLogger(JobDispatcher.class);

  /**
   * internalScheduledThreadPool
   */
  private ScheduledExecutorService internalScheduledThreadPool;

  /**
   * dispatchJob
   */
  private ScheduledFuture<?> dispatchJob;

  /**
   * intervalMillis
   */
  private long intervalMillis = DEFAULT_INTERVAL_MILLIS;

  /**
   * reliable
   */
  private boolean reliable = DEFAULT_RELIABLE;

  /**
   * jobTimeoutSecs
   */
  private long jobTimeoutSecs = DEFAULT_JOB_TIMEOUT_SECONDS;

  /**
   * JedisScriptExecutor
   */
  private JedisScriptExecutor scriptExecutor;

  /**
   * scriptPath
   */
  private String scriptPath = DEFAULT_DISPATCH_LUA_FILE;

  /**
   * jobName
   */
  private String jobName;

  /**
   * keys
   */
  private List<String> keys;

  /**
   * JobDispatcher
   *
   * @param jobName
   *        jobName
   * @param jedisPool
   *        jedisPool
   */
  public JobDispatcher(String jobName, JedisPool jedisPool) {
    this.jobName = jobName;

    String scheduledJobKey = Keys.getScheduledJobKey(jobName);
    String readyJobKey = Keys.getReadyJobKey(jobName);
    String dispatchCounterKey = Keys.getDispatchCounterKey(jobName);
    String lockJobKey = Keys.getLockJobKey(jobName);
    String retryCounterKey = Keys.getRetryCounterKey(jobName);

    keys = Lists.newArrayList(scheduledJobKey, readyJobKey, dispatchCounterKey, lockJobKey, retryCounterKey);

    scriptExecutor = new JedisScriptExecutor(jedisPool);
  }

  /**
   * 启动分发线程, 自行创建scheduler线程池.
   */
  public void start() {
    internalScheduledThreadPool = Executors.newScheduledThreadPool(1,
        ThreadUtil.buildJobFactory("Job-Dispatcher-" + jobName + "-%d"));

    start(internalScheduledThreadPool);
  }

  /**
   * 启动分发线程, 使用传入的scheduler线程池.
   *
   * @param scheduledThreadPool
   *        scheduledThreadPool
   */
  public void start(ScheduledExecutorService scheduledThreadPool) {
    scriptExecutor.loadFromFile(scriptPath);

    dispatchJob = scheduledThreadPool.scheduleAtFixedRate(new WrapExceptionRunnable(this), 0, intervalMillis,
        TimeUnit.MILLISECONDS);
  }

  /**
   * 停止分发任务，如果是自行创建的threadPool则自行销毁，关闭时最多等待5秒。
   */
  public void stop() {
    dispatchJob.cancel(false);

    if (internalScheduledThreadPool != null) {
      ThreadUtil.normalShutdown(internalScheduledThreadPool, 5, TimeUnit.SECONDS);
    }
  }

  /**
   * 以当前时间为参数执行Lua Script分发任务。
   */
  @Override
  public void run() {
    try {
      long currTime = System.currentTimeMillis();
      List<String> args = Lists.newArrayList(String.valueOf(currTime), String.valueOf(reliable),
          String.valueOf(jobTimeoutSecs));
      scriptExecutor.execute(keys, args);
    } catch (Throwable e) {
      // catch any exception, because the scheduled thread will break if the exception thrown
      // outside.
      logger.error("Unexpected error occurred in task", e);
    }
  }

  /**
   * 设置非默认的script path, 格式为spring的Resource路径风格。
   *
   * @param scriptPath
   *        scriptPath
   */
  public void setScriptPath(String scriptPath) {
    this.scriptPath = scriptPath;
  }

  /**
   * 设置非默认1秒的分发间隔.
   *
   * @param intervalMillis
   *        intervalMillis
   */
  public void setIntervalMillis(long intervalMillis) {
    this.intervalMillis = intervalMillis;
  }

  /**
   * 设置是否支持高可靠性.
   *
   * @param reliable
   *        reliable
   */
  public void setReliable(boolean reliable) {
    this.reliable = reliable;
  }

  /**
   * 设置高可靠性模式下，非默认1分钟的任务执行超时时间.
   *
   * @param jobTimeoutSecs
   *        jobTimeoutSecs
   */
  public void setJobTimeoutSecs(long jobTimeoutSecs) {
    this.jobTimeoutSecs = jobTimeoutSecs;
  }
}
