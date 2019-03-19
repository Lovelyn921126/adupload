/**
 * File：MySqlUtil.java
 * Package：com.fang.util
 * Author：yaokaibao
 * Date：2017年4月21日 下午6:25:39
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.util;

import java.sql.Connection;
import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

/**
 * MySqlUtil
 *
 * @author yaokaibao
 */
public class MySqlUtil {

  /**
   * LOCK
   */
  private static final Object LOCK = new Object();

  /**
   * 单例
   */
  private static MySqlUtil dbcp;

  /**
   * static
   */
  static {
    try {
      dbcp = new MySqlUtil();
    } catch (SQLException e) {
      dbcp = null;
    }
  }

  /**
   * dataSourceForWrite
   */
  private DruidDataSource dataSource;

  /**
   * containerForWrite
   */
  private ThreadLocal<Connection> container;

  /**
   * 构造
   *
   * @throws SQLException
   *         SQLException
   */
  private MySqlUtil() throws SQLException {
    super();
    initDataSource();
    container = new ThreadLocal<Connection>();
  }

  /**
   * initDataSource
   *
   * @throws SQLException
   *         SQLException
   */
  private void initDataSource() throws SQLException {
    this.dataSource = new DruidDataSource();
    dataSource.setUrl("jdbc:mysql:///soufunad_test");
    dataSource.setUsername("root");
    dataSource.setPassword("123456");
    dataSource.setMaxActive(200);
    dataSource.setMinIdle(5);
    // dataSourceForWrite.setMaxIdle(200);
    dataSource.setInitialSize(10);
    dataSource.setMaxWait(90000);
    dataSource.setPoolPreparedStatements(false);
    dataSource.setMaxPoolPreparedStatementPerConnectionSize(0);
    dataSource.setTimeBetweenEvictionRunsMillis(150);
    dataSource.setValidationQuery("SELECT 1");
    dataSource.setTestOnBorrow(false);
    dataSource.setTestOnReturn(false);
    dataSource.setTestWhileIdle(true);
    dataSource.setMinEvictableIdleTimeMillis(25200000L);
    dataSource.setRemoveAbandoned(true);
    dataSource.setRemoveAbandonedTimeout(900);
    dataSource.setLogAbandoned(true);
    // dataSource.setConnectionProperties("druid.stat.mergeSql=true");
    // dataSource.setFilters("mergeStat");
    // dataSource.setProxyFilters(filters);
  }

  /**
   * getConnection
   *
   * @return Connection
   * @throws Exception
   *         Exception
   */
  public Connection getConnection() throws Exception {
    Connection conn = null;

    try {
      Class.forName("com.mysql.jdbc.Driver");
      conn = dataSource.getConnection();
      container.set(conn);
    } catch (Exception e) {
      throw new Exception("获取数据库连接失败！", e);
    }
    return conn;
  }

  /**
   * getCurrentConnection
   *
   * @return Connection
   * @throws Exception
   *         Exception
   */
  public Connection getCurrentConnection() throws Exception {
    // 计数
    // System.out.println(this.index++);

    Connection conn = null;
    try {
      conn = container.get();
      if (conn == null || conn.isClosed()) {
        synchronized (LOCK) {
          conn = container.get();
          if (conn == null || conn.isClosed()) {
            conn = getConnection();
          }
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return conn;
  }

  /**
   * close
   *
   * @throws Exception
   *         Exception
   */
  public void close() throws Exception {
    try {
      Connection conn_w = container.get();
      if (conn_w != null) {
        conn_w.close();
      }
    } catch (SQLException e) {
      throw new Exception("连接关闭失败", e);
    } finally {
      try {
        container.remove(); // 从当前线程移除连接切记
      } catch (Exception e2) {
        throw new Exception("连接池关闭失败！", e2);
      }
    }
  }

  /**
   * 获取DBCP的单例
   *
   * @return dbcp
   */
  public static MySqlUtil getInstance() {
    return dbcp;
  }

  /**
   * recycleConn
   *
   * @param conn
   *        conn
   * @throws Exception
   *         Exception
   */
  public void recycleConn(Connection conn) throws Exception {
    try {
      if (!(conn == null || conn.isClosed())) {
        ((DruidPooledConnection) conn).recycle();
      }
    } catch (SQLException e) {
      throw e;
    }
  }

}
