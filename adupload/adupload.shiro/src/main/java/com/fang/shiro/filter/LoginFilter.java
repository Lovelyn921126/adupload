package com.fang.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;

import com.fang.entity.SysUser;
import com.fang.utils.JsonXmlObjUtils;
import com.fang.utils.ShiroUtil;
import com.fang.utils.web.R;
import com.fang.utils.web.WebUtil;

/**
 * 判断登录
 *
 * @author wangzhiyuan
 */
public class LoginFilter extends AccessControlFilter {

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

    SysUser token = ShiroUtil.getUser();

    if (null != token || isLoginRequest(request, response)) { // && isEnabled()
      return Boolean.TRUE;
    }
    if (ShiroUtil.isAjax((HttpServletRequest) request)) { // ajax请求
      R r = R.error("您的登录已经超时，请刷新页面！");
      WebUtil.sendJsonResponse(JsonXmlObjUtils.obj2json(r), (HttpServletResponse) response);
    }
    return Boolean.FALSE;

  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    // 保存Request和Response 到登录后的链接
    saveRequestAndRedirectToLogin(request, response);
    return Boolean.FALSE;
  }

}
