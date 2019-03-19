/**
 * File：UserInfo.java
 * Package：com.fang.coupon.entity
 * Author：wangzhiyuan
 * Date：2016年9月27日 下午2:13:16
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.entity;

import java.io.Serializable;

/**
 * UserInfo实体
 *
 * @author wangzhiyuan
 */
public class UserInfo extends BaseEntity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 6694240185897454176L;

  /**
   * 用户表 唯一编号
   */
  private String id;

  /**
   * 用户编号(通行证编号)
   */
  private String userId;

  /**
   * 用户名
   */
  private String name;

  /**
   * 用户手机号
   */
  private String mobile;

  /**
   * 邮箱地址
   */
  private String email;

  /**
   * 用户头像
   */
  private String avatar;

  /**
   * 无参构造
   */
  public UserInfo() {

  }

  /**
   * getId
   *
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * setId
   *
   * @param id
   *        set id
   */
  public void setId(String id) {
    this.id = id;
  }

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
   * getEmail
   *
   * @return email
   */
  public String getEmail() {
    return email;
  }

  /**
   * setEmail
   *
   * @param email
   *        set email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * getAvatar
   *
   * @return avatar
   */
  public String getAvatar() {
    return avatar;
  }

  /**
   * setAvatar
   *
   * @param avatar
   *        set avatar
   */
  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

}
