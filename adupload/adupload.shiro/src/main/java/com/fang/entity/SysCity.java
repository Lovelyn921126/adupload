/**
 * File：AdvertCity.java
 * Package：com.fang.adupload.entity.po
 * Author：wangzhiyuan
 * Date：2017年3月23日 下午5:53:11
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity;

import java.io.Serializable;

/**
 * 广告城市
 *
 * @author wangzhiyuan
 */
public class SysCity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 7217457632364921107L;

  /**
   * 城市编号
   */
  private Integer cityId;

  /**
   * 城市名称
   */
  private String name;

  /**
   * 城市简写
   */
  private String code;

  /**
   * 备注
   */
  private String remark;

  /**
   * 是否删除（1=删除，0=未删除）
   */
  private Integer isDelete;

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
   * getCode
   *
   * @return code
   */
  public String getCode() {
    return code;
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

  /**
   * getRemark
   *
   * @return remark
   */
  public String getRemark() {
    return remark;
  }

  /**
   * setCode
   *
   * @param code
   *        set code
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * getIsDelete
   *
   * @return isDelete
   */
  public Integer getIsDelete() {
    return isDelete;
  }

  /**
   * setIsDelete
   *
   * @param isDelete
   *        set isDelete
   */
  public void setIsDelete(Integer isDelete) {
    this.isDelete = isDelete;
  }

}
