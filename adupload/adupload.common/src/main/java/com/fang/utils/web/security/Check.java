/**
 * File：CheckIP.java
 * Package：com.fang.utils.web.security
 * Author：wangzhiyuan
 * Date：2017年5月12日 下午2:36:30
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.utils.web.security;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fang.common.Config;
import com.fang.utils.CLogger;
import com.fang.utils.CodecUtil;
import com.fang.utils.IPUtil;
import com.fang.utils.exception.ExceptionUtil;
import com.fang.utils.lang.CharsetsUtil;
import com.fang.utils.nosql.redis.JedisTemplate;
import com.fang.utils.web.HttpClientUtil;

/**
 * 验证ip是否有效，并且判断短时间内访问量
 *
 * @author wangzhiyuan
 */
public class Check {

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

  /**
   * REFERER正则
   */
  private static final String REFERER = "^([a-zA-Z0-9]+\\.)+(fang.com|soufun.com)$";

  /**
   * 验证用户ip
   *
   * @param request
   *        request
   * @return 结果
   */
  public boolean checkUserIP(HttpServletRequest request) {

    String oldIp = IPUtil.getIpAddr(request);
    if (!IPUtil.isValidIpv4(oldIp)) {
      return false;
    }

    if (StringUtils.startsWithAny(oldIp, Config.IPWHITE)) {
      return true;
    }

    String ip = Config.REDIS_IPPREFIX.concat(oldIp);

    try {
      Long count = jedisTemplateR.getAsLong(ip);
      if (count == null) {
        jedisTemplateW.setnxex(ip, "1", 60 * 5);
        return true;
      }
      if (count > 200) {
        return false;
      }
      jedisTemplateW.incr(ip);
      return true;
    } catch (Exception e) {
      CLogger.error("ipcheck:{}", ExceptionUtil.getErrorMessageWithNestedException(e));
      return true;
    }
  }

  /**
   * 验证用户cookie
   *
   * @param request
   *        request
   * @return 结果
   */
  public boolean checkUserCookie(HttpServletRequest request) {
    boolean withcookie = false;
    String global_cookie = "global_cookie";
    Cookie[] cookies = request.getCookies();
    if (cookies != null && cookies.length != 0) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equalsIgnoreCase("global_cookie") && StringUtils.isNotBlank(cookie.getValue())) {
          global_cookie = CodecUtil.urlEncode(cookie.getValue(), CharsetsUtil.GBK);
          withcookie = true;
          break;
        }
      }
    }

    if (!withcookie) {
      return false;
    }

    String cookie = Config.REDIS_COOKIEPREFIX.concat(global_cookie);
    try {
      Long count = jedisTemplateR.getAsLong(cookie);
      if (count == null) {
        jedisTemplateW.setnxex(cookie, "1", 60 * 5);
        return true;
      }
      if (count > 200) {
        return false;
      }
      jedisTemplateW.incr(cookie);
      return true;
    } catch (Exception e) {
      CLogger.error("cookiecheck:{}", ExceptionUtil.getErrorMessageWithNestedException(e));
      return true;
    }
  }

  /**
   * 验证Referer是否合法
   *
   * @param request
   *        request
   * @return 结果
   * @throws MalformedURLException
   *         MalformedURLException
   */
  public static boolean checkUrlReferer(HttpServletRequest request) throws MalformedURLException {
    String address = request.getHeader("referer");
    String pathAdd = "";

    if (StringUtils.isBlank(address)) {
      return false;
    }

    URL urlOne = new URL(address); // 实例化URL方法
    pathAdd = urlOne.getHost(); // 获取请求页面的服务器主机

    if (!Pattern.compile(REFERER).matcher(pathAdd).matches()) {
      return false;
    }

    // String referer = request.getHeader("Referer");
    // if (StringUtils.isBlank(referer)) {
    // return false;
    // }
    //
    // if (Config.RUNSTATE.equals("publish")) {
    // if (!Pattern.compile(REFERER).matcher(request.getRemoteHost()).matches()) {
    // return false;
    // }
    // } else {
    // if (!StringUtils.containsAny(referer, "fang.com", "soufun.com")) {
    // return false;
    // }
    // }

    return true;
  }

  /**
   * 判断用户名是否受限
   *
   * @param str
   *        验证的字符串
   * @return 返回结果
   */
  public static boolean isWordsLimited(String str) {
    String ip = Config.KEYWORDSSOCKETHOST;
    int port = Integer.parseInt(Config.KEYWORDSSOCKETPORT);
    String socketSendData = "CheckPost~全国~passport~" + str + "\0";
    try {
      String receive = HttpClientUtil.socket(ip, port, socketSendData, true, "GB2312");
      if (receive.substring(0, 1).equals("1")) {
        return true;
      }
    } catch (Exception e) {
      // 记录日志
      return false;
    }
    return false;
  }

}
