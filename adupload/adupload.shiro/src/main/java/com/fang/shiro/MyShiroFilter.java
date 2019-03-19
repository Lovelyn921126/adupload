/**
 * File：MyShiroFilter.java
 * Package：io.wille.web.security
 * Author：wangzhiyuan
 * Date：2017年3月22日 下午4:41:15
 */
package com.fang.shiro;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * 自定义ShiroFilter
 *
 * @author wangzhiyuan
 */
public class MyShiroFilter extends DelegatingFilterProxy {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (!(request instanceof HttpServletRequest)) {
      super.doFilter(request, response, filterChain);
      return;
    }

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    String url = httpRequest.getRequestURL().toString();
    /**
     * 由于Resin等服务器原因，如果用户已经登录，这时服务器重启，再次刷新页面时由于之前的SessionID已经不存在于Server上，
     * 这样会重新生成一个新的SessionId，此时新的SessionId也不存在于Shiro中，因此进行了Redirect来规避此问题
     */
    if (url.contains("autologin.jsp;JSESSIONID")) {
      httpResponse.sendRedirect("autologin.jsp");
      return;
    }
    if (url.contains("autologin.html;JSESSIONID")) {
      httpResponse.sendRedirect("autologin.html");
      return;
    }
    if (url.contains("autologinpage.html;JSESSIONID")) {
      httpResponse.sendRedirect("autologinpage.html");
      return;
    }
    if (url.contains("login.html;JSESSIONID")) {
      httpResponse.sendRedirect("login.html");
      return;
    }
    if (url.contains("login2.html;JSESSIONID")) {
      httpResponse.sendRedirect("login2.html");
      return;
    }
    if (url.contains("index.html;JSESSIONID")) {
      httpResponse.sendRedirect("index.html");
      return;
    }
    super.doFilter(request, response, filterChain);
  }
}
