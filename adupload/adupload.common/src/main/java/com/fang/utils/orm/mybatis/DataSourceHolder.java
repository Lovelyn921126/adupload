package com.fang.utils.orm.mybatis;

import javax.sql.DataSource;

/**
 * DataSourceHolder
 *
 * @author wangzhiyuan
 */
public class DataSourceHolder {

  /**
   * MASTER
   */
  private static final String MASTER = "master";

  /**
   * SLAVE
   */
  private static final String SLAVE = "slave";

  /**
   * dataSource master or slave
   */
  private static final ThreadLocal<String> DATASOURCES = new ThreadLocal<String>();

  /**
   * master local
   */
  private static final ThreadLocal<DataSource> MASTERLOCAL = new ThreadLocal<DataSource>();

  /**
   * master local
   */
  private static final ThreadLocal<DataSource> SLAVELOCAL = new ThreadLocal<DataSource>();

  /**
   * 设置数据源
   *
   * @param dataSourceKey
   *        dataSourceKey
   * @author wangzhiyuan
   */
  private static void setDataSource(String dataSourceKey) {
    DATASOURCES.set(dataSourceKey);
  }

  /**
   * 获取数据源
   *
   * @return 数据源
   * @author wangzhiyuan
   */
  private static String getDataSource() {
    return (String) DATASOURCES.get();
  }

  /**
   * 标志为master
   */
  public static void setMaster() {
    setDataSource(MASTER);
  }

  /**
   * 标志为slave
   */
  public static void setSlave() {
    setDataSource(SLAVE);
  }

  /**
   * 将master放入threadlocal
   *
   * @param master
   *        master
   */
  public static void setMaster(DataSource master) {
    MASTERLOCAL.set(master);
  }

  /**
   * 将slave放入threadlocal
   *
   * @param slave
   *        slave
   */
  public static void setSlave(DataSource slave) {
    SLAVELOCAL.set(slave);
  }

  /**
   * 判断是否是Master
   *
   * @return 返回Master
   */
  public static boolean isMaster() {
    return getDataSource() == MASTER;
  }

  /**
   * 判断是否是Slave
   *
   * @return 返回Slave
   */
  public static boolean isSlave() {
    return getDataSource() == SLAVE;
  }

  /**
   * 清除thread local中的数据源
   *
   * @author wangzhiyuan
   */
  public static void clearDataSource() {
    DATASOURCES.remove();
    MASTERLOCAL.remove();
    SLAVELOCAL.remove();
  }
}
