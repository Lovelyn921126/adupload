/**
 * File：AdvertFile.java
 * Package：com.fang.pojoformysql
 * Author：yaokaibao
 * Date：2017年4月21日 上午10:14:30
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.pojoformysql;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.fang.pojoformssql.SlFile;

/**
 * AdvertFile
 *
 * @author yaokaibao
 */
public class AdvertFile implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -8502925927124623207L;

  /**
   * id
   */
  private String id;

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
   * sourceExtension
   */
  private String sourceExtension;

  /**
   * sourceSize
   */
  private Double sourceSize;

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
  private Byte serviceType;

  /**
   * cityId
   */
  private Integer cityId;

  /**
   * locationId
   */
  private Long locationId;

  /**
   * channelId
   */
  private Long channelId;

  /**
   * acquireInfoWay
   */
  private Byte acquireInfoWay;

  /**
   * uploadUserId
   */
  private String uploadUserId;

  /**
   * uploadUsername
   */
  private String uploadUsername;

  /**
   * isDelete
   */
  private Byte isDelete;

  /**
   * createTime
   */
  private Date createTime;

  /**
   * updateTime
   */
  private Date updateTime;

  /**
   * AdvertFile
   */
  public AdvertFile() {
    super();
    sourceSize = 0.0;
    sourceWidth = 0;
    sourceHeight = 0;
    serviceType = 0;
    cityId = 0;
    locationId = 0L;
    channelId = 0L;
    acquireInfoWay = 0;
    isDelete = 0;
  }

  /**
   * AdvertFile
   *
   * @param file
   *        file
   */
  public AdvertFile(SlFile file) {
    super();
    id = UUID.randomUUID().toString().replaceAll("-", "");
    sourceId = file.getSourceId();
    projectName = file.getProjectName();
    sourceUrl = file.getSourceUrl();
    sourceExtension = file.getSourceType();
    sourceSize = file.getSourceSize() == null ? 0d : file.getSourceSize().doubleValue();
    sourceWidth = file.getSourceWidth();
    sourceHeight = file.getSourceHeight();
    serviceType = file.getServiceType() == null ? 1 : file.getServiceType().byteValue();
    cityId = file.getCityId();
    locationId = file.getLocationId() == null ? null : file.getLocationId().longValue();
    channelId = file.getChannelId() == null ? 0 : file.getChannelId().longValue();
    acquireInfoWay = file.getAcquireInfoWay() == null ? null : Byte.valueOf(file.getAcquireInfoWay());
    uploadUserId = file.getUploadUserID();
    uploadUsername = file.getUploadUsername();
    isDelete = file.getIsDelete() == null ? 0 : file.getIsDelete().byteValue();
    createTime = file.getCreateTime();
    updateTime = file.getUpdateTime();
  }

  /**
   * id
   *
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * id
   *
   * @param id
   *        id
   */
  public void setId(String id) {
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
   * sourceExtension
   *
   * @return sourceExtension
   */
  public String getSourceExtension() {
    return sourceExtension;
  }

  /**
   * sourceExtension
   *
   * @param sourceExtension
   *        sourceExtension
   */
  public void setSourceExtension(String sourceExtension) {
    this.sourceExtension = sourceExtension;
  }

  /**
   * sourceSize
   *
   * @return sourceSize
   */
  public Double getSourceSize() {
    return sourceSize;
  }

  /**
   * sourceSize
   *
   * @param sourceSize
   *        sourceSize
   */
  public void setSourceSize(Double sourceSize) {
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
  public Byte getServiceType() {
    return serviceType;
  }

  /**
   * serviceType
   *
   * @param serviceType
   *        serviceType
   */
  public void setServiceType(Byte serviceType) {
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
  public Long getLocationId() {
    return locationId;
  }

  /**
   * locationId
   *
   * @param locationId
   *        locationId
   */
  public void setLocationId(Long locationId) {
    this.locationId = locationId;
  }

  /**
   * channelId
   *
   * @return channelId
   */
  public Long getChannelId() {
    return channelId;
  }

  /**
   * channelId
   *
   * @param channelId
   *        channelId
   */
  public void setChannelId(Long channelId) {
    this.channelId = channelId;
  }

  /**
   * acquireInfoWay
   *
   * @return acquireInfoWay
   */
  public Byte getAcquireInfoWay() {
    return acquireInfoWay;
  }

  /**
   * acquireInfoWay
   *
   * @param acquireInfoWay
   *        acquireInfoWay
   */
  public void setAcquireInfoWay(Byte acquireInfoWay) {
    this.acquireInfoWay = acquireInfoWay;
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
  public Byte getIsDelete() {
    return isDelete;
  }

  /**
   * isDelete
   *
   * @param isDelete
   *        isDelete
   */
  public void setIsDelete(Byte isDelete) {
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

  @Override
  public boolean equals(Object arg0) {
    if (arg0 instanceof AdvertFile) {
      AdvertFile file = (AdvertFile) arg0;

      return this.sourceId.equals(file.getSourceId()) && this.projectName.equals(file.getProjectName()) && this.sourceUrl.equals(file.getSourceUrl())
          && this.sourceExtension.equals(file.getSourceExtension()) && this.sourceSize.equals(file.getSourceSize()) && this.sourceWidth.equals(file.getSourceWidth())
          && this.sourceHeight.equals(file.getSourceHeight()) && this.serviceType.equals(file.getServiceType()) && this.cityId.equals(file.getCityId())
          && this.locationId.equals(file.getLocationId()) && this.channelId.equals(file.getChannelId()) && this.acquireInfoWay.equals(file.getAcquireInfoWay())
          && this.uploadUsername.equals(file.getUploadUsername()) && this.isDelete.equals(file.getIsDelete()) && this.createTime.equals(file.getCreateTime())
          && this.updateTime.equals(file.getUpdateTime());
    }

    return false;
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }

  @Override
  public String toString() {
    return "AdvertFile [id=" + id + ", sourceId=" + sourceId + ", projectName=" + projectName + ", sourceUrl=" + sourceUrl + ", sourceExtension=" + sourceExtension
        + ", sourceSize=" + sourceSize + ", sourceWidth=" + sourceWidth + ", sourceHeight=" + sourceHeight + ", serviceType=" + serviceType + ", cityId=" + cityId
        + ", locationId=" + locationId + ", channelId=" + channelId + ", acquireInfoWay=" + acquireInfoWay + ", uploadUsername=" + uploadUsername + ", isDelete=" + isDelete
        + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
  }


  /**
   * uploadUserId
   * @return uploadUserId
   */
  public String getUploadUserId() {
    return uploadUserId;
  }


  /**
   * uploadUserId
   * @param uploadUserId
   *        uploadUserId
   */
  public void setUploadUserId(String uploadUserId) {
    this.uploadUserId = uploadUserId;
  }
}
