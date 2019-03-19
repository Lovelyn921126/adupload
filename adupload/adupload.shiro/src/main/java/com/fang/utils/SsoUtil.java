/**
 * File：SsoUtil.java
 * Package：com.fang.framework
 * Author："wangzhiyuan"
 * Date：2016年10月29日 上午11:07:53
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils;

import java.text.MessageFormat;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.fang.utils.lang.CharsetsUtil;
import com.fang.utils.security.DESUtil;
import com.fang.utils.web.HttpClientUtil;
import com.fang.utils.web.HttpContextUtil;

/**
 * 单点登录
 *
 * @author wangzhiyuan
 */
public class SsoUtil {

  /**
   * httpClientUtil
   */
  @Autowired
  private HttpClientUtil httpClientUtil;

  /**
   * 用户自动登陆
   *
   * @param ssoApiUrl
   *        OA提供验证用户接口
   * @param ssoServiceID
   *        服务ID
   * @param mail
   *        返回的mail
   * @return 是否登录成功
   * @throws Exception
   *         Exception
   */
  public boolean autoLogin(String ssoApiUrl, String ssoServiceID, RefUtil<String> mail) throws Exception {
    HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
    Cookie[] cookies = request.getCookies();
    if (cookies != null && cookies.length != 0) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("oa_token")) {
          String oa_token = CodecUtil.urlEncode(cookie.getValue(), CharsetsUtil.GBK);
          String url = ssoApiUrl + "&isso_sid=" + ssoServiceID + "&oa_token=" + oa_token + "&guid=" + IdUtil.newGuid();
          String result = httpClientUtil.get(url);
          JSONObject jsonObj = JsonXmlObjUtils.json2JsonObject(result);
          mail.set(jsonObj.get("msg").toString());
          if (jsonObj.get("code").equals("0")) {
            return true;
          }
          return false;
        }
      }
    }
    return false;
  }

  /**
   * 注销用户接口
   *
   * @param ssoApiUrl
   *        OA提供验证用户接口
   * @param ssoServiceID
   *        单点服务ID
   * @param email
   *        被激活用户邮箱
   * @return 是否注销成功
   * @throws Exception
   *         Exception
   */
  public boolean deActive(String ssoApiUrl, String ssoServiceID, String email) throws Exception {
    return deActive(ssoApiUrl, ssoServiceID, email, "isso_key");
  }

  /**
   * 注销用户接口
   *
   * @param ssoApiUrl
   *        OA提供验证用户接口
   * @param ssoServiceID
   *        单点服务ID
   * @param email
   *        被激活用户邮箱
   * @param signKey
   *        用来算sign的key
   * @return 是否注销成功
   * @throws Exception
   *         Exception
   */
  public boolean deActive(String ssoApiUrl, String ssoServiceID, String email, String signKey) throws Exception {
    HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
    String sign = CodecUtil.urlEncode(DESUtil.encrypt(MessageFormat.format("deactv_v2_{0}_{1}", email, ssoServiceID), signKey), CharsetsUtil.GBK);
    String url = ssoApiUrl + "?act=deactv_v2&oa_username=" + email + "&isso_sid=" + ssoServiceID + "&sign=" + sign + "&ip=" + IPUtil.getIpAddr(request) + "&info="
        + IdUtil.newGuid();

    String xml = httpClientUtil.get(url);

    JSONObject responseObj = JsonXmlObjUtils.xml2obj(xml, JSONObject.class);
    String xmlNode = responseObj.getJSONObject("isso").get("code").toString();
    // “0”-成功，“-4”-无此用户
    if (xmlNode.equals("0") || xmlNode.equals("-4")) {
      return true;
    }
    return false;
  }

  /**
   * 用户激活
   *
   * @param ssoApiUrl
   *        单点接口地址
   * @param ssoServiceID
   *        服务ID
   * @param email
   *        被激活用户邮箱
   * @return 是否激活成功
   * @throws Exception
   *         Exception
   */
  public boolean active(String ssoApiUrl, String ssoServiceID, String email) throws Exception {
    RefUtil<String> result = new RefUtil<String>("");
    return active(ssoApiUrl, ssoServiceID, email, "isso_key", result);
  }

  /**
   * 用户激活
   *
   * @param ssoApiUrl
   *        单点接口地址
   * @param ssoServiceID
   *        服务ID
   * @param email
   *        被激活用户邮箱
   * @param signKey
   *        用来算sign的key
   * @param re
   *        返回参数信息
   * @return 是否激活成功
   * @throws Exception
   *         Exception
   */
  public boolean active(String ssoApiUrl, String ssoServiceID, String email, String signKey, RefUtil<String> re) throws Exception {
    HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
    String sign = CodecUtil.urlEncode(DESUtil.encrypt(MessageFormat.format("actv_v2_{0}_{1}", email, ssoServiceID), signKey), CharsetsUtil.GBK);
    String url = ssoApiUrl + "?act=actv_v2&oa_username=" + email + "&isso_sid=" + ssoServiceID + "&sign=" + sign + "&ip=" + IPUtil.getIpAddr(request) + "&info="
        + IdUtil.newGuid();

    httpClientUtil.setConnectTimeout(3000);
    String xml = httpClientUtil.get(url);

    JSONObject responseObj = JsonXmlObjUtils.xml2obj(xml, JSONObject.class);
    String xmlNode = responseObj.getJSONObject("isso").get("code").toString();
    String xmlMsg = responseObj.getJSONObject("isso").get("msg").toString();
    // “0”-成功，“-8”-权限已开通
    if (xmlNode.equals("0") || xmlNode.equals("-8")) {
      return true;
    }
    re.set(xmlMsg);
    return false;
  }
}
