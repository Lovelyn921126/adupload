package com.fang.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisSessionDAO;

import com.fang.entity.SysUser;
import com.fang.shiro.MyShiroRealm;

/**
 * Shiro工具类
 *
 * @author wangzhiyuan
 */
public class ShiroUtil {

  /**
   * 域名
   */
  private static final String DOMAIN_FANG = ".fang.com";

  /**
   * getSession
   *
   * @return Session
   */
  public static Session getSession() {
    return SecurityUtils.getSubject().getSession();
  }

  /**
   * getSubject
   *
   * @return Subject
   */
  public static Subject getSubject() {
    return SecurityUtils.getSubject();
  }

  /**
   * 在当前shiro中取出用户
   *
   * @return SysUser
   */
  public static SysUser getUser() {
    return (SysUser) SecurityUtils.getSubject().getPrincipal();
  }

  /**
   * 在当前shiro中取出用户id
   *
   * @return getUserId
   */
  public static String getUserId() {
    return getUser().getUserId();
  }

  /**
   * 添加Attribute到当前shiro
   *
   * @param key
   *        key
   * @param value
   *        value
   */
  public static void setSessionAttribute(Object key, Object value) {
    getSession().setAttribute(key, value);
  }

  /**
   * 通过KEY在当前shiro中取出Attribute
   *
   * @param key
   *        key
   * @return Attribute
   */
  public static Object getSessionAttribute(Object key) {
    return getSession().getAttribute(key);
  }

  /**
   * 验证是否是登录状态
   *
   * @return boolean
   */
  public static boolean isLogin() {
    return SecurityUtils.getSubject().getPrincipal() != null;
  }

  /**
   * 登出
   */
  public static void logout() {
    SecurityUtils.getSubject().logout();
    getSessionDAO().delete(SecurityUtils.getSubject().getSession());
  }

  /**
   * 清除缓存
   *
   * @param key
   *        key
   */
  public static void clear(String key) {
    RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
    MyShiroRealm userRealm = (MyShiroRealm) securityManager.getRealms().iterator().next();
    userRealm.clearCachedAuthorizationAndAuthenticationInfo(key);
  }

  /**
   * 获取SessionDAO
   *
   * @return SessionDAO
   */
  public static RedisSessionDAO getSessionDAO() {
    SessionsSecurityManager securityManager = (SessionsSecurityManager) SecurityUtils.getSecurityManager();
    DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
    RedisSessionDAO sessionDAO = (RedisSessionDAO) sessionManager.getSessionDAO();
    return sessionDAO;
  }

  /**
   * 在当前shiro中取出验证码
   *
   * @param key
   *        key
   * @return Kaptcha
   */
  public static String getKaptcha(String key) {
    String kaptcha = getSessionAttribute(key).toString();
    getSession().removeAttribute(key);
    return kaptcha;
  }

  /**
   * 重装加载用户权限
   */
  /*
   * public static void reLoadAuth() {
   * Object shiroBean = SpringContextHolder.getBean("myShiro");
   * MyShiroRealm myShiroRealm = (MyShiroRealm) shiroBean;
   * if (myShiroRealm != null) {
   * // 调用onlogout方法清权限缓存
   * myShiroRealm.onLogout(SecurityUtils.getSubject().getPrincipals());
   * // 触发权限读取
   * myShiroRealm.isPermitted(SecurityUtils.getSubject().getPrincipals(), "强制shiro检查加载用户权限缓存,避免懒加载!"
   * + System.currentTimeMillis());
   * }
   * }
   */

  /**
   * 退出时清除单点Cookie
   *
   * @param res
   *        res
   */
  public static void clearCookies(HttpServletResponse res) {
    Cookie uuidCookie = new Cookie("isso_uuid", null);
    uuidCookie.setSecure(true);
    uuidCookie.setMaxAge(0);
    uuidCookie.setDomain(DOMAIN_FANG);
    uuidCookie.setPath("/");

    Cookie loginCookie = new Cookie("isso_login", null);
    loginCookie.setSecure(true);
    loginCookie.setMaxAge(0);
    loginCookie.setDomain(DOMAIN_FANG);
    loginCookie.setPath("/");

    Cookie passCookie = new Cookie("isso_passwd", null);
    passCookie.setSecure(true);
    passCookie.setMaxAge(0);
    passCookie.setDomain(DOMAIN_FANG);
    passCookie.setPath("/");

    Cookie ticketCookie = new Cookie("isso_ticket", null);
    ticketCookie.setSecure(true);
    ticketCookie.setMaxAge(0);
    ticketCookie.setDomain(DOMAIN_FANG);
    ticketCookie.setPath("/");

    Cookie oaCookie = new Cookie("oa_token", null);
    ticketCookie.setSecure(true);
    ticketCookie.setMaxAge(0);
    ticketCookie.setDomain(DOMAIN_FANG);
    ticketCookie.setPath("/");

    res.addCookie(uuidCookie);
    res.addCookie(loginCookie);
    res.addCookie(passCookie);
    res.addCookie(ticketCookie);
    res.addCookie(oaCookie);
  }

  /**
   * 是否是Ajax请求
   *
   * @param request
   *        request
   * @return true、false
   */
  public static boolean isAjax(HttpServletRequest request) {
    return request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest");
  }

}
