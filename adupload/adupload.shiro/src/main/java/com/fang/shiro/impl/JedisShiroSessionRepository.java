package com.fang.shiro.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;

import com.fang.common.ShiroConfig;
import com.fang.shiro.ShiroSessionRepository;
import com.fang.utils.CLogger;
import com.fang.utils.nosql.redis.JedisTemplate;
import com.fang.utils.serializer.SerializeUtil;

/**
 * Jedis共享session实现类
 *
 * @author wangzhiyuan
 */
public class JedisShiroSessionRepository implements ShiroSessionRepository {

  /**
   * redis session key前缀
   */
  private static final String REDIS_SHIRO_SESSION = "shiro-session:";

  /**
   * 毫秒与秒转化系数
   */
  private static final int PARAM = 1000;

  /**
   * 注入redis服务
   */
  @Autowired
  private JedisTemplate jedisTemplateW;

  /**
   * 注入redis服务
   */
  @Autowired
  private JedisTemplate jedisTemplateR;

  @Override
  public void saveSession(final Session session) {
    if (session == null || session.getId() == null) {
      CLogger.error("session或者session id为空");
      return;
    }
    try {
      session.setTimeout(Integer.parseInt(ShiroConfig.AUTH_TIMEOUT));
      jedisTemplateW.setex(getRedisSessionKey(session.getId()), SerializeUtil.serialize(session, "UTF-8"), (int) (session.getTimeout() / PARAM));
    } catch (org.springframework.dao.DataAccessResourceFailureException e) {
      CLogger.error("持久化session失败，请检查redis数据库是否正常连接:" + e);
    }
  }

  @Override
  public void deleteSession(final Serializable sessionId) {
    if (sessionId == null) {
      CLogger.error("sessionid为空");
      return;
    }
    try {
      jedisTemplateW.del(getRedisSessionKey(sessionId));
    } catch (org.springframework.dao.DataAccessResourceFailureException e) {
      CLogger.error("删除session失败，请检查redis数据库是否正常连接:" + e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Session getSession(final Serializable sessionId) {
    if (sessionId == null) {
      CLogger.error("id为空");
      return null;
    }
    Session session = null;
    try {
      String value = jedisTemplateR.get(getRedisSessionKey(sessionId));
      if (value == null) {
        return null;
      }
      session = SerializeUtil.deserialize(value, "UTF-8", Session.class);
    } catch (org.springframework.dao.DataAccessResourceFailureException e) {
      CLogger.error("获取session失败，请检查redis数据库是否正常连接:" + e);
    }
    return session;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Session> getAllSessions() {
    Set<Session> sessions = null;
    Set<String> arr = null;
    try {
      arr = jedisTemplateR.keys(REDIS_SHIRO_SESSION + "*");
      if (arr == null) {
        return null;
      }
      sessions = new HashSet<>();
      for (String str : arr) {
        sessions.add(SerializeUtil.deserialize(str, "UTF-8", Session.class));
      }
    } catch (org.springframework.dao.DataAccessResourceFailureException e) {
      CLogger.error("获取当前活动session失败，请检查redis数据库是否正常连接:" + e);
    }

    return sessions;
  }

  /**
   * 拼接redis中的session key 用于设置前缀方便模糊查询
   *
   * @param sessionId
   *        sessionId
   * @return redis中的session key
   */
  private String getRedisSessionKey(Serializable sessionId) {
    return REDIS_SHIRO_SESSION + sessionId;
  }

}
