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
public class Config {

  /**
   * 项目当前的运行环境（开发，测试，正式）
   */
  public static final String RUNSTATE = ConfigUtil.runState;

  /**
   * 存放项目properties文件的公共路径
   */
  public static final Configuration COMM_PATH = ConfigUtil.getConfig(MessageFormat.format("conf/constants.{0}.properties", RUNSTATE));

  /**
   * 通行证接口
   */
  public static final String TXZ_USERINFO = COMM_PATH.getString("txz_userinfo_url");

  /**
   * sfut cookie 验证地址
   */
  public static final String TXZ_SFUTCOOKIECHECK = COMM_PATH.getString("txz_sfutCookieCheck");

  /**
   * 关键字过滤
   */
  public static final String KEYWORDSSOCKETPORT = COMM_PATH.getString("keyWordsSocketport");

  /**
   * 关键字过滤
   */
  public static final String KEYWORDSSOCKETHOST = COMM_PATH.getString("keyWordsSocketHost");

  /**
   * IP白名单段
   */
  public static final String[] IPWHITE = COMM_PATH.getStringArray("ipwhite");

  /**
   * REDIS_ADVERT_KEY
   */
  public static final String REDIS_ADVERT_KEY = "advert-";

  /**
   * REDIS_ADVERT_KEY_LIKE
   */
  public static final String REDIS_ADVERT_KEY_LIKE = "advert-like-";

  /**
   * REDIS_ADVERT_KEY_HITS
   */
  public static final String REDIS_ADVERT_KEY_HITS = "advert-hits-";

  /**
   * REDIS_ADVERT_KEY_COMMENT
   */
  public static final String REDIS_ADVERT_KEY_COMMENT = "advert-comment-";

  /**
   * REDIS_IPPREFIX
   */
  public static final String REDIS_IPPREFIX = "IP-";

  /**
   * REDIS_COOKIEPREFIX
   */
  public static final String REDIS_COOKIEPREFIX = "cookie-";

}