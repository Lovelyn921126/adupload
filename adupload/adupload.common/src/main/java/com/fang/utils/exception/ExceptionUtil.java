/**
 * File：ExceptionUtil.java
 * Package：com.fang.framework.utils
 * Author：wangzhiyuan
 * Date：2016年7月20日 上午11:36:22
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * 关于异常的工具类.
 *
 * 参考了guava的Throwables。
 *
 * @author wangzhiyuan
 */
public class ExceptionUtil extends ExceptionUtils {

  /**
   * 将CheckedException转换为UncheckedException.
   *
   * @param ex
   *        Throwable
   * @return 不被检查的异常
   */
  public static RuntimeException unchecked(Throwable ex) {
    if (ex instanceof RuntimeException) {
      return (RuntimeException) ex;
    } else {
      return new RuntimeException(ex);
    }
  }

  /**
   * 将ErrorStack转化为String.
   *
   * @param ex
   *        Throwable
   * @return 字符串类型的ErrorStack
   */
  public static String getStackTraceAsString(Throwable ex) {
    StringWriter stringWriter = new StringWriter();
    ex.printStackTrace(new PrintWriter(stringWriter));
    return stringWriter.toString();
  }

  /**
   * 获取组合本异常信息与底层异常信息的异常描述, 适用于本异常为统一包装异常类，底层异常才是根本原因的情况。
   *
   * @param ex
   *        Throwable
   * @return 组合本异常信息与底层异常信息的异常描述
   */
  public static String getErrorMessageWithNestedException(Throwable ex) {
    Throwable nestedException = ex.getCause();
    return nestedException == null ? "" : new StringBuilder()
        .append(ex.getMessage())
        .append(" nested exception is ")
        .append(nestedException.getClass().getName())
        .append(":")
        .append(nestedException.getMessage()).toString();
  }

  /**
   * 获取异常的Root Cause.
   *
   * @param ex
   *        Throwable
   * @return 异常的Root Cause
   */
  public static Throwable getRootCause(Throwable ex) {
    Throwable cause;
    while ((cause = ex.getCause()) != null) {
      ex = cause;
    }
    return ex;
  }

  /**
   * 判断异常是否由某些底层的异常引起.
   *
   * @param ex
   *        Exception
   * @param causeExceptionClasses
   *        causeExceptionClasses
   * @return true/false
   */
  @SuppressWarnings("unchecked")
  public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
    Throwable cause = ex;
    while (cause != null) {
      for (Class<? extends Exception> causeClass : causeExceptionClasses) {
        if (causeClass.isInstance(cause)) {
          return true;
        }
      }
      cause = cause.getCause();
    }
    return false;
  }
}
