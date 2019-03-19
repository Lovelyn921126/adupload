/**
 * File：SlSysUser.java
 * Package：com.fang.pojoformssql
 * Author：yaokaibao
 * Date：2017年5月18日 下午4:39:14
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.pojoformssql;

import java.io.Serializable;
import java.util.Date;

/**
 * SlSysUser
 *
 * @author yaokaibao
 */
public class SlSysUser implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 8848306408975368600L;

  /**
   * id
   */
  private String id;

  /**
   * username
   */
  private String username;

  /**
   * name
   */
  private String name;

  /**
   * createTime
   */
  private Date createTime;

  /**
   * updateTime
   */
  private Date updateTime;

  /**
   * bloc
   */
  private String bloc;

  /**
   * workLocation
   */
  private String workLocation;

  /**
   * teleNumber
   */
  private String teleNumber;

  /**
   * 是否删除（1=删除，0=未删除）
   */
  private Integer isDelete;

  /**
   * 行号
   */
  private Long rowNumber;


  /**
   * id
   * @return id
   */
  public String getId() {
    return id;
  }


  /**
   * id
   * @param id
   *        id
   */
  public void setId(String id) {
    this.id = id;
  }


  /**
   * username
   * @return username
   */
  public String getUsername() {
    return username;
  }


  /**
   * username
   * @param username
   *        username
   */
  public void setUsername(String username) {
    this.username = username;
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


  /**
   * updateTime
   * @return updateTime
   */
  public Date getUpdateTime() {
    return updateTime;
  }


  /**
   * updateTime
   * @param updateTime
   *        updateTime
   */
  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }


  /**
   * bloc
   * @return bloc
   */
  public String getBloc() {
    return bloc;
  }


  /**
   * bloc
   * @param bloc
   *        bloc
   */
  public void setBloc(String bloc) {
    this.bloc = bloc;
  }


  /**
   * workLocation
   * @return workLocation
   */
  public String getWorkLocation() {
    return workLocation;
  }


  /**
   * workLocation
   * @param workLocation
   *        workLocation
   */
  public void setWorkLocation(String workLocation) {
    this.workLocation = workLocation;
  }


  /**
   * teleNumber
   * @return teleNumber
   */
  public String getTeleNumber() {
    return teleNumber;
  }


  /**
   * teleNumber
   * @param teleNumber
   *        teleNumber
   */
  public void setTeleNumber(String teleNumber) {
    this.teleNumber = teleNumber;
  }


  /**
   * isDelete
   * @return isDelete
   */
  public Integer getIsDelete() {
    return isDelete;
  }


  /**
   * isDelete
   * @param isDelete
   *        isDelete
   */
  public void setIsDelete(Integer isDelete) {
    this.isDelete = isDelete;
  }


  /**
   * rowNumber
   * @return rowNumber
   */
  public Long getRowNumber() {
    return rowNumber;
  }


  /**
   * rowNumber
   * @param rowNumber
   *        rowNumber
   */
  public void setRowNumber(Long rowNumber) {
    this.rowNumber = rowNumber;
  }
}
