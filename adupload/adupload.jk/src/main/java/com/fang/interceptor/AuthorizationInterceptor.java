package com.fang.interceptor;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fang.annotation.IgnoreAuth;
import com.fang.annotation.ResponseJsonp;
import com.fang.common.MsgConfig;
import com.fang.common.User;
import com.fang.entity.UserInfo;
import com.fang.utils.exception.CommonException;
import com.fang.utils.web.R;
import com.fang.utils.web.RJsonp;
import com.fang.utils.web.security.Check;
import com.fang.validator.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证
 *
 * @author wangzhiyuan
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

  /**
   * userService
   */
  @Autowired
  private User user;

  /**
   * 验证ip
   */
  @Autowired
  private Check check;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    String jsonpcallback = "jsonpcallback";

    ResponseJsonp esponseJsonp;
    IgnoreAuth ignoreAuth;
    if (handler instanceof HandlerMethod) {
      esponseJsonp = ((HandlerMethod) handler).getMethodAnnotation(ResponseJsonp.class);
      ignoreAuth = ((HandlerMethod) handler).getMethodAnnotation(IgnoreAuth.class);
    } else {
      return true;
    }

    try {

      // 重复提交校验

      if (esponseJsonp != null) {

        jsonpcallback = request.getParameter("jsonpcallback"); // 客户端请求参数
        Assert.commonCheck(true, "jsonpCallback", jsonpcallback, 0, false);

        // 验证referer
        if (!Check.checkUrlReferer(request)) {
          throw new CommonException(MsgConfig.API_NOT_ALLOW_ACCESS.concat("[r]"));
        }

        // IP校验
        if (!check.checkUserIP(request)) {
          throw new CommonException(MsgConfig.API_NOT_ALLOW_ACCESS.concat("[i]"));
        }

        // cookie校验
        if (!check.checkUserCookie(request)) {
          throw new CommonException(MsgConfig.API_NOT_ALLOW_ACCESS.concat("[c]"));
        }
      }

      // 验证用户
      if (ignoreAuth == null) {
        String userid = request.getParameter("userid");
        if (StringUtils.isBlank(userid)) {
          throw new CommonException(MsgConfig.API_PARAM_ILLEGAL.concat("[userid]"));
        }

        UserInfo userinfo = user.getUserInfoFormTXZ(userid);
        if (userinfo != null) {
          request.setAttribute("userid", userinfo.getUserId());
        } else {
          throw new CommonException(MsgConfig.API_ERR_USER);
        }
      }

    } catch (Exception e) {
      if (esponseJsonp != null) {
        RJsonp.error(jsonpcallback, e, response);
      } else {
        R.error(e.getMessage());
      }

      return false;
    }

    return true;
  }
}
