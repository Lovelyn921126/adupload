/**
 * File：SysMenu.java
 * Package：com.fang.adupload.entity.po
 * Author：wangzhiyuan
 * Date：2017年3月24日 上午9:16:28
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity;

import java.io.Serializable;

/**
 * 菜单
 *
 * @author wangzhiyuan
 */
public class SysMenu extends BaseEntity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 429454327563961490L;

  /**
   * 菜单ID
   */
  private String menuId;

  /**
   * 父菜单ID，一级菜单为0
   */
  private String parentId;

  /**
   * 当前菜单名称
   */
  private String menuName;

  /**
   * 菜单URL
   */
  private String url;

  /**
   * 授权(多个用逗号分隔，如：user:list,user:create)
   */
  private String perms;

  /**
   * 类型 0：目录(文件夹) 1：菜单(节点) 2：按钮(页面内操作)
   */
  private Integer type;

  /**
   * 菜单图标
   */
  private String icon;

  /**
   * getMenuId
   *
   * @return menuId
   */
  public String getMenuId() {
    return menuId;
  }

  /**
   * setMenuId
   *
   * @param menuId
   *        set menuId
   */
  public void setMenuId(String menuId) {
    this.menuId = menuId;
  }

  /**
   * getParentId
   *
   * @return parentId
   */
  public String getParentId() {
    return parentId;
  }

  /**
   * setParentId
   *
   * @param parentId
   *        set parentId
   */
  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  /**
   * getMenuName
   *
   * @return menuName
   */
  public String getName() {
    return menuName;
  }

  /**
   * setMenuName
   *
   * @param menuName
   *        set menuName
   */
  public void setName(String menuName) {
    this.menuName = menuName;
  }

  /**
   * getUrl
   *
   * @return url
   */
  public String getUrl() {
    return url;
  }

  /**
   * setUrl
   *
   * @param url
   *        set url
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * getPerms
   *
   * @return perms
   */
  public String getPerms() {
    return perms;
  }

  /**
   * setPerms
   *
   * @param perms
   *        set perms
   */
  public void setPerms(String perms) {
    this.perms = perms;
  }

  /**
   * getType
   *
   * @return type
   */
  public Integer getType() {
    return type;
  }

  /**
   * setType
   *
   * @param type
   *        set type
   */
  public void setType(Integer type) {
    this.type = type;
  }

  /**
   * getIcon
   *
   * @return icon
   */
  public String getIcon() {
    return icon;
  }

  /**
   * setIcon
   *
   * @param icon
   *        set icon
   */
  public void setIcon(String icon) {
    this.icon = icon;
  }

}
