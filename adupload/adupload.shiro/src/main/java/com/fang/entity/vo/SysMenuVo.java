/**
 * File：SysMenuVo.java
 * Package：SysMenu
 * Author：wangzhiyuan
 * Date：2017年3月29日 下午4:23:29
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity.vo;

import java.util.List;

import com.fang.entity.SysMenu;

/**
 * 系统菜单vo
 *
 * @author wangzhiyuan
 */
public class SysMenuVo extends SysMenu {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -4953919088474311669L;

  /**
   * 父菜单名称
   */
  private String parentName;

  /**
   * ztree属性
   */
  private Boolean open;

  /**
   * list
   */
  private List<?> list;

  /**
   * getOpen
   *
   * @return open
   */
  public Boolean getOpen() {
    return open;
  }

  /**
   * setOpen
   *
   * @param open
   *        set open
   */
  public void setOpen(Boolean open) {
    this.open = open;
  }

  /**
   * getList
   *
   * @return list
   */
  public List<?> getList() {
    return list;
  }

  /**
   * setList
   *
   * @param list
   *        set list
   */
  public void setList(List<?> list) {
    this.list = list;
  }


  /**
   * getParentName
   * @return parentName
   */
  public String getParentName() {
    return parentName;
  }


  /**
   * setParentName
   * @param parentName set parentName
   */
  public void setParentName(String parentName) {
    this.parentName = parentName;
  }

}
