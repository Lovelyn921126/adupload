/**
 * File：Config.java
 * Package：com.fang.shiro.common
 * Author：wangzhiyuan
 * Date：2017年3月29日 下午4:15:49
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.common;

import org.apache.commons.configuration.Configuration;

import com.fang.utils.ConfigUtil;

/**
 * 通用变量
 *
 * @author wangzhiyuan
 */
public class MsgConfig {

  /**
   * 存放项目properties文件的公共路径
   */
  public static final Configuration COMM_PATH = ConfigUtil.getConfig("conf/message/message_api.properties");

  /**
   * 不允许访问
   */
  public static final String API_NOT_ALLOW_ACCESS = COMM_PATH.getString("api_not_allow_access");

  /**
   * 用户未注册搜房网通行证
   */
  public static final String API_ERR_USER = COMM_PATH.getString("api_err_user");

  /**
   * 参数异常
   */
  public static final String API_PARAM_ILLEGAL = COMM_PATH.getString("api_param_illegal");

  /**
   *交易请求参数不完整
   */
  public static final String API_PARAM_INCOMPLETE = COMM_PATH.getString("api_param_incomplete");

}