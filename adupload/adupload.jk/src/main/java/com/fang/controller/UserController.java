/**
 * File：UserController.java
 * Package：com.fang.controller
 * Author：wangzhiyuan
 * Date：2017年5月16日 下午1:09:49
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.controller;

import com.alibaba.fastjson.JSONObject;
import com.fang.annotation.IgnoreAuth;
import com.fang.annotation.ResponseJsonp;
import com.fang.common.Config;
import com.fang.common.MsgConfig;
import com.fang.utils.CodecUtil;
import com.fang.utils.IPUtil;
import com.fang.utils.JsonXmlObjUtils;
import com.fang.utils.exception.CommonException;
import com.fang.utils.lang.CharsetsUtil;
import com.fang.utils.web.HttpClientUtil;
import com.fang.utils.web.RJsonp;
import com.fang.validator.Assert;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户相关
 *
 * @author wangzhiyuan
 */
@RestController
@RequestMapping("/api")
public class UserController {

  /**
   * httpClientUtil
   */
  @Autowired
  private HttpClientUtil httpClientUtil;

  /**
   * 注入user
   */
  /*
   * @Autowired
   * private User user;
   */

  /**
   * 根据SFUT获取用户信息
   *
   * @param request
   *        request
   * @param response
   *        response
   * @return 结果
   */
  @IgnoreAuth
  @ResponseJsonp
  @RequestMapping("user/info")
  public RJsonp info(HttpServletRequest request, HttpServletResponse response) {
    boolean withcookie = false;
    String jsonpcallback = "jsonpcallback";
    try {
      jsonpcallback = request.getParameter("jsonpcallback"); // 客户端请求参数
      Assert.isBlank(jsonpcallback, MsgConfig.API_PARAM_ILLEGAL.concat("[jsonpCallback]"));

      String sfut = "sfut";
      Cookie[] cookies = request.getCookies();
      if (cookies != null && cookies.length != 0) {
        for (Cookie cookie : cookies) {
          if (cookie.getName().equalsIgnoreCase("sfut") && StringUtils.isNotBlank(cookie.getValue())) {
            sfut = CodecUtil.urlEncode(cookie.getValue(), CharsetsUtil.GBK);
            withcookie = true;
            break;
          }
        }
      }

      if (!withcookie) {
        throw new CommonException("请重新登录");
      }

      Map<String, Object> data = new HashMap<>();

      String url = MessageFormat.format(Config.TXZ_SFUTCOOKIECHECK, IPUtil.getIpAddr(request), sfut);
      String xml = httpClientUtil.get(url);
      JSONObject responseObj = JsonXmlObjUtils.xml2obj(xml, JSONObject.class).getJSONObject("soufun_passport").getJSONObject("common");
      String returnvalue = responseObj.get("return_result").toString();
      if (returnvalue.equals("100")) {
        data.put("userid", responseObj.get("userid").toString());
        data.put("username", responseObj.get("username").toString());
        // UserInfo u = user.getUserInfoFormTXZ(responseObj.get("userid").toString());
        // data.put("avatar", u.getAvatar() == null ? "" : u.getAvatar());
        data.put("avatar", "");
      } else {
        throw new CommonException(responseObj.get("error_reason").toString());
      }
      return RJsonp.success(jsonpcallback, data, response);
    } catch (Exception e) {
      return RJsonp.error(jsonpcallback, e, response);
    }
  }

}
