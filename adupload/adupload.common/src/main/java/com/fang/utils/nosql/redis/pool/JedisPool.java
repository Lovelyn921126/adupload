package com.fang.utils.nosql.redis.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

/**
 * JedisPool抽象类
 *
 * @author wangzhiyuan
 */
public abstract class JedisPool extends Pool<Jedis> {

  /**
   * 连接池名称
   */
  protected String poolName;

  /**
   * 地址
   */
  protected HostAndPort address;

  /**
   * 连接信息
   */
  protected ConnectionInfo connectionInfo;

  /**
   * Create a JedisPoolConfig with new maxPoolSize becasuse JedisPoolConfig's default maxPoolSize is
   * only 8.
   * Also reset the idle checking time to 10 minutes, the default value is half minute.
   * Also rest the max idle to zero, the default value is 8 too.
   * The default idle time is 60 seconds.
   *
   * @param maxPoolSize
   *        池子大小
   * @return JedisPoolConfig
   */
  public static JedisPoolConfig createPoolConfig(int maxPoolSize) {
    JedisPoolConfig config = new JedisPoolConfig();
    config.setMaxTotal(maxPoolSize);
    config.setMaxIdle(maxPoolSize);

    config.setTimeBetweenEvictionRunsMillis(600 * 1000);

    return config;
  }

  /**
   * Initialize the internal pool with connection info and pool config.
   *
   * @param address
   *        连接地址
   * @param connectionInfo
   *        连接信息
   * @param config
   *        配置
   */
  protected void initInternalPool(HostAndPort address, ConnectionInfo connectionInfo, JedisPoolConfig config) {
    //this.poolName = poolName;
    this.address = address;
    this.connectionInfo = connectionInfo;
    JedisFactory factory = new JedisFactory(address.getHost(), address.getPort(), connectionInfo.getTimeout(),
        connectionInfo.getPassword(), connectionInfo.getDatabase());

    internalPool = new GenericObjectPool<Jedis>(factory, config);
  }

  /**
   * Return a broken jedis connection back to pool.
   */
  @Override
  public void returnBrokenResource(final Jedis resource) {
    if (resource != null) {
      returnBrokenResourceObject(resource);
    }
  }

  /**
   * Return a available jedis connection back to pool.
   */
  @SuppressWarnings("deprecation")
  @Override
  public void returnResource(final Jedis resource) {
    if (resource != null) {
      resource.resetState();
      returnResourceObject(resource);
    }
  }

  /**
   * getAddress
   *
   * @return address
   */
  public HostAndPort getAddress() {
    return address;
  }

  /**
   * getConnectionInfo
   *
   * @return connectionInfo
   */
  public ConnectionInfo getConnectionInfo() {
    return connectionInfo;
  }
}
