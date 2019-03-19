package com.fang.utils.orm.mybatis;

/**
 * 事务配置
 *
 * @author wangzhiyuan
 */
public class TransactionConfig {

  /**
   * 方法名称
   */
  private String name;

  /**
   * 事务传播
   */
  private String propagation;

  /**
   * 只读事务
   */
  private boolean readOnly;

  /**
   * 获取方法名称
   *
   * @return 方法名
   */
  public String getName() {
    return name;
  }

  /**
   * 设置方法名称
   *
   * @param name
   *        方法名
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 获取事务传播级别
   *
   * @return 事务传播级别
   */
  public String getPropagation() {
    return propagation;
  }

  /**
   * 设置事务传播级别
   *
   * @param propagation
   *        事务传播级别
   */
  public void setPropagation(String propagation) {
    this.propagation = propagation;
  }

  /**
   * 是否是只读事务
   *
   * @return 只读事务
   */
  public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * 设置只读事务
   *
   * @param readOnly
   *        只读事务
   */
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

}
