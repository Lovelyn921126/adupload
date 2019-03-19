/**
 * File：SysUser.java
 * Package：com.fang.adupload.entity.po
 * Author：wangzhiyuan
 * Date：2017年3月24日 上午9:52:48
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity;

import java.io.Serializable;

/**
 * 系统用户表
 *
 * @author wangzhiyuan
 */
public class SysUser extends BaseEntity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -3862233473384775357L;

  /**
   * 用户ID
   */
  private String userId;

  /**
   * 用户名(邮箱)
   */
  private String username;

  /**
   * 密码
   */
  private transient String password;

  /**
   * 用户名字（真实名字）
   */
  private String name;

  /**
   * 手机号
   */
  private String mobile;

  /**
   * 账户类型（(默认)0:需要在OA入口登录，(少量)1:不经过OA;）
   */
  private Integer type;

  /**
   * 集团编号
   */
  private String groupId;

  /**
   * getUserId
   *
   * @return userId
   */
  public String getUserId() {
    return userId;
  }

  /**
   * setUserId
   *
   * @param userId
   *        set userId
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * getUsername
   *
   * @return username
   */
  public String getUsername() {
    return username;
  }

  /**
   * setUsername
   *
   * @param username
   *        set username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * getPassword
   *
   * @return password
   */
  public String getPassword() {
    return password;
  }

  /**
   * setPassword
   *
   * @param password
   *        set password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * getName
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * setName
   *
   * @param name
   *        set name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * getMobile
   *
   * @return mobile
   */
  public String getMobile() {
    return mobile;
  }

  /**
   * setMobile
   *
   * @param mobile
   *        set mobile
   */
  public void setMobile(String mobile) {
    this.mobile = mobile;
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
   * getGroupId
   *
   * @return groupId
   */
  public String getGroupId() {
    return groupId;
  }

  /**
   * setGroupId
   *
   * @param groupId
   *        set groupId
   */
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

}
