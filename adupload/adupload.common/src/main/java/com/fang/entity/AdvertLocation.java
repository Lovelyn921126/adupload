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
 * 视频广告位
 *
 * @author wangzhiyuan
 */
public class AdvertLocation extends BaseEntity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -5004994160882509571L;

  /**
   * id
   */
  private Long id;

  /**
   * 名字
   */
  private String name;

  /**
   * 对应的频道ID
   */
  private Long channelId;

  /**
   * 大小
   */
  private Double size;

  /**
   * 单位
   */
  private String units;

  /**
   * 宽
   */
  private Integer width;

  /**
   * 高
   */
  private Integer height;

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
   * getChannelId
   *
   * @return channelId
   */
  public Long getChannelId() {
    return channelId;
  }

  /**
   * setChannelId
   *
   * @param channelId
   *        set channelId
   */
  public void setChannelId(Long channelId) {
    this.channelId = channelId;
  }

  /**
   * getSize
   *
   * @return size
   */
  public Double getSize() {
    return size;
  }

  /**
   * setSize
   *
   * @param size
   *        set size
   */
  public void setSize(Double size) {
    this.size = size;
  }

  /**
   * getUnits
   *
   * @return units
   */
  public String getUnits() {
    return units;
  }

  /**
   * setUnits
   *
   * @param units
   *        set units
   */
  public void setUnits(String units) {
    this.units = units;
  }

  /**
   * getWidth
   *
   * @return width
   */
  public Integer getWidth() {
    return width;
  }

  /**
   * setWidth
   *
   * @param width
   *        set width
   */
  public void setWidth(Integer width) {
    this.width = width;
  }

  /**
   * getHeight
   *
   * @return height
   */
  public Integer getHeight() {
    return height;
  }

  /**
   * setHeight
   *
   * @param height
   *        set height
   */
  public void setHeight(Integer height) {
    this.height = height;
  }

}
