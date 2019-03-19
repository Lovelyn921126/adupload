/**
 * File：ShiroEnums.java
 * Package：com.fang.shiro.utils.enums
 * Author：wangzhiyuan
 * Date：2017年3月29日 下午2:54:39
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.utils.enums;

/**
 * 权限枚举
 *
 * @author wangzhiyuan
 */
public enum ShiroEnums implements BasicEnums {

  /**
   * 枚举
   */
  CATALOG("目录", 0), MENU("菜单", 1), BUTTON("按钮", 2);

  /**
   * 状态值
   */
  private int code;

  /**
   * 状态名称
   */
  private String display;

  /**
   * 有参构造
   *
   * @param display
   *        状态名称
   * @param code
   *        状态值
   */
  ShiroEnums(String display, int code) {
    this.code = code;
    this.display = display;
  }

  /**
   * 根据code找到相对应的状态名称
   *
   * @param code
   *        状态值
   * @return 状态名称
   */
  public static String getDisplay(int code) {
    for (ShiroEnums c : ShiroEnums.values()) {
      if (c.getCode() == code) {
        return c.display;
      }
    }
    return null;
  }

  /**
   * 根据code找到相对应的状态名称
   *
   * @param display
   *        状态值
   * @return 状态名称
   */
  public static int getCode(String display) {
    for (ShiroEnums c : ShiroEnums.values()) {
      if (c.getDisplay().equals(display)) {
        return c.code;
      }
    }
    return -1;
  }

  @Override
  public int getCode() {
    return this.code;
  }

  @Override
  public String getDisplay() {
    return this.display;
  }

  @Override
  public String getEnumString() {
    return name();
  }
}
