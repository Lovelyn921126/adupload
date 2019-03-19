/**
 * File：SlSysCity.java
 * Package：com.fang.pojoformssql
 * Author：yaokaibao
 * Date：2017年5月19日 下午3:25:21
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.pojoformssql;

import java.io.Serializable;
import java.util.Date;


/**
 * SlSysCity
 * @author yaokaibao
 */
public class SlSysCity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -5962859351771038302L;

  /**
   * id
   */
  private Integer id;

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
   * createTime
   */
  private Date createTime;

  /**
   * id
   * @return id
   */
  public Integer getId() {
    return id;
  }

  /**
   * id
   * @param id
   *        id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * name
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * name
   * @param name
   *        name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * code
   * @return code
   */
  public String getCode() {
    return code;
  }

  /**
   * code
   * @param code
   *        code
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * remark
   * @return remark
   */
  public String getRemark() {
    return remark;
  }

  /**
   * remark
   * @param remark
   *        remark
   */
  public void setRemark(String remark) {
    this.remark = remark;
  }

  /**
   * createTime
   * @return createTime
   */
  public Date getCreateTime() {
    return createTime;
  }

  /**
   * createTime
   * @param createTime
   *        createTime
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}
