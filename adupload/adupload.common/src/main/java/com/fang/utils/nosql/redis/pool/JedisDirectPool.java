package com.fang.utils.nosql.redis.pool;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 直连redis的pool
 *
 * @author wangzhiyuan
 */
public class JedisDirectPool extends JedisPool {

  /**
   * 连接redis池子
   *
   * @param poolName
   *        连接池名称
   * @param address
   *        地址
   * @param config
   *        配置
   */
  public JedisDirectPool(String poolName, HostAndPort address, JedisPoolConfig config) {
    this(poolName, address, new ConnectionInfo(), config);
  }

  /**
   * 连接redis池子
   *
   * @param poolName
   *        连接池名称
   * @param host
   *        host
   * @param port
   *        port
   * @param passpost
   *        passpost
   * @param config
   *        配置
   */
  public JedisDirectPool(String poolName, String host, int port, String passpost, JedisPoolConfig config) {
    HostAndPort address = new HostAndPort(host, port);
    ConnectionInfo connectionInfo = new ConnectionInfo(0, passpost, 1000);
    initInternalPool(address, connectionInfo, config);
    this.poolName = poolName;
  }

  /**
   * 连接redis池子
   *
   * @param poolName
   *        连接池名称
   * @param host
   *        host
   * @param port
   *        port
   * @param passpost
   *        passpost
   * @param config
   *        配置
   * @param database
   *        数据库（0到15）
   */
  public JedisDirectPool(String poolName, String host, int port, String passpost, JedisPoolConfig config, int database) {
    HostAndPort address = new HostAndPort(host, port);
    ConnectionInfo connectionInfo = new ConnectionInfo(database, passpost, 1000);
    initInternalPool(address, connectionInfo, config);
    this.poolName = poolName;
  }

  /**
   * 连接redis池子
   *
   * @param poolName
   *        address
   * @param address
   *        地址
   * @param connectionInfo
   *        redis connection信息
   * @param config
   *        配置
   */
  public JedisDirectPool(String poolName, HostAndPort address, ConnectionInfo connectionInfo, JedisPoolConfig config) {
    initInternalPool(address, connectionInfo, config);
    this.poolName = poolName;
  }
}
