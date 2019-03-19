/**
 * File：SysCity.java
 * Package：com.fang.pojoformysql
 * Author：yaokaibao
 * Date：2017年5月19日 下午2:44:34
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.pojoformysql;

import java.io.Serializable;

import com.fang.pojoformssql.SlSysCity;

/**
 * SysCity
 *
 * @author yaokaibao
 */
public class SysCity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 3061686338481700752L;

  /**
   * cityId
   */
  private Integer cityId;

  /**
   * name
   */
  private String name;

  /**
   * code
   */
  private String code;

  /**
   * remark
   */
  private String remark;

  /**
   * isDelete
   */
  private Integer isDelete;

  /**
   * SysCity
   */
  public SysCity() {
    super();
    cityId = 0;
    isDelete = 0;
  }

  /**
   * SysCity
   *
   * @param city
   *        city
   */
  public SysCity(SlSysCity city) {
    super();
    cityId = city.getId();
    name = city.getName();
    code = city.getCode();
    remark = city.getRemark();
    isDelete = 0;
  }

  /**
   * cityId
   *
   * @return cityId
   */
  public Integer getCityId() {
    return cityId;
  }

  /**
   * cityId
   *
   * @param cityId
   *        cityId
   */
  public void setCityId(Integer cityId) {
    this.cityId = cityId;
  }

  /**
   * name
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * name
   *
   * @param name
   *        name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * code
   *
   * @return code
   */
  public String getCode() {
    return code;
  }

  /**
   * code
   *
   * @param code
   *        code
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * remark
   *
   * @return remark
   */
  public String getRemark() {
    return remark;
  }

  /**
   * remark
   *
   * @param remark
   *        remark
   */
  public void setRemark(String remark) {
    this.remark = remark;
  }

  /**
   * isDelete
   *
   * @return isDelete
   */
  public Integer getIsDelete() {
    return isDelete;
  }

  /**
   * isDelete
   *
   * @param isDelete
   *        isDelete
   */
  public void setIsDelete(Integer isDelete) {
    this.isDelete = isDelete;
  }

}
