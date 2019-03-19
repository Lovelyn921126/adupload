/**
 * File：BasicEnums.java
 * Package：com.fang.framework.lang.enums
 * Author：wangzhiyuan
 * Date：2016年8月1日 下午4:42:36
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.enums;

/**
 * 基础枚举类
 *
 * @author wangzhiyuan
 */
public interface BasicEnums {

  /**
   * 获取状态值
   *
   * @return 状态值
   */
  int getCode();

  /**
   * 获取状态名称
   *
   * @return 状态名称
   */
  String getDisplay();

  /**
   * 返回此枚举常量的名称
   *
   * @return 此枚举常量的名称
   */
  String getEnumString();

}
