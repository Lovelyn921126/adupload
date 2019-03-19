/**
 * File：ApplicationContextAware.java
 * Package：com.fang.framework.context
 * Author：wangzhiyuan
 * Date：2016年6月28日 下午5:47:18
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候中取出ApplicaitonContext.
 *
 * @author wangzhiyuan
 */
public class SpringContextUtil implements ApplicationContextAware {

  /**
   * 用于持有ApplicationContext
   */
  private static ApplicationContext applicationContext;

  /**
   * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
   *
   * @param applicationContext
   *        applicationContext
   */
  public void setApplicationContext(ApplicationContext applicationContext) {
    SpringContextUtil.applicationContext = applicationContext; // NOSONAR
  }

  /**
   * 取得存储在静态变量中的ApplicationContext.
   *
   * @return ApplicationContext
   */
  public static ApplicationContext getApplicationContext() {
    checkApplicationContext();
    return applicationContext;
  }

  /**
   * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.(通过名字)
   *
   * @param name
   *        spring bean 名字
   * @param <T>
   *        泛型
   * @return bean对象
   */
  @SuppressWarnings("unchecked")
  public static <T> T getBean(String name) {
    checkApplicationContext();
    return (T) applicationContext.getBean(name);
  }

  /**
   * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.(通过类型)
   *
   * @param clazz
   *        类型
   * @param <T>
   *        泛型
   * @return bean对象
   */
  @SuppressWarnings("unchecked")
  public static <T> T getBean(Class<T> clazz) {
    checkApplicationContext();
    return (T) applicationContext.getBeansOfType(clazz);
  }

  /**
   * 清除applicationContext静态变量.
   */
  public static void cleanApplicationContext() {
    applicationContext = null;
  }

  /**
   * 检查applicationContext静态变量.
   */
  private static void checkApplicationContext() {
    if (applicationContext == null) {
      throw new IllegalStateException(
          "applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
    }
  }
}
