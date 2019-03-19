/**
 * File：SysRole.java
 * Package：com.fang.adupload.entity.po
 * Author：wangzhiyuan
 * Date：2017年3月24日 上午9:26:00
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity;

import java.io.Serializable;

/**
 * 角色
 *
 * @author wangzhiyuan
 */
public class SysRole extends BaseEntity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 1320995865005172689L;

  /**
   * 角色ID
   */
  private String roleId;

  /**
   * 父角色ID
   */
  public String parentId;

  /**
   * 角色名称
   */
  private String roleName;

  /**
   * 备注
   */
  private String remark;

  /**
   * getRoleId
   *
   * @return roleId
   */
  public String getRoleId() {
    return roleId;
  }

  /**
   * setRoleId
   *
   * @param roleId
   *        set roleId
   */
  public void setRoleId(String roleId) {
    this.roleId = roleId;
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
   * getRoleName
   *
   * @return roleName
   */
  public String getRoleName() {
    return roleName;
  }

  /**
   * setRoleName
   *
   * @param roleName
   *        set roleName
   */
  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  /**
   * getRemark
   *
   * @return remark
   */
  public String getRemark() {
    return remark;
  }

  /**
   * setRemark
   *
   * @param remark
   *        set remark
   */
  public void setRemark(String remark) {
    this.remark = remark;
  }

}
