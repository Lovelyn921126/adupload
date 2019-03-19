package com.fang.utils.web;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpContextUtil
 *
 * @author wangzhiyuan
 */
public class HttpContextUtil {

  /**
   * 在程序中任何位置获得HttpServletRequest
   *
   * @return HttpServletRequest
   */
  public static HttpServletRequest getHttpServletRequest() {
    return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
  }
}
