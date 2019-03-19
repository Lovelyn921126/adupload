/**
 * File：AdvertLike.java
 * Package：com.fang.entity
 * Author：wangzhiyuan
 * Date：2017年5月12日 下午1:13:20
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity;

import java.io.Serializable;

/**
 * 人气、点赞表
 *
 * @author wangzhiyuan
 */
public class AdvertLike extends BaseEntity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 8920844802849996608L;

  /**
   * 文件id
   */
  private String panoid;

  /**
   * 人气数
   */
  private Integer pv;

  /**
   * 点赞数
   */
  private Integer up;

  /**
   * getPv
   *
   * @return pv
   */
  public Integer getPv() {
    return pv;
  }

  /**
   * setPv
   *
   * @param pv
   *        set pv
   */
  public void setPv(Integer pv) {
    this.pv = pv;
  }

  /**
   * getUp
   *
   * @return up
   */
  public Integer getUp() {
    return up;
  }

  /**
   * setUp
   *
   * @param up
   *        set up
   */
  public void setUp(Integer up) {
    this.up = up;
  }

  /**
   * getPanoid
   *
   * @return panoid
   */
  public String getPanoid() {
    return panoid;
  }

  /**
   * setPanoid
   *
   * @param panoid
   *        set panoid
   */
  public void setPanoid(String panoid) {
    this.panoid = panoid;
  }

}
