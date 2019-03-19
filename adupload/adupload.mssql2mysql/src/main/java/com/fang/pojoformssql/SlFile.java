/**
 * File：SlFile.java
 * Package：com.fang.pojoformssql
 * Author：yaokaibao
 * Date：2017年4月21日 上午10:15:00
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.pojoformssql;

import java.util.Date;

/**
 * SlFile
 *
 * @author yaokaibao
 */
public class SlFile {

  /**
   * id
   */
  private Long id;

  /**
   * sourceId
   */
  private String sourceId;

  /**
   * projectName
   */
  private String projectName;

  /**
   * sourceUrl
   */
  private String sourceUrl;

  /**
   * sourceType
   */
  private String sourceType;

  /**
   * sourceSize
   */
  private Integer sourceSize;

  /**
   * sourceWidth
   */
  private Integer sourceWidth;

  /**
   * sourceHeight
   */
  private Integer sourceHeight;

  /**
   * serviceType
   */
  private Integer serviceType;

  /**
   * cityId
   */
  private Integer cityId;

  /**
   * locationId
   */
  private Integer locationId;

  /**
   * channelId
   */
  private Integer channelId;

  /**
   * acquireInfoWay
   */
  private String acquireInfoWay;

  /**
   * uploadUserID
   */
  private String uploadUserID;

  /**
   * id
   */
  private String uploadUsername;

  /**
   * isDelete
   */
  private Integer isDelete;

  /**
   * createTime
   */
  private Date createTime;

  /**
   * updateTime
   */
  private Date updateTime;

  /**
   * SlFile
   */
  public SlFile() {
    super();
    id = 0L;
    serviceType = 0;
    cityId = 0;
    channelId = 0;
    sourceSize = 0;
    sourceWidth = 0;
    sourceHeight = 0;
    isDelete = 0;
    locationId = 0;
  }

  /**
   * id
   *
   * @return id
   */
  public Long getId() {
    return id;
  }

  /**
   * id
   *
   * @param id
   *        id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * sourceId
   *
   * @return sourceId
   */
  public String getSourceId() {
    return sourceId;
  }

  /**
   * sourceId
   *
   * @param sourceId
   *        sourceId
   */
  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  /**
   * projectName
   *
   * @return projectName
   */
  public String getProjectName() {
    return projectName;
  }

  /**
   * projectName
   *
   * @param projectName
   *        projectName
   */
  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  /**
   * sourceUrl
   *
   * @return sourceUrl
   */
  public String getSourceUrl() {
    return sourceUrl;
  }

  /**
   * sourceUrl
   *
   * @param sourceUrl
   *        sourceUrl
   */
  public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }

  /**
   * sourceType
   *
   * @return sourceExtension
   */
  public String getSourceType() {
    return sourceType;
  }

  /**
   * sourceType
   *
   * @param sourceType
   *        sourceType
   */
  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  /**
   * sourceSize
   *
   * @return sourceSize
   */
  public Integer getSourceSize() {
    return sourceSize;
  }

  /**
   * sourceSize
   *
   * @param sourceSize
   *        sourceSize
   */
  public void setSourceSize(Integer sourceSize) {
    this.sourceSize = sourceSize;
  }

  /**
   * sourceWidth
   *
   * @return sourceWidth
   */
  public Integer getSourceWidth() {
    return sourceWidth;
  }

  /**
   * sourceWidth
   *
   * @param sourceWidth
   *        sourceWidth
   */
  public void setSourceWidth(Integer sourceWidth) {
    this.sourceWidth = sourceWidth;
  }

  /**
   * sourceHeight
   *
   * @return sourceHeight
   */
  public Integer getSourceHeight() {
    return sourceHeight;
  }

  /**
   * sourceHeight
   *
   * @param sourceHeight
   *        sourceHeight
   */
  public void setSourceHeight(Integer sourceHeight) {
    this.sourceHeight = sourceHeight;
  }

  /**
   * serviceType
   *
   * @return serviceType
   */
  public Integer getServiceType() {
    return serviceType;
  }

  /**
   * serviceType
   *
   * @param serviceType
   *        serviceType
   */
  public void setServiceType(Integer serviceType) {
    this.serviceType = serviceType;
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
   * locationId
   *
   * @return locationId
   */
  public Integer getLocationId() {
    return locationId;
  }

  /**
   * locationId
   *
   * @param locationId
   *        locationId
   */
  public void setLocationId(Integer locationId) {
    this.locationId = locationId;
  }

  /**
   * channelId
   *
   * @return channelId
   */
  public Integer getChannelId() {
    return channelId;
  }

  /**
   * channelId
   *
   * @param channelId
   *        channelId
   */
  public void setChannelId(Integer channelId) {
    this.channelId = channelId;
  }

  /**
   * acquireInfoWay
   *
   * @return acquireInfoWay
   */
  public String getAcquireInfoWay() {
    return acquireInfoWay;
  }

  /**
   * acquireInfoWay
   *
   * @param acquireInfoWay
   *        acquireInfoWay
   */
  public void setAcquireInfoWay(String acquireInfoWay) {
    this.acquireInfoWay = acquireInfoWay;
  }

  /**
   * uploadUserID
   *
   * @return uploadUserID
   */
  public String getUploadUserID() {
    return uploadUserID;
  }

  /**
   * uploadUserID
   *
   * @param uploadUserID
   *        uploadUserID
   */
  public void setUploadUserID(String uploadUserID) {
    this.uploadUserID = uploadUserID;
  }

  /**
   * uploadUsername
   *
   * @return uploadUsername
   */
  public String getUploadUsername() {
    return uploadUsername;
  }

  /**
   * uploadUsername
   *
   * @param uploadUsername
   *        uploadUsername
   */
  public void setUploadUsername(String uploadUsername) {
    this.uploadUsername = uploadUsername;
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

  /**
   * createTime
   *
   * @return createTime
   */
  public Date getCreateTime() {
    return createTime;
  }

  /**
   * createTime
   *
   * @param createTime
   *        createTime
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  /**
   * updateTime
   *
   * @return updateTime
   */
  public Date getUpdateTime() {
    return updateTime;
  }

  /**
   * updateTime
   *
   * @param updateTime
   *        updateTime
   */
  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

}
