/**
 * File：AdvertFile.java
 * Package：com.fang.entity
 * Author：wangzhiyuan
 * Date：2017年4月18日 下午2:13:59
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity;

import java.io.Serializable;

/**
 * 业务上传文件信息表
 *
 * @author wangzhiyuan
 */
public class AdvertFile extends BaseEntity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -1976267895303207759L;

  /**
   * id
   */
  private String id;

  /**
   * 文件id
   */
  private String sourceId;

  /**
   * 项目名字
   */
  private String projectName;

  /**
   * 文件链接地址
   */
  private String sourceUrl;

  /**
   * 文件扩展名
   */
  private String sourceExtension;

  /**
   * 文件大小
   */
  private Double sourceSize;

  /**
   * 文件宽(px)
   */
  private Integer sourceWidth;

  /**
   * 文件高(px)
   */
  private Integer sourceHeight;

  /**
   * 文件类型(1:普通广告；2:360全景)
   */
  private Integer serviceType;

  /**
   * 对应的城市id
   */
  private Integer cityId;

  /**
   * 对应的广告位id
   */
  private Integer locationId;

  /**
   * 频道id
   */
  private Integer channelId;

  /**
   * 数据来源渠道(1:接口；2:用户直接上传；-1:无定义)
   */
  private Integer acquireInfoWay;

  /**
   * 上传者ID
   */
  private String uploadUserId;

  /**
   * 上传者邮箱
   */
  private String uploadUsername;


  /**
   * getSourceId
   * @return sourceId
   */
  public String getSourceId() {
    return sourceId;
  }


  /**
   * setSourceId
   * @param sourceId set sourceId
   */
  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }


  /**
   * getProjectName
   * @return projectName
   */
  public String getProjectName() {
    return projectName;
  }


  /**
   * setProjectName
   * @param projectName set projectName
   */
  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }


  /**
   * getSourceUrl
   * @return sourceUrl
   */
  public String getSourceUrl() {
    return sourceUrl;
  }


  /**
   * setSourceUrl
   * @param sourceUrl set sourceUrl
   */
  public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }


  /**
   * getSourceExtension
   * @return sourceExtension
   */
  public String getSourceExtension() {
    return sourceExtension;
  }


  /**
   * setSourceExtension
   * @param sourceExtension set sourceExtension
   */
  public void setSourceExtension(String sourceExtension) {
    this.sourceExtension = sourceExtension;
  }


  /**
   * getSourceSize
   * @return sourceSize
   */
  public Double getSourceSize() {
    return sourceSize;
  }


  /**
   * setSourceSize
   * @param sourceSize set sourceSize
   */
  public void setSourceSize(Double sourceSize) {
    this.sourceSize = sourceSize;
  }


  /**
   * getSourceWidth
   * @return sourceWidth
   */
  public Integer getSourceWidth() {
    return sourceWidth;
  }


  /**
   * setSourceWidth
   * @param sourceWidth set sourceWidth
   */
  public void setSourceWidth(Integer sourceWidth) {
    this.sourceWidth = sourceWidth;
  }


  /**
   * getSourceHeight
   * @return sourceHeight
   */
  public Integer getSourceHeight() {
    return sourceHeight;
  }


  /**
   * setSourceHeight
   * @param sourceHeight set sourceHeight
   */
  public void setSourceHeight(Integer sourceHeight) {
    this.sourceHeight = sourceHeight;
  }


  /**
   * getServiceType
   * @return serviceType
   */
  public Integer getServiceType() {
    return serviceType;
  }


  /**
   * setServiceType
   * @param serviceType set serviceType
   */
  public void setServiceType(Integer serviceType) {
    this.serviceType = serviceType;
  }


  /**
   * getCityId
   * @return cityId
   */
  public Integer getCityId() {
    return cityId;
  }


  /**
   * setCityId
   * @param cityId set cityId
   */
  public void setCityId(Integer cityId) {
    this.cityId = cityId;
  }


  /**
   * getLocationId
   * @return locationId
   */
  public Integer getLocationId() {
    return locationId;
  }


  /**
   * setLocationId
   * @param locationId set locationId
   */
  public void setLocationId(Integer locationId) {
    this.locationId = locationId;
  }


  /**
   * getChannelId
   * @return channelId
   */
  public Integer getChannelId() {
    return channelId;
  }


  /**
   * setChannelId
   * @param channelId set channelId
   */
  public void setChannelId(Integer channelId) {
    this.channelId = channelId;
  }


  /**
   * getAcquireInfoWay
   * @return acquireInfoWay
   */
  public Integer getAcquireInfoWay() {
    return acquireInfoWay;
  }


  /**
   * setAcquireInfoWay
   * @param acquireInfoWay set acquireInfoWay
   */
  public void setAcquireInfoWay(Integer acquireInfoWay) {
    this.acquireInfoWay = acquireInfoWay;
  }

  /**
   * getUploadUserId
   * @return uploadUserId
   */
  public String getUploadUserId() {
    return uploadUserId;
  }


  /**
   * setUploadUserId
   * @param uploadUserId set uploadUserId
   */
  public void setUploadUserId(String uploadUserId) {
    this.uploadUserId = uploadUserId;
  }


  /**
   * getUploadUsername
   * @return uploadUsername
   */
  public String getUploadUsername() {
    return uploadUsername;
  }


  /**
   * setUploadUsername
   * @param uploadUsername set uploadUsername
   */
  public void setUploadUsername(String uploadUsername) {
    this.uploadUsername = uploadUsername;
  }



  /**
   * getId
   * @return id
   */
  public String getId() {
    return id;
  }



  /**
   * setId
   * @param id set id
   */
  public void setId(String id) {
    this.id = id;
  }

}
