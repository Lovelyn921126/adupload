package com.fang.utils.nosql.redis.service.scheduler;

/**
 * 获取各种key
 *
 * @author wangzhiyuan
 */
public class Keys {

  /**
   * getScheduledJobKey
   *
   * @param jobName
   *        jobName
   * @return scheduledJobKey
   */
  public static String getScheduledJobKey(String jobName) {
    return new StringBuilder().append("job:").append(jobName).append(":scheduled").toString();
  }

  /**
   * getReadyJobKey
   *
   * @param jobName
   *        jobName
   * @return readyJobKey
   */
  public static String getReadyJobKey(String jobName) {
    return new StringBuilder().append("job:").append(jobName).append(":ready").toString();
  }

  /**
   * getLockJobKey
   *
   * @param jobName
   *        jobName
   * @return lockJobKey
   */
  public static String getLockJobKey(String jobName) {
    return new StringBuilder().append("job:").append(jobName).append(":lock").toString();
  }

  /**
   * getDispatchCounterKey
   *
   * @param jobName
   *        jobName
   * @return dispatchCounterKey
   */
  public static String getDispatchCounterKey(String jobName) {
    return new StringBuilder().append("job:").append(jobName).append(":dispatch.counter").toString();
  }

  /**
   * getRetryCounterKey
   *
   * @param jobName
   *        jobName
   * @return retryCounterKey
   */
  public static String getRetryCounterKey(String jobName) {
    return new StringBuilder().append("job:").append(jobName).append(":retry.counter").toString();
  }
}
