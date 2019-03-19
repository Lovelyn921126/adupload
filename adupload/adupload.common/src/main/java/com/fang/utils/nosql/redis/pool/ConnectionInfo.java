package com.fang.utils.nosql.redis.pool;

import redis.clients.jedis.Protocol;

/**
 * redis connection信息
 *
 * @author wangzhiyuan
 */
public class ConnectionInfo {

  /**
   * 默认密码
   */
  public static final String DEFAULT_PASSWORD = null;

  /**
   * 默认DATABASE
   */
  private int database = Protocol.DEFAULT_DATABASE;

  /**
   * 默认密码
   */
  private String password = DEFAULT_PASSWORD;

  /**
   * 连接超时时间
   */
  private int timeout = Protocol.DEFAULT_TIMEOUT;

  /**
   * 无参构造
   */
  public ConnectionInfo() {
  }

  /**
   * 有参构造
   *
   * @param database
   *        database
   * @param password
   *        password
   * @param timeout
   *        timeout
   */
  public ConnectionInfo(int database, String password, int timeout) {
    this.timeout = timeout;
    this.password = password;
    this.database = database;
  }

  /**
   * getDatabase
   *
   * @return database
   */
  public int getDatabase() {
    return database;
  }

  /**
   * setDatabase
   *
   * @param database
   *        set database
   */
  public void setDatabase(int database) {
    this.database = database;
  }

  /**
   * getPassword
   *
   * @return password
   */
  public String getPassword() {
    return password;
  }

  /**
   * setPassword
   *
   * @param password
   *        set password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * getTimeout
   *
   * @return timeout
   */
  public int getTimeout() {
    return timeout;
  }

  /**
   * setTimeout
   *
   * @param timeout
   *        set timeout
   */
  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  @Override
  public String toString() {
    return "ConnectionInfo [database=" + database + ", password=" + password + ", timeout=" + timeout + "]";
  }
}
