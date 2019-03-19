package com.fang.utils.orm.mybatis;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 *
 * @author wangzhiyuan
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

  /**
   * 自动递增计数器
   */
  private AtomicInteger counter = new AtomicInteger();

  /**
   * master库 dataSource
   */
  private DataSource master;

  /**
   * slaves
   */
  private List<DataSource> slaves;

  @Override
  protected Object determineCurrentLookupKey() {
    // do nothing
    return null;
  }

  @Override
  public void afterPropertiesSet() {
    // do nothing
  }

  /**
   * 根据标识
   * 获取数据源
   *
   */
  @Override
  protected DataSource determineTargetDataSource() {
    DataSource returnDataSource = null;
    if (DataSourceHolder.isMaster()) {
      returnDataSource = master;
    } else if (DataSourceHolder.isSlave()) {
      int count = counter.incrementAndGet();
      if (count > 1000000) {
        counter.set(0);
      }
      int n = slaves.size();
      int index = count % n;
      returnDataSource = slaves.get(index);
    } else {
      returnDataSource = master;
    }

    /*if (returnDataSource instanceof ComboPooledDataSource) {
      ComboPooledDataSource source = (ComboPooledDataSource) returnDataSource;
      String jdbcUrl = source.getJdbcUrl();
      CLogger.debug("jdbcUrl:" + jdbcUrl);
    }*/

    return returnDataSource;
  }

  /**
   * 获取Master
   *
   * @return Master
   */
  public DataSource getMaster() {
    return master;
  }

  /**
   * 设置Master
   *
   * @param master
   *        Master
   */
  public void setMaster(DataSource master) {
    this.master = master;
  }

  /**
   * 获取Slaves
   *
   * @return Slaves
   */
  public List<DataSource> getSlaves() {
    return slaves;
  }

  /**
   * 设置Slaves
   *
   * @param slaves
   *        Slaves
   */
  public void setSlaves(List<DataSource> slaves) {
    this.slaves = slaves;
  }

}
