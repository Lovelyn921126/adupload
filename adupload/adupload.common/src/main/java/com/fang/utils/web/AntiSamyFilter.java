/**
 * File：AntiSamyFilter.java
 * Package：com.fang.framework.web.filter
 * Author：wangzhiyuan
 * Date：2016年6月28日 下午6:10:00
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;

import com.fang.utils.CLogger;
import com.fang.utils.CodecUtil;
import com.fang.utils.exception.ExceptionUtil;
import com.fang.utils.lang.SqlUtil;

/**
 * Servlet filter that checks all request parameters for potential XSS attacks.
 *
 * @author wangzhiyuan
 */
public class AntiSamyFilter implements Filter {

  /**
   * Policy Policy
   */
  private static Policy policy = null;

  /**
   * AntiSamy is unfortunately not immutable, but is threadsafe if we only
   * call {@link AntiSamy#scan(String taintedHTML, int scanType)}
   */
  private static AntiSamy antiSamy = new AntiSamy();

  static {
    try {
      String path = AntiSamyFilter.class.getClassLoader().getResource("antisamy/antisamy-anythinggoes-1.4.4.xml").getFile();

      if (path.startsWith("file")) {
        path = path.substring(6);
      }

      policy = Policy.getInstance(path);
      antiSamy = new AntiSamy(policy);
    } catch (PolicyException e) {
      CLogger.error("AntiSamy policy error:{}", ExceptionUtil.getErrorMessageWithNestedException(e));
    }

  }

  /**
   * doFilter
   *
   * @param request
   *        request
   * @param response
   *        response
   * @param chain
   *        chain
   * @exception IOException
   *            IOException
   * @exception ServletException
   *            ServletException
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {
      CleanServletRequest cleanRequest = new CleanServletRequest((HttpServletRequest) request,
          antiSamy);
      chain.doFilter(cleanRequest, response);
    } else {
      chain.doFilter(request, response);
    }
  }

  /**
   * init
   *
   * @param filterConfig
   *        filterConfig
   * @exception ServletException
   *            ServletException
   */
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  /**
   * destroy
   */
  public void destroy() {
  }

  /**
   * Wrapper for a {@link HttpServletRequest} that returns 'safe' parameter
   * values by passing the raw request parameters through the anti-samy
   * filter. Should be private
   */
  public static class CleanServletRequest extends HttpServletRequestWrapper {

    /**
     * antiSamy
     */
    private final AntiSamy antiSamy;

    /**
     * CleanServletRequest
     *
     * @param request
     *        request
     * @param antiSamy
     *        antiSamy
     */
    private CleanServletRequest(HttpServletRequest request, AntiSamy antiSamy) {
      super(request);
      this.antiSamy = antiSamy;
    }

    /**
     * overriding getParameter functions in {@link ServletRequestWrapper}
     */
    @Override
    public String[] getParameterValues(String name) {
      String[] originalValues = super.getParameterValues(name);
      if (originalValues == null) {
        return null;
      }
      List<String> newValues = new ArrayList<String>(originalValues.length);
      for (String value : originalValues) {
        newValues.add(filterString(value));
      }
      return newValues.toArray(new String[newValues.size()]);
    }

    /**
     * getParameterMap
     */
    @Override
    public Map<String, String[]> getParameterMap() {
      Map<String, String[]> originalMap = super.getParameterMap();
      Map<String, String[]> filteredMap = new ConcurrentHashMap<String, String[]>(
          originalMap.size());
      for (String name : originalMap.keySet()) {
        filteredMap.put(name, getParameterValues(name));
      }
      return Collections.unmodifiableMap(filteredMap);
    }

    /**
     * getParameter
     */
    @Override
    public String getParameter(String name) {
      String potentiallyDirtyParameter = super.getParameter(name);
      return filterString(potentiallyDirtyParameter);
    }

    /**
     * This is only here so we can see what the original parameters were,
     * you should delete this method!
     *
     * @return original unwrapped request
     */
    @Deprecated
    public HttpServletRequest getOriginalRequest() {
      return (HttpServletRequest) super.getRequest();
    }

    /**
     * filterString
     *
     * @param potentiallyDirtyParameter
     *        string to be cleaned
     * @return a clean version of the same string
     */
    private String filterString(String potentiallyDirtyParameter) {
      if (potentiallyDirtyParameter == null) {
        return null;
      }

      String temp = potentiallyDirtyParameter;
      // 1.url解码
      temp = CodecUtil.urlDecode(temp);

      try {
        CleanResults cr = antiSamy.scan(potentiallyDirtyParameter, AntiSamy.DOM);
        if (cr.getNumberOfErrors() > 0) {
          CLogger.warn("antisamy filter input: " + cr.getErrorMessages());
        }

        // 2.xss过滤
        temp = cr.getCleanHTML();

        // 3.sql注入过滤
        temp = SqlUtil.getSafetySql(temp);

        return temp;
      } catch (Exception e) {
        throw new IllegalStateException(e.getMessage(), e);
      }
    }
  }
}
