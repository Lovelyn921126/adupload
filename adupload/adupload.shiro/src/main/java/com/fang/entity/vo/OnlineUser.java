/**
 * File：OnlineUser.java
 * Package：com.fang.adupload.vo.out
 * Author：wangzhiyuan
 * Date：2017年3月27日 上午9:25:39
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity.vo;

import java.io.Serializable;

/**
 * 在线用户
 *
 * @author wangzhiyuan
 */
public class OnlineUser implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -997133926697144363L;

  /**
   * 用户登录IP
   */
  private String ip;

  /**
   * 用户名称
   */
  private String name;

  /**
   * 登录时间
   */
  private String loginTime;

  /**
   * 最后访问时间
   */
  private String lastAccessTime;

  /**
   * con
   */
  public OnlineUser() {
    super();
  }

  /**
   * OnlineUser
   *
   * @param ip
   *        ip
   * @param name
   *        用户名
   * @param loginTime
   *        登录时间
   * @param lastAccessTime
   *        最后访问时间
   */
  public OnlineUser(String ip, String name, String loginTime, String lastAccessTime) {
    this.ip = ip;
    this.name = name;
    this.loginTime = loginTime;
    this.lastAccessTime = lastAccessTime;
  }

  /**
   * getIp
   *
   * @return ip
   */
  public String getIp() {
    return ip;
  }

  /**
   * setIp
   *
   * @param ip
   *        set ip
   */
  public void setIp(String ip) {
    this.ip = ip;
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
   * getLoginTime
   *
   * @return loginTime
   */
  public String getLoginTime() {
    return loginTime;
  }

  /**
   * setLoginTime
   *
   * @param loginTime
   *        set loginTime
   */
  public void setLoginTime(String loginTime) {
    this.loginTime = loginTime;
  }

  /**
   * getLastAccessTime
   *
   * @return lastAccessTime
   */
  public String getLastAccessTime() {
    return lastAccessTime;
  }

  /**
   * setLastAccessTime
   *
   * @param lastAccessTime
   *        set lastAccessTime
   */
  public void setLastAccessTime(String lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }

}
