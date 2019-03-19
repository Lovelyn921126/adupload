/**
 * File：MySessionDao.java
 * Package：com.fang.adupload.web.security
 * Author：wangzhiyuan
 * Date：2017年3月27日 上午9:15:54
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.shiro;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.util.CollectionUtils;

import com.fang.common.ShiroConfig;
import com.fang.entity.vo.OnlineUser;
import com.fang.service.SysOnlineUserService;

/**
 * session处理相关Dao，继承{@link org.apache.shiro.session.mgt.eis.AbstractSessionDAO}
 *
 * @author wangzhiyuan
 */
public class MySessionDao extends AbstractSessionDAO implements SysOnlineUserService {

  /**
   * 日期格式字符串
   */
  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  /**
   * 在线session
   */
  private ConcurrentMap<Serializable, Session> sessions;

  /**
   * con
   */
  public MySessionDao() {
    this.sessions = new ConcurrentHashMap<>();
  }

  @Override
  public void update(Session session) throws UnknownSessionException {
    session.setTimeout(Integer.parseInt(ShiroConfig.AUTH_TIMEOUT));
    storeSession(session.getId(), session);
  }

  @Override
  public void delete(Session session) {
    if (session == null) {
      throw new NullPointerException("session argument cannot be null.");
    }
    Serializable id = session.getId();
    if (id != null) {
      sessions.remove(id);
    }
  }

  @Override
  public Collection<Session> getActiveSessions() {
    Collection<Session> values = sessions.values();
    if (CollectionUtils.isEmpty(values)) {
      return Collections.emptySet();
    } else {
      return Collections.unmodifiableCollection(values);
    }
  }

  @Override
  public List<OnlineUser> queryAll() {
    List<OnlineUser> onlineUserList = new ArrayList<>();
    Collection<Session> values = sessions.values();
    for (Session session : values) {
      Object nameObj = session.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY");
      if (nameObj != null) {
        onlineUserList.add(new OnlineUser(session.getHost(), nameObj.toString(), dateFormat.format(session.getStartTimestamp()), dateFormat.format(session.getLastAccessTime())));
      }
    }
    return onlineUserList;
  }

  @Override
  protected Serializable doCreate(Session session) {
    Serializable sessionId = generateSessionId(session);
    assignSessionId(session, sessionId);
    storeSession(sessionId, session);
    return sessionId;
  }

  @Override
  protected Session doReadSession(Serializable sessionId) {
    return sessions.get(sessionId);
  }

  /**
   * store session
   *
   * @param id
   *        id
   * @param session
   *        session
   * @return Session
   */
  protected Session storeSession(Serializable id, Session session) {
    if (id == null) {
      throw new NullPointerException("id argument cannot be null.");
    }
    return sessions.putIfAbsent(id, session);
  }
}
