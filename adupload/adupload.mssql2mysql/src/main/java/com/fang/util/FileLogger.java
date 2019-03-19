/**
 * File：FileLogger.java
 * Package： com.fang.util
 * Author：yaokaibao
 * Date：2016年12月20日 下午3:33:35
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.util;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * FileLogger
 *
 * @author yaokaibao
 */
public class FileLogger {

  /**
   * pathname
   */
  private static String pathname = "C:\\Users\\user\\Desktop\\task\\log.txt";

  /**
   * write file
   *
   * @param message
   *        message
   */
  private static void write(String message) {
    File file = new File(pathname);
    try {
      if (!file.exists()) {
        file.createNewFile(); // 创建文件
      }
      FileWriter writer = new FileWriter(file, true);
      writer.write(message);
      writer.write(System.getProperty("line.separator"));
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * setLogPath
   *
   * @param path
   *        path
   */
  public static void setLogPath(String path) {
    pathname = path;
  }

  /**
   * setLogName
   *
   * @param name
   *        name
   */
  public static void setLogName(String name) {
    pathname = pathname.replace("log", name);
  }

  /**
   * trace
   *
   * @param message
   *        message
   */
  public static void trace(String message) {
    message = dateTimeNow() + " |-TRACE " + message;
    write(message);
  }

  /**
   * debug
   *
   * @param message
   *        message
   */
  public static void debug(String message) {
    message = dateTimeNow() + " |-DEBUG " + message;
    write(message);
  }

  /**
   * info
   *
   * @param message
   *        message
   */
  public static void info(String message) {
    message = dateTimeNow() + " |-INFO " + message;
    write(message);
  }

  /**
   * warn
   *
   * @param message
   *        message
   */
  public static void warn(String message) {
    message = dateTimeNow() + " |-WARN " + message;
    write(message);
  }

  /**
   * error
   *
   * @param message
   *        message
   */
  public static void error(String message) {
    message = dateTimeNow() + " |-ERROR " + message;
    write(message);
  }

  /**
   * error
   *
   * @param e
   *        e
   */
  public static void error(Exception e) {
    String message = dateTimeNow() + " |-ERROR " + e.toString();
    write(message);
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
    String message = dateTimeNow() + " |-ERROR " + msg + "\r\n" + e;
    write(message);
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
    String message = dateTimeNow() + " |-ERROR " + msg + "\r\n" + e.toString();
    write(message);
  }

  /**
   * dateTimeNow
   *
   * @return now
   */
  private static String dateTimeNow() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
  }
}
