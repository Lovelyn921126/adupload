/**
 * File：baseEntity.java
 * Package：com.fang.adupload.entity.po
 * Author：wangzhiyuan
 * Date：2017年3月24日 下午3:37:33
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体基类
 *
 * @author wangzhiyuan
 */
public class BaseEntity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 6983653674637617070L;

  /**
   * 显示顺序（越大越在上）
   */
  private Integer orderNum;

  /**
   * 是否删除（1=删除，0=未删除）
   */
  private Integer isDelete;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 更新时间
   */
  private Date updateTime;

  /**
   * 创建者ID
   */
  private String createUserId;

  /**
   * getOrderNum
   *
   * @return orderNum
   */
  public Integer getOrderNum() {
    return orderNum;
  }

  /**
   * setOrderNum
   *
   * @param orderNum
   *        set orderNum
   */
  public void setOrderNum(Integer orderNum) {
    this.orderNum = orderNum;
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

  /**
   * getCreateTime
   *
   * @return createTime
   */
  public Date getCreateTime() {
    return createTime;
  }

  /**
   * setCreateTime
   *
   * @param createTime
   *        set createTime
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  /**
   * getUpdateTime
   *
   * @return updateTime
   */
  public Date getUpdateTime() {
    return updateTime;
  }

  /**
   * setUpdateTime
   *
   * @param updateTime
   *        set updateTime
   */
  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  /**
   * getCreateUserId
   * @return createUserId
   */
  public String getCreateUserId() {
    return createUserId;
  }


  /**
   * setCreateUserId
   * @param createUserId set createUserId
   */
  public void setCreateUserId(String createUserId) {
    this.createUserId = createUserId;
  }

}
