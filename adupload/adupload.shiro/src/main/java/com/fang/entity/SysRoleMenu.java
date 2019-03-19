/**
 * File：SysRoleMenu.java
 * Package：com.fang.adupload.entity.po
 * Author：wangzhiyuan
 * Date：2017年3月24日 上午9:44:56
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity;

import java.io.Serializable;

/**
 * 角色与菜单对应关系
 *
 * @author wangzhiyuan
 */
public class SysRoleMenu implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -1903975671639514500L;

  /**
   * 关系ID
   */
  private String id;

  /**
   * 角色ID
   */
  private String roleId;

  /**
   * 菜单ID
   */
  private String menuId;


  /**
   * getId
   * @return id
   */
  public String getId() {
    return id;
  }


  /**
   * setId
   * @param id set id
   */
  public void setId(String id) {
    this.id = id;
  }


  /**
   * getRoleId
   * @return roleId
   */
  public String getRoleId() {
    return roleId;
  }


  /**
   * setRoleId
   * @param roleId set roleId
   */
  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }


  /**
   * getMenuId
   * @return menuId
   */
  public String getMenuId() {
    return menuId;
  }


  /**
   * setMenuId
   * @param menuId set menuId
   */
  public void setMenuId(String menuId) {
    this.menuId = menuId;
  }

}
