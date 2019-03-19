/**
 * File：AdvertComment.java
 * Package：com.fang.entity
 * Author：wangzhiyuan
 * Date：2017年5月12日 下午1:18:43
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity;

import java.io.Serializable;

/**
 * 说一说表
 *
 * @author wangzhiyuan
 */
public class AdvertComment extends BaseEntity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 2888123456632409360L;

  /**
   * id
   */
  private String id;

  /**
   * 文件id
   */
  private String panoid;

  /**
   * 说一说内容
   */
  private String message;

  /**
   * 说一说内容
   */
  private String content;

  /**
   * 用户id
   */
  private String userid;

  /**
   * 头像URL
   */
  private String avatar;

  /**
   * 横坐标
   */
  private float ath;

  /**
   * 纵坐标
   */
  private float atv;

  /**
   * 全景/航拍的场景
   */
  private String scene;

  /**
   * getId
   *
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * setId
   *
   * @param id
   *        set id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * getMessage
   *
   * @return message
   */
  public String getMessage() {
    return message;
  }

  /**
   * setMessage
   *
   * @param message
   *        set message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * getAvatar
   *
   * @return avatar
   */
  public String getAvatar() {
    return avatar;
  }

  /**
   * setAvatar
   *
   * @param avatar
   *        set avatar
   */
  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  /**
   * getAth
   *
   * @return ath
   */
  public float getAth() {
    return ath;
  }

  /**
   * setAth
   *
   * @param ath
   *        set ath
   */
  public void setAth(float ath) {
    this.ath = ath;
  }

  /**
   * getAtv
   *
   * @return atv
   */
  public float getAtv() {
    return atv;
  }

  /**
   * setAtv
   *
   * @param atv
   *        set atv
   */
  public void setAtv(float atv) {
    this.atv = atv;
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

  /**
   * getUserid
   *
   * @return userid
   */
  public String getUserid() {
    return userid;
  }

  /**
   * setUserid
   *
   * @param userid
   *        set userid
   */
  public void setUserid(String userid) {
    this.userid = userid;
  }

  /**
   * getContent
   *
   * @return content
   */
  public String getContent() {
    return content;
  }

  /**
   * setContent
   *
   * @param content
   *        set content
   */
  public void setContent(String content) {
    this.content = content;
  }


  /**
   * getScene
   * @return scene
   */
  public String getScene() {
    return scene;
  }


  /**
   * setScene
   * @param scene set scene
   */
  public void setScene(String scene) {
    this.scene = scene;
  }

}
