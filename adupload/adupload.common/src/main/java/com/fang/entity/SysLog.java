package com.fang.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志
 *
 * @author wangzhiyuan
 */
public class SysLog implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 7444930425970628060L;

  /**
   * id
   */
  private Long id;

  /**
   * 用户名
   */
  private String username;

  /**
   * 用户操作
   */
  private String operation;

  /**
   * 请求方法
   */
  private String method;

  /**
   * 请求参数
   */
  private String params;

  /**
   * IP地址
   */
  private String ip;

  /**
   * 创建时间
   */
  private Date createDate;

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
   * getUsername
   *
   * @return username
   */
  public String getUsername() {
    return username;
  }

  /**
   * setUsername
   *
   * @param username
   *        set username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * getOperation
   *
   * @return operation
   */
  public String getOperation() {
    return operation;
  }

  /**
   * setOperation
   *
   * @param operation
   *        set operation
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * getMethod
   *
   * @return method
   */
  public String getMethod() {
    return method;
  }

  /**
   * setMethod
   *
   * @param method
   *        set method
   */
  public void setMethod(String method) {
    this.method = method;
  }

  /**
   * getParams
   *
   * @return params
   */
  public String getParams() {
    return params;
  }

  /**
   * setParams
   *
   * @param params
   *        set params
   */
  public void setParams(String params) {
    this.params = params;
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
   * getCreateDate
   *
   * @return createDate
   */
  public Date getCreateDate() {
    return createDate;
  }

  /**
   * setCreateDate
   *
   * @param createDate
   *        set createDate
   */
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
