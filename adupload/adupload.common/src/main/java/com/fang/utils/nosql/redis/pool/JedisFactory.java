package com.fang.utils.nosql.redis.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;

/**
 * Copy from redis.clients.jedis.JedisFactory, because it is not a public class.
 *
 * @author wangzhiyuan
 */
public class JedisFactory implements PooledObjectFactory<Jedis> {

  /**
   * host
   */
  private final String host;

  /**
   * port
   */
  private final int port;

  /**
   * timeout
   */
  private final int timeout;

  /**
   * password
   */
  private final String password;

  /**
   * database
   */
  private final int database;

  /**
   * clientName
   */
  private final String clientName;

  /**
   * JedisFactory工厂
   * @param host host
   * @param port port
   * @param timeout timeout
   * @param password password
   * @param database database
   */
  public JedisFactory(final String host, final int port, final int timeout, final String password, final int database) {
    this(host, port, timeout, password, database, null);
  }

  /**
   * JedisFactory工厂
   * @param host host
   * @param port port
   * @param timeout timeout
   * @param password password
   * @param database database
   * @param clientName clientName
   */
  public JedisFactory(final String host, final int port, final int timeout, final String password,
      final int database, final String clientName) {
    super();
    this.host = host;
    this.port = port;
    this.timeout = timeout;
    this.password = password;
    this.database = database;
    this.clientName = clientName;
  }

  @Override
  public void activateObject(PooledObject<Jedis> pooledJedis) throws Exception {
    final BinaryJedis jedis = pooledJedis.getObject();
    if (jedis.getDB() != database) {
      jedis.select(database);
    }

  }

  @Override
  public void destroyObject(PooledObject<Jedis> pooledJedis) throws Exception {
    final BinaryJedis jedis = pooledJedis.getObject();
    if (jedis.isConnected()) {
      try {
        try {
          jedis.quit();
        } catch (Exception e) {
        }
        jedis.disconnect();
      } catch (Exception e) {

      }
    }

  }

  @Override
  public PooledObject<Jedis> makeObject() throws Exception {
    final Jedis jedis = new Jedis(this.host, this.port, this.timeout);

    jedis.connect();
    if (null != this.password) {
      jedis.auth(this.password);
    }
    if (database != 0) {
      jedis.select(database);
    }
    if (clientName != null) {
      jedis.clientSetname(clientName);
    }

    return new DefaultPooledObject<Jedis>(jedis);
  }

  @Override
  public void passivateObject(PooledObject<Jedis> pooledJedis) throws Exception {
    // maybe should select db 0? Not sure right now.
  }

  @Override
  public boolean validateObject(PooledObject<Jedis> pooledJedis) {
    final BinaryJedis jedis = pooledJedis.getObject();
    try {
      return jedis.isConnected() && jedis.ping().equals("PONG");
    } catch (final Exception e) {
      return false;
    }
  }
}