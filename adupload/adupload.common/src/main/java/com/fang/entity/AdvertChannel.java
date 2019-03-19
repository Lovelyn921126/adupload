/**
 * File：AdvertLocation.java
 * Package：com.fang.entity
 * Author：wangzhiyuan
 * Date：2017年4月14日 下午2:35:47
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity;

import java.io.Serializable;

/**
 * 视频频道
 *
 * @author wangzhiyuan
 */
public class AdvertChannel extends BaseEntity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 9038667171653568297L;

  /**
   * id
   */
  private Long id;

  /**
   * 名字
   */
  private String name;

  /**
   * 备注
   */
  private String remark;

  /**
   * getId
   *
   * @return id
   */
  public Long getId() {
    return id;
  }

  /**
   * setId
   *
   * @param id
   *        set id
   */
  public void setId(Long id) {
    this.id = id;
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
