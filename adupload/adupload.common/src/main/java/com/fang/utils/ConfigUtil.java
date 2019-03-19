/**
 * File：ConfigUtil.java
 * Package：com.fang.framework.utils
 * Author：wangzhiyuan
 * Date：2017年3月27日 上午9:42:46
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.utils;

import java.util.ResourceBundle;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.fang.utils.exception.CommonException;

/**
 * 配置文件读取类
 *
 * @author wangzhiyuan
 */
public class ConfigUtil {

  /**
   * 当前系统状态
   */
  public static String runState;

  /**
   * 静态代码块
   */
  static {
    ResourceBundle bundleRunState = ResourceBundle.getBundle("conf/runState");
    runState = bundleRunState.getString("profiles.active");
    if (runState.trim().equals("${profiles.active}")) {
      runState = "develope";
    }
  }

  /**
   * 获取配置信息
   *
   * @param filePath
   *        filePath
   * @return Configuration
   */
  public static Configuration getConfig(String filePath) {
    try {
      return new PropertiesConfiguration(filePath);
    } catch (ConfigurationException e) {
      throw new CommonException("获取配置文件失败，", e);
    }
  }

}
