/**
 * File：AdvertFileVo.java
 * Package：com.fang.entity.vo
 * Author：wangzhiyuan
 * Date：2017年4月18日 下午3:42:25
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity.vo;

import com.fang.entity.AdvertFile;

/**
 * 广告管理vo
 *
 * @author wangzhiyuan
 */
public class AdvertFileVo extends AdvertFile {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -1287285162484876087L;

  /**
   * 城市名字
   */
  private String cityName;

  /**
   * 网络路径
   */
  private String netRoad;

  /**
   * 关联的频道名字
   */
  private String channelName;

  /**
   * 关联的广告位名字
   */
  private String locationName;

  /**
   * 原文件地址
   */
  private String oriAdver;

  /**
   * 720云
   */
  private Boolean yunUsed;

  /**
   * 项目ID
   */
  private String projectId;

  /**
   * 项目类型
   */
  private String projectType;

  /**
   * 楼盘名
   */
  private String buildingName;

  /**
   * 集团code
   */
  private String group;

  /**
   * 图片URL
   */
  private String imageURL;

  /**
   * 描述
   */
  private String description;

  /**
   * 人气
   */
  private Boolean pv;

  /**
   * 点赞
   */
  private Boolean up;

  /**
   * 说一说
   */
  private Boolean sm;

  /**
   * 是否转换index
   */
  private Boolean creatable;

  /**
   * AdvertFileVo
   */
  public AdvertFileVo() {
    super();
    pv = false;
    up = false;
    sm = false;
    creatable = false;
  }

  /**
   * getCityName
   *
   * @return cityName
   */
  public String getCityName() {
    return cityName;
  }

  /**
   * setCityName
   *
   * @param cityName
   *        set cityName
   */
  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  /**
   * getNetRoad
   *
   * @return netRoad
   */
  public String getNetRoad() {
    return netRoad;
  }

  /**
   * setNetRoad
   *
   * @param netRoad
   *        set netRoad
   */
  public void setNetRoad(String netRoad) {
    this.netRoad = netRoad;
  }

  /**
   * getChannelName
   *
   * @return channelName
   */
  public String getChannelName() {
    return channelName;
  }

  /**
   * setChannelName
   *
   * @param channelName
   *        set channelName
   */
  public void setChannelName(String channelName) {
    this.channelName = channelName;
  }

  /**
   * getOriAdver
   *
   * @return oriAdver
   */
  public String getOriAdver() {
    return oriAdver;
  }

  /**
   * setOriAdver
   *
   * @param oriAdver
   *        set oriAdver
   */
  public void setOriAdver(String oriAdver) {
    this.oriAdver = oriAdver;
  }

  /**
   * getLocationName
   *
   * @return locationName
   */
  public String getLocationName() {
    return locationName;
  }

  /**
   * setLocationName
   *
   * @param locationName
   *        set locationName
   */
  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  /**
   * yunUsed
   *
   * @return yunUsed
   */
  public Boolean getYunUsed() {
    return yunUsed;
  }

  /**
   * yunUsed
   *
   * @param yunUsed
   *        yunUsed
   */
  public void setYunUsed(Boolean yunUsed) {
    this.yunUsed = yunUsed;
  }

  /**
   * imageURL
   *
   * @return imageURL
   */
  public String getImageURL() {
    return imageURL;
  }

  /**
   * imageURL
   *
   * @param imageURL
   *        imageURL
   */
  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  /**
   * description
   *
   * @return description
   */
  public String getDescription() {
    return description;
  }

  /**
   * description
   *
   * @param description
   *        description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * projectId
   *
   * @return projectId
   */
  public String getProjectId() {
    return projectId;
  }

  /**
   * projectId
   *
   * @param projectId
   *        projectId
   */
  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }

  /**
   * projectType
   *
   * @return projectType
   */
  public String getProjectType() {
    return projectType;
  }

  /**
   * projectType
   *
   * @param projectType
   *        projectType
   */
  public void setProjectType(String projectType) {
    this.projectType = projectType;
  }

  /**
   * buildingName
   *
   * @return buildingName
   */
  public String getBuildingName() {
    return buildingName;
  }

  /**
   * buildingName
   *
   * @param buildingName
   *        buildingName
   */
  public void setBuildingName(String buildingName) {
    this.buildingName = buildingName;
  }

  /**
   * group
   *
   * @return group
   */
  public String getGroup() {
    return group;
  }

  /**
   * group
   *
   * @param group
   *        group
   */
  public void setGroup(String group) {
    this.group = group;
  }

  /**
   * pv
   *
   * @return pv
   */
  public Boolean getPv() {
    return pv;
  }

  /**
   * pv
   *
   * @param pv
   *        pv
   */
  public void setPv(Boolean pv) {
    this.pv = pv;
  }

  /**
   * up
   *
   * @return up
   */
  public Boolean getUp() {
    return up;
  }

  /**
   * up
   *
   * @param up
   *        up
   */
  public void setUp(Boolean up) {
    this.up = up;
  }

  /**
   * sm
   *
   * @return sm
   */
  public Boolean getSm() {
    return sm;
  }

  /**
   * sm
   *
   * @param sm
   *        sm
   */
  public void setSm(Boolean sm) {
    this.sm = sm;
  }

  /**
   * creatable
   *
   * @return creatable
   */
  public Boolean getCreatable() {
    return creatable;
  }

  /**
   * creatable
   *
   * @param creatable
   *        creatable
   */
  public void setCreatable(Boolean creatable) {
    this.creatable = creatable;
  }

}
