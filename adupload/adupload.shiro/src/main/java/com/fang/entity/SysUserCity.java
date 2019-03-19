/**
 * File：SysUserCity.java
 * Package：com.fang.adupload.entity.po
 * Author：wangzhiyuan
 * Date：2017年3月24日 下午3:28:16
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity;

import java.io.Serializable;

/**
 * 用户与城市对应关系
 *
 * @author wangzhiyuan
 */
public class SysUserCity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -5622224133403065042L;

  /**
   * 关系ID
   */
  private String id;

  /**
   * 城市编号
   */
  private Integer cityId;

  /**
   * 用户编号
   */
  private String userId;

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
   * getCityId
   *
   * @return cityId
   */
  public Integer getCityId() {
    return cityId;
  }

  /**
   * setCityId
   *
   * @param cityId
   *        set cityId
   */
  public void setCityId(Integer cityId) {
    this.cityId = cityId;
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
}
