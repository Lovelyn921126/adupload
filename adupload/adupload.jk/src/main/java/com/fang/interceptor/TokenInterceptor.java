/**
 * File：TokenInterceptor.java
 * Package：com.fang.coupon.core.web.servlet.interceptor
 * Author："wangzhiyuan"
 * Date：2016年9月26日 下午1:53:28
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * token校验，防止重复提交
 *
 * @author wangzhiyuan
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {

  /**
   * 注入TokenHelper
   */
  @Autowired
  //private TokenHelper tokenHelper;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {



    //--特殊cookie不能为空
    //--cookie防刷
    //--ip短时间不能大量访问
    //--refer校验
    //--用户id校验
    //过滤
    return super.preHandle(request, response, handler);
  }

}
