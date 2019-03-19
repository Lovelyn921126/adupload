package com.fang.shiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import com.fang.entity.SysLog;
import com.fang.entity.SysMenu;
import com.fang.entity.SysUser;
import com.fang.entity.vo.SysMenuVo;
import com.fang.service.SysLogService;
import com.fang.service.SysMenuService;
import com.fang.service.SysUserService;
import com.fang.utils.IPUtil;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.web.HttpContextUtil;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 认证
 *
 * @author wangzhiyuan
 */
public class MyShiroRealm extends AuthorizingRealm {

  /**
   * sysLogService
   */
  @Autowired
  private SysLogService sysLogService;

  /**
   * 注入sysUserService
   */
  @Autowired
  private SysUserService sysUserService;

  /**
   * 注入sysMenuService
   */
  @Autowired
  private SysMenuService sysMenuService;

  /**
   * sessionDAO
   */
  @Autowired
  private RedisSessionDAO sessionDAO;

  /**
   * 授权(验证权限时调用)
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    SysUser user = (SysUser) principals.getPrimaryPrincipal();
    String userId = user.getUserId();

    List<String> permsList = null;

    // 系统管理员，拥有最高权限
    if (user.getType() == 3) {
      List<SysMenuVo> menuList = sysMenuService.queryList(new HashMap<String, Object>(), new PageBounds());
      permsList = new ArrayList<>(menuList.size());
      for (SysMenu menu : menuList) {
        permsList.add(menu.getPerms());
      }
    } else {
      permsList = sysUserService.queryAllPerms(userId);
    }

    // 用户权限列表
    Set<String> permsSet = new HashSet<String>();
    for (String perms : permsList) {
      if (StringUtils.isBlank(perms)) {
        continue;
      }
      permsSet.addAll(Arrays.asList(perms.trim().split(",")));
    }

    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    info.setStringPermissions(permsSet);
    return info;
  }

  /**
   * 认证(登录时调用)
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    String username = (String) token.getPrincipal();
    String password = new String((char[]) token.getCredentials());

    // 查询用户信息
    SysUser user = sysUserService.queryByUserName(username);

    // 账号不存在
    if (user == null) {
      throw new UnknownAccountException("账号或密码不正确");
    }

    if (user.getType().equals(0) || user.getType().equals(3)) {
      // 如果使用单点，不需要Shiro来验证密码
      user.setPassword(password);
    } else {
      // shiro判断时只抛出权限认证失败异常
      // 并不会抛出密码错误异常，因此手动抛出为此异常
      if (!password.equals(user.getPassword())) {
        throw new IncorrectCredentialsException("账号或密码不正确");
      }
    }

    // 账号锁定
    if (user.getIsDelete() == 1) {
      throw new LockedAccountException("账号已被锁定,请联系管理员");
    }

    SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
    // 记录登陆日志
    saveSysUserLoginLog(username);

    return info;
  }

  /**
   * 缓存KEY
   */
  @Override
  protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
    SysUser user = (SysUser) principals.getPrimaryPrincipal();
    if (user == null) {
      return principals;
    }
    return user.getUsername();
  }

  /**
   * 清除指定用户的授权信息
   *
   * @param key
   *        key
   */
  public void clearCachedAuthorizationAndAuthenticationInfo(String key) {
    if (StringUtils.isNotBlank(key)) {

      Cache<Object, AuthorizationInfo> authenticationCache = getAuthorizationCache();
      if (authenticationCache != null) {
        AuthorizationInfo authorizationInfo = authenticationCache.get(key);
        if (authorizationInfo != null) {
          authenticationCache.remove(key);
        }
      }
      updateSession(key);
    }
  }

  /**
   * 更新指定用户的session
   *
   * @param key
   *        key
   */
  private void updateSession(String key) {
    Session session = getSession(key);
    if (session != null) {
      SysUser user = null;
      try {
        user = sysUserService.queryByUserName(key);
      } catch (Exception e) {

      }
      SimplePrincipalCollection simplePrincipalCollection = new SimplePrincipalCollection(user, this.getName());
      session.setAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY, simplePrincipalCollection);
      sessionDAO.update(session);
    }
  }

  /**
   *
   * getSession
   *
   * @param email
   *        邮箱
   * @return 返回值
   */
  private Session getSession(String email) {
    Collection<Session> activeSessions = sessionDAO.getActiveSessions();
    if (activeSessions != null && activeSessions.size() > 0) {
      for (Session session : activeSessions) {
        SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (principalCollection != null) {
          SysUser user = (SysUser) principalCollection.getPrimaryPrincipal();
          if (user != null && email.equals(user.getUsername())) {
            return session;
          }
        }
      }
    }
    return null;
  }

  /**
   * 记录登陆日志
   *
   * @param username
   *        邮箱
   */
  private void saveSysUserLoginLog(String username) {
    SysLog sysLog = new SysLog();
    // 操作描述
    sysLog.setOperation("登录");
    // 获取request
    HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
    // 设置IP地址
    sysLog.setIp(IPUtil.getIpAddr(request));

    // 用户名
    sysLog.setUsername(username);

    sysLog.setCreateDate(DateTime.now().toDate());
    // 保存系统日志
    sysLogService.save(sysLog);
  }
}
