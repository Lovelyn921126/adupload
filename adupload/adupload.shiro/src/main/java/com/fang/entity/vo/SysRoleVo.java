/**
 * File：SysRoleVo.java
 * Package：com.fang.shiro.entity.vo
 * Author：wangzhiyuan
 * Date：2017年3月29日 下午5:30:53
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity.vo;

import java.util.List;

import com.fang.entity.SysRole;

/**
 * 系统角色
 *
 * @author wangzhiyuan
 */
public class SysRoleVo extends SysRole {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -7474929774827039606L;

  /**
   * 角色对应的菜单列表
   */
  private List<String> menuIdList;

  /**
   * 父角色名称
   */
  private String parentName;

  /**
   * getMenuIdList
   *
   * @return menuIdList
   */
  public List<String> getMenuIdList() {
    return menuIdList;
  }

  /**
   * setMenuIdList
   *
   * @param menuIdList
   *        set menuIdList
   */
  public void setMenuIdList(List<String> menuIdList) {
    this.menuIdList = menuIdList;
  }

  /**
   * getParentName
   *
   * @return parentName
   */
  public String getParentName() {
    return parentName;
  }

  /**
   * setParentName
   *
   * @param parentName
   *        set parentName
   */
  public void setParentName(String parentName) {
    this.parentName = parentName;
  }

}
