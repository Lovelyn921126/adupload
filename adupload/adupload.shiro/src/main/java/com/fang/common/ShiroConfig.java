/**
 * File：Config.java
 * Package：com.fang.shiro.common
 * Author：wangzhiyuan
 * Date：2017年3月29日 下午4:15:49
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.common;

import java.text.MessageFormat;

import org.apache.commons.configuration.Configuration;

import com.fang.utils.ConfigUtil;

/**
 * 通用变量
 *
 * @author wangzhiyuan
 */
public class ShiroConfig {

  /**
   * 项目当前的运行环境（开发，测试，正式）
   */
  public static final String RUNSTATE = ConfigUtil.runState;

  /**
   * 存放项目properties文件的公共路径
   */
  public static final Configuration COMM_PATH = ConfigUtil.getConfig(MessageFormat.format("conf/constants.{0}.properties", RUNSTATE));

  /**
   * 后台登录凭证过期时间（毫秒）
   */
  public static final String AUTH_TIMEOUT = COMM_PATH.getString("Auth_timeout");

  /**
   * oa验证登陆接口
   */
  public static final String SSOOAVERIFY = COMM_PATH.getString("ssoOaVerify");

  /**
   * 单点登录服务标识,每个业务需要单独申请
   */
  public static final String SSOSERVICEID = COMM_PATH.getString("ssoServiceID");

  /**
   * 管理后台-单点登录服务地址
   */
  public static final String SSOAPIURL = COMM_PATH.getString("ssoApiUrl");

  /**
   * 管理后台-OA sign计算key
   */
  public static final String OASIGNKEY = COMM_PATH.getString("oaSignKey");

  /**
   * OA登录地址
   */
  public static final String OA_LOGINURL = COMM_PATH.getString("siteAdmin_loginUrl");

  /**
   * 根目录ID
   */
  public static final String ROOT_MENU_ID = "0";

  /**
   * 权限内全编辑角色
   */
  public static final String HOST_ID = COMM_PATH.getString("host_id");
}