package com.fang.shiro;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.session.Session;

/**
 * session持久化实现类接口
 *
 * @author wangzhiyuan
 */
public interface ShiroSessionRepository {

  /**
   * 保存session
   *
   * @param session
   *        session对象
   */
  void saveSession(Session session);

  /**
   * 删除session
   *
   * @param sessionId
   *        sessionid标识
   */
  void deleteSession(Serializable sessionId);

  /**
   * 获取session
   *
   * @param sessionId
   *        sessionid标识
   * @return Session session对象
   */
  Session getSession(Serializable sessionId);

  /**
   * 获取所有session
   *
   * @return session 集合
   */
  Collection<Session> getAllSessions();
}
