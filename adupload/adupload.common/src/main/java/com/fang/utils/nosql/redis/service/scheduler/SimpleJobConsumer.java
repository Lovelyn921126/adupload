package com.fang.utils.nosql.redis.service.scheduler;

import java.util.List;

import com.fang.utils.concurrent.ThreadUtil;
import com.fang.utils.nosql.redis.JedisTemplate;
import com.fang.utils.nosql.redis.JedisTemplate.JedisAction;
import com.fang.utils.nosql.redis.pool.JedisPool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 简单的基于brpop()API, 阻塞的取出任务。
 * brpop的阻塞，在线程中断时不会自动退出，所以还是设置有限timeout时间，另外在线程池退出时已比timeout时间长的时间调用awaitTermination()等待线程结束.
 *
 * @author wangzhiyuan
 */
public class SimpleJobConsumer {

  /**
   * DEFAULT_POPUP_TIMEOUT_SECONDS
   */
  public static final int DEFAULT_POPUP_TIMEOUT_SECONDS = 5;

  /**
   * DEFAULT_CONNECTION_RETRY_MILLS
   */
  public static final int DEFAULT_CONNECTION_RETRY_MILLS = 5000;

  /**
   * logger
   */
  // private static Logger logger = LoggerFactory.getLogger(SimpleJobConsumer.class);

  /**
   * jedisTemplate
   */
  private JedisTemplate jedisTemplate;

  /**
   * readyJobKey
   */
  private String readyJobKey;

  /**
   * popupTimeoutSecs
   */
  private int popupTimeoutSecs = DEFAULT_POPUP_TIMEOUT_SECONDS;

  /**
   * SimpleJobConsumer
   *
   * @param jobName
   *        jobName
   * @param jedisPool
   *        jedisPool
   */
  public SimpleJobConsumer(String jobName, JedisPool jedisPool) {
    jedisTemplate = new JedisTemplate(jedisPool);
    readyJobKey = Keys.getReadyJobKey(jobName);
  }

  /**
   * 阻塞直到返回任务，如果popupTimeoutSecs内(默认5秒)无任务到达，返回null.
   * 如发生redis连接异常, 线程会sleep 5秒后返回null，
   *
   * @return popupJob
   */
  public String popupJob() {

    List<String> nameValuePair = null;
    try {
      nameValuePair = jedisTemplate.execute(new JedisAction<List<String>>() {

        @Override
        public List<String> action(Jedis jedis) {
          return jedis.brpop(popupTimeoutSecs, readyJobKey);
        }
      });
    } catch (JedisConnectionException e) {
      ThreadUtil.sleep(DEFAULT_CONNECTION_RETRY_MILLS);
    }

    if ((nameValuePair != null) && !nameValuePair.isEmpty()) {
      return nameValuePair.get(1);
    } else {
      return null;
    }
  }

  /**
   * setPopupTimeoutSecs
   *
   * @param popupTimeoutSecs
   *        popupTimeoutSecs
   */
  public void setPopupTimeoutSecs(int popupTimeoutSecs) {
    this.popupTimeoutSecs = popupTimeoutSecs;
  }
}
