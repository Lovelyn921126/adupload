package com.fang.shiro;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import com.fang.entity.vo.OnlineUser;
import com.fang.service.SysOnlineUserService;

/**
 * session持久化 用于shiro实现session共享
 *
 * @author wangzhiyuan
 */
public class MySessionDaoForRedis extends AbstractSessionDAO implements SysOnlineUserService {

  /**
   * 日期格式字符串
   */
  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  /**
   * session共享实现类
   */
  @Autowired
  private ShiroSessionRepository shiroSessionRepository;

  @Override
  public List<OnlineUser> queryAll() {
    List<OnlineUser> onlineUserList = new ArrayList<>();
    Collection<Session> values = getActiveSessions();
    for (Session session : values) {
      Object nameObj = session.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY");
      if (nameObj != null) {
        onlineUserList.add(new OnlineUser(
            session.getHost(),
            nameObj.toString(),
            dateFormat.format(session.getStartTimestamp()),
            dateFormat.format(session.getLastAccessTime())
            ));
      }
    }
    return onlineUserList;
  }

  @Override
  public Collection<Session> getActiveSessions() {
    return shiroSessionRepository.getAllSessions();
  }

  @Override
  protected Serializable doCreate(Session session) {
    Serializable sessionId = generateSessionId(session);
    assignSessionId(session, sessionId);
    // 自定义存储
    shiroSessionRepository.saveSession(session);
    return sessionId;
  }

  @Override
  protected Session doReadSession(Serializable sessionId) {
    return shiroSessionRepository.getSession(sessionId);
  }

  @Override
  public void update(Session session) {
    shiroSessionRepository.saveSession(session);
  }

  @Override
  public void delete(Session session) {
    if (session == null) {
      throw new NullPointerException("session argument cannot be null.");
    }
    Serializable id = session.getId();
    if (id != null) {
      shiroSessionRepository.deleteSession(id);
    }
  }

}
