package com.fang.utils.nosql.redis.pool;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * Build JedisPool smartly
 *
 * @author wangzhiyuan
 */
public class JedisPoolBuilder {

  /**
   * 池子名前缀
   */
  public static final String DIRECT_POOL_PREFIX = "direct:";

  /**
   * Sentinel机制池子名前缀
   */
  public static final String SENTINEL_POOL_PREFIX = "direct:";

  /**
   * 日志类
   */
  private static Logger logger = LoggerFactory.getLogger(JedisPoolBuilder.class);

  /**
   * 池子名称
   */
  private String poolName;

  /**
   * Sentinel机制HostAndPorts
   */
  private String[] sentinelHostAndPorts;

  /**
   * hostAndPorts
   */
  private String[] hostAndPorts;

  /**
   * masterNames
   */
  private String[] masterNames;

  /**
   * poolSize
   */
  private int poolSize = -1;

  /**
   * database
   */
  private int database = Protocol.DEFAULT_DATABASE;

  /**
   * password
   */
  private String password = ConnectionInfo.DEFAULT_PASSWORD;

  /**
   * timeout
   */
  private int timeout = Protocol.DEFAULT_TIMEOUT;

  /**
   * URL example
   * direct/sentinel:[sentinel or redis address and
   * port]?poolName=x&masterNames=x,x&poolSize=x&database=x&password=x&timeout=x
   * <p/>
   * direct redis: direct://localhost:6379?poolSize=5
   * <p/>
   * sentinel redis: sentinel://sentinel-1:26379,sentinel-2:26379?masterNames=default&pollSize=100
   * <p/>
   * sharding sentinel:
   * sentinel://sentinel-1:26379,sentinel-2:26379?masterNames=shard1,shard2&pollSize=100
   *
   * @param url
   *        url
   * @return 整理后的url
   */
  public JedisPoolBuilder setUrl(String url) {
    URI uri;
    try {
      uri = new URI(url);
    } catch (URISyntaxException ex) {
      logger.error("Incorrect URI for initializing Jedis pool", ex);
      return this;
    }

    final Properties prop = new Properties();
    String query = uri.getQuery();
    if (query != null) {
      try {
        prop.load(new StringReader(query.replace("&", "\n")));
      } catch (IOException ex) {
        logger.error("Failed to load the URI query string as stream", ex);
        return this;
      }
    } else {
      logger.error("No redis pool information set in query part of URI");
      return this;
    }

    String authority = uri.getAuthority();

    if ("direct".equals(uri.getScheme())) {
      this.setShardedDirectHosts(authority);
    } else {
      this.setSentinelHosts(authority);
    }

    if (prop.getProperty("masterName") != null) {
      String masterName = prop.getProperty("masterName");
      setShardedMasterNames(masterName);
    }

    if (prop.getProperty("poolName") != null) {
      setPoolName((prop.getProperty("poolName")));
    }

    if (prop.getProperty("poolSize") != null) {
      setPoolSize(Integer.parseInt(prop.getProperty("poolSize")));
    }
    if (prop.getProperty("database") != null) {
      setDatabase(Integer.parseInt(prop.getProperty("database")));
    }
    if (prop.getProperty("password") != null) {
      setPassword(prop.getProperty("password"));
    }
    if (prop.getProperty("timeout") != null) {
      setTimeout(Integer.parseInt(prop.getProperty("timeout")));
    }

    return this;
  }

  /**
   * setPoolName
   *
   * @param poolName
   *        poolName
   * @return JedisPoolBuilder
   */
  public JedisPoolBuilder setPoolName(String poolName) {
    this.poolName = poolName;
    return this;
  }

  /**
   * setSentinelHosts
   *
   * @param sentinelHostsAndPorts
   *        sentinelHostsAndPorts
   * @return JedisPoolBuilder
   */
  public JedisPoolBuilder setSentinelHosts(String[] sentinelHostsAndPorts) {
    this.sentinelHostAndPorts = sentinelHostsAndPorts;
    return this;
  }

  /**
   * setSentinelHosts
   *
   * @param sentinelHostsAndPorts
   *        sentinelHostsAndPorts
   * @return JedisPoolBuilder
   */
  public JedisPoolBuilder setSentinelHosts(String sentinelHostsAndPorts) {
    if (sentinelHostsAndPorts != null) {
      this.sentinelHostAndPorts = sentinelHostsAndPorts.split(",");
    }
    return this;
  }

  /**
   * setMasterName
   *
   * @param masterName
   *        masterName
   * @return JedisPoolBuilder
   */
  public JedisPoolBuilder setMasterName(String masterName) {
    this.masterNames = new String[] {masterName};
    return this;
  }

  /**
   * setShardedMasterNames
   *
   * @param shardedMasterNames
   *        shardedMasterNames
   * @return JedisPoolBuilder
   */
  public JedisPoolBuilder setShardedMasterNames(String[] shardedMasterNames) {
    this.masterNames = shardedMasterNames;
    return this;
  }

  /**
   * setShardedMasterNames
   *
   * @param shardedMasterNames
   *        shardedMasterNames
   * @return JedisPoolBuilder
   */
  public JedisPoolBuilder setShardedMasterNames(String shardedMasterNames) {
    if (shardedMasterNames != null) {
      this.masterNames = shardedMasterNames.split(",");
    }
    return this;
  }

  /**
   * setDirectHost
   *
   * @param hostAndPort
   *        hostAndPort
   * @return JedisPoolBuilder
   */
  public JedisPoolBuilder setDirectHost(String hostAndPort) {
    this.hostAndPorts = new String[] {hostAndPort};
    return this;
  }

  /**
   * setShardedDirectHosts
   *
   * @param shardedHostAndPorts
   *        shardedHostAndPorts
   * @return JedisPoolBuilder
   */
  public JedisPoolBuilder setShardedDirectHosts(String[] shardedHostAndPorts) {
    this.hostAndPorts = shardedHostAndPorts;
    return this;
  }

  /**
   * setShardedDirectHosts
   *
   * @param shardedHostAndPorts
   *        shardedHostAndPorts
   * @return JedisPoolBuilder
   */
  public JedisPoolBuilder setShardedDirectHosts(String shardedHostAndPorts) {
    if (shardedHostAndPorts != null) {
      this.hostAndPorts = shardedHostAndPorts.split(",");
    }
    return this;
  }

  /**
   * setPoolSize
   *
   * @param poolSize
   *        poolSize
   * @return JedisPoolBuilder
   */
  public JedisPoolBuilder setPoolSize(int poolSize) {
    this.poolSize = poolSize;
    return this;
  }

  /**
   * setDatabase
   *
   * @param database
   *        database
   * @return JedisPoolBuilder
   */
  public JedisPoolBuilder setDatabase(int database) {
    this.database = database;
    return this;
  }

  /**
   * setPassword
   *
   * @param password
   *        password
   * @return JedisPoolBuilder
   */
  public JedisPoolBuilder setPassword(String password) {
    this.password = password;
    return this;
  }

  /**
   * setTimeout
   *
   * @param timeout
   *        timeout
   * @return JedisPoolBuilder
   */
  public JedisPoolBuilder setTimeout(int timeout) {
    this.timeout = timeout;
    return this;
  }

  /**
   * 创建用于非分布式部署的redis池子
   *
   * @return JedisPool
   */
  public JedisPool buildPool() {

    if ((poolName == null) || (poolName.length() == 0)) {
      throw new IllegalArgumentException("poolName is null or empty");
    }

    if (poolSize < 1) {
      throw new IllegalArgumentException("poolSize is less then one");
    }

    JedisPoolConfig config = JedisPool.createPoolConfig(poolSize);
    ConnectionInfo connectionInfo = new ConnectionInfo(database, password, timeout);

    if (isDirect()) {
      return buildDirectPool(hostAndPorts[0], connectionInfo, config);
    } else {
      if ((sentinelHostAndPorts == null) || (sentinelHostAndPorts.length == 0)) {
        throw new IllegalArgumentException("sentinelHostsAndPorts is null or empty");
      }

      if ((masterNames == null) || (masterNames.length == 0)) {
        throw new IllegalArgumentException("masterNames is null or empty");
      }

      return buildSentinelPool(masterNames[0], connectionInfo, config);
    }
  }

  /**
   * 创建用于分布式部署的redis池子
   *
   * @return List<JedisPool>
   */
  public List<JedisPool> buildShardedPools() {

    if ((poolName == null) || (poolName.length() == 0)) {
      throw new IllegalArgumentException("poolName is null or empty");
    }

    if (poolSize < 1) {
      throw new IllegalArgumentException("poolSize is less then one");
    }

    JedisPoolConfig config = JedisPool.createPoolConfig(poolSize);
    ConnectionInfo connectionInfo = new ConnectionInfo(database, password, timeout);

    List<JedisPool> jedisPools = new ArrayList<JedisPool>();
    if (isDirect()) {
      for (String hostAndPort : hostAndPorts) {
        jedisPools.add(buildDirectPool(hostAndPort, connectionInfo, config));
      }
    } else {
      if ((sentinelHostAndPorts == null) || (sentinelHostAndPorts.length == 0)) {
        throw new IllegalArgumentException("sentinelHostsAndPorts is null or empty");
      }

      if ((masterNames == null) || (masterNames.length == 0)) {
        throw new IllegalArgumentException("masterNames is null or empty");
      }

      for (String masterName : masterNames) {
        jedisPools.add(buildSentinelPool(masterName, connectionInfo, config));
      }
    }

    return jedisPools;
  }

  /**
   * 创建池子
   *
   * @param hostAndPort
   *        hostAndPort
   * @param connectionInfo
   *        connectionInfo
   * @param config
   *        config
   * @return JedisPool
   */
  private JedisPool buildDirectPool(String hostAndPort, ConnectionInfo connectionInfo, JedisPoolConfig config) {
    logger.info("Building JedisDirectPool, on redis server {}", hostAndPort);
    String[] hostPort = hostAndPort.split(":");
    HostAndPort masterAddress = new HostAndPort(hostPort[0], Integer.parseInt(hostPort[1]));
    return new JedisDirectPool(poolName, masterAddress, connectionInfo, config);
  }

  /**
   * 创建Sentinel机制池子
   *
   * @param masterName
   *        masterName
   * @param connectionInfo
   *        connectionInfo
   * @param config
   *        config
   * @return JedisPool
   */
  private JedisPool buildSentinelPool(String masterName, ConnectionInfo connectionInfo, JedisPoolConfig config) {
    logger.info("Building JedisSentinelPool, on sentinel sentinelHosts:" + Arrays.toString(sentinelHostAndPorts)
        + ", masterName is " + masterName);

    HostAndPort[] sentinelAddresses = new HostAndPort[sentinelHostAndPorts.length];
    for (int i = 0; i < sentinelHostAndPorts.length; i++) {
      String[] hostPort = sentinelHostAndPorts[i].split(":");
      sentinelAddresses[i] = new HostAndPort(hostPort[0], Integer.parseInt(hostPort[1]));
    }

    return new JedisSentinelPool(poolName, sentinelAddresses, masterName, connectionInfo, config);
  }

  /**
   * 判断池子类型
   *
   * @return true：Direct；false：Sentinel机制
   *
   */
  private boolean isDirect() {
    return ((hostAndPorts != null) && (hostAndPorts.length > 0));
  }
}
