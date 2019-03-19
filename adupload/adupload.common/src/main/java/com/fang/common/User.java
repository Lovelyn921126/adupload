/**
 * File：User.java
 * Package：com.fang.coupon.business
 * Author："wangzhiyuan"
 * Date：2016年9月26日 下午3:57:49
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.common;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.fang.entity.UserInfo;
import com.fang.service.UserInfoService;
import com.fang.utils.CLogger;
import com.fang.utils.JsonXmlObjUtils;
import com.fang.utils.exception.ExceptionUtil;
import com.fang.utils.lang.CharsetsUtil;
import com.fang.utils.lang.StringUtil;
import com.fang.utils.web.HttpClientUtil;

/**
 * 用户相关操作
 *
 * @author wangzhiyuan
 */
public class User {

  /**
   * 注入用户相关服务层
   */
  @Autowired
  private UserInfoService userInfoService;

  /**
   * httpClientUtil
   */
  @Autowired
  private HttpClientUtil httpClientUtil;

  /**
   * 尝试添加用户
   *
   * @param userid
   *        用户ID
   * @return 相关用户信息
   */
  public UserInfo tryAdd(String userid) {
    UserInfo user = null;
    try {
      user = userInfoService.getUserByID(userid);

      if (user == null) {

        UserInfo userTXZ = getUserInfoFormTXZ(userid);
        if (StringUtil.isNotBlank(userTXZ.getUserId())) {
          user = userInfoService.addUser(userid, userTXZ.getName(), userTXZ.getMobile(), userTXZ.getEmail());
        }

      }

    } catch (Exception e) {
      CLogger.error("添加用户出现问题：userid=".concat(userid).concat(ExceptionUtil.getErrorMessageWithNestedException(e)));
      user = null;
    }
    return user;

  }

  /**
   * 从通行证接口获取用户信息
   *
   * @param userid
   *        用户ID
   * @return 相关用户信息
   * @throws Exception
   *         Exception
   */
  public UserInfo getUserInfoFormTXZ(String userid) throws Exception {
    UserInfo user = new UserInfo();
    String url = String.format(Config.TXZ_USERINFO, userid);
    String responseXml = httpClientUtil.get(url, CharsetsUtil.GB2312);
    JSONObject responseObj = JsonXmlObjUtils.xml2obj(responseXml, JSONObject.class);

    JSONObject userInfo = responseObj.getJSONObject("soufun_passport").getJSONObject("common");
    if (userInfo.get("return_result").toString().equals("100")) {
      user.setEmail(userInfo.get("email") != null ? userInfo.get("email").toString() : "");
      user.setMobile(userInfo.get("mobilephone") != null ? userInfo.get("mobilephone").toString() : "");
      user.setName(userInfo.get("username") != null ? userInfo.get("username").toString() : "");
      user.setAvatar(userInfo.get("avatar") != null ? userInfo.get("avatar").toString() : "");
      user.setUserId(userid);
      return user;
    } else {
      return null;
    }
  }

}
