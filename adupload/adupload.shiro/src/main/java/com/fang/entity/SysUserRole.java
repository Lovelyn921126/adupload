/**
 * File：SysUserRole.java
 * Package：com.fang.adupload.entity.po
 * Author：wangzhiyuan
 * Date：2017年3月24日 下午3:31:20
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity;

import java.io.Serializable;

/**
 * 用户与角色对应关系
 *
 * @author wangzhiyuan
 */
public class SysUserRole implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 6932422228907882549L;

  /**
   * 关系ID
   */
  private String id;

  /**
   * 用户ID
   */
  private String userId;

  /**
   * 角色ID
   */
  private String roleId;


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
   * getUserId
   * @return userId
   */
  public String getUserId() {
    return userId;
  }


  /**
   * setUserId
   * @param userId set userId
   */
  public void setUserId(String userId) {
    this.userId = userId;
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

}
