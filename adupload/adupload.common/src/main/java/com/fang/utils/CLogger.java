/**
 * File：Logger.java
 * Package：com.fang.framework.io
 * Author：wangzhiyuan
 * Date：2016年6月29日 下午5:17:35
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.fang.utils.exception.ExceptionUtil;

/**
 * logback封装类
 *
 * @author wangzhiyuan
 */
public class CLogger {

  /**
   * logback的logger
   */
  private static Logger logger = null;

  /**
   * 私有化构造器
   */
  private CLogger() {
  }

  /**
   * 获取Logger
   *
   * @param name
   *        类型名称
   * @return Logger logback的logger
   */
  private static Logger getLogger(String name) {
    return (Logger) LoggerFactory.getLogger(name);
  }

  /**
   * 输出trace级别信息
   *
   * @param msg
   *        日志信息
   */
  public static void trace(String msg) {
    synchronized (CLogger.class) {
      CLogger.logger = getLogger(getCurrentClass());
      logger.trace(msg);
    }
  }

  /**
   * 输出debug级别信息
   *
   * @param msg
   *        日志信息
   */
  public static void debug(String msg) {
    synchronized (CLogger.class) {
      CLogger.logger = getLogger(getCurrentClass());
      logger.debug(msg);
    }
  }

  /**
   * 输出info级别信息
   *
   * @param msg
   *        日志信息
   */
  public static void info(String msg) {
    synchronized (CLogger.class) {
      CLogger.logger = getLogger(getCurrentClass());
      logger.info(msg);
    }
  }

  /**
   * 输出warn级别信息
   *
   * @param msg
   *        日志信息
   */
  public static void warn(String msg) {
    synchronized (CLogger.class) {
      CLogger.logger = getLogger(getCurrentClass());
      logger.warn(msg);
    }
  }

  /**
   * 输出error级别信息
   *
   * @param msg
   *        日志信息
   */
  public static void error(String msg) {
    synchronized (CLogger.class) {
      CLogger.logger = getLogger(getCurrentClass());
      logger.error(msg);
    }
  }

  /**
   * 输出error级别信息
   *
   * @param e
   *        异常
   */
  public static void error(Exception e) {
    synchronized (CLogger.class) {
      CLogger.logger = getLogger(getCurrentClass());
      logger.error(ExceptionUtil.getErrorMessageWithNestedException(e));
    }
  }

  /**
   * 输出error级别信息
   *
   * @param msg
   *        日志信息
   * @param e
   *        异常
   */
  public static void error(String msg, String e) {
    synchronized (CLogger.class) {
      CLogger.logger = getLogger(getCurrentClass());
      logger.error(msg, e);
    }
  }

  /**
   * 输出error级别信息
   *
   * @param msg
   *        日志信息
   * @param e
   *        异常
   */
  public static void error(String msg, Throwable e) {
    synchronized (CLogger.class) {
      CLogger.logger = getLogger(getCurrentClass());
      logger.error(msg, e);
    }
  }

  /**
   * 获取当前类名称
   *
   * @return String 调用当前类的类名称
   */
  private static String getCurrentClass() {
    String classname = "";
    StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
    for (int i = 0; i < stacks.length; i++) {
      // 循环判断堆栈中哪一个是com.fang.framework.io.CLogger
      if (stacks[i].getClassName().equals(CLogger.class.getName())) {
        // 判断下一个是不是，如果不是返回，是继续循环
        if (!stacks[i + 1].getClassName().equals(CLogger.class.getName())) {
          classname = stacks[i + 1].getClassName();
        }
      }
    }
    return classname;
  }
}
