/**
 * File：SysCity.java
 * Package：com.fang.dao
 * Author：yaokaibao
 * Date：2017年5月19日 下午2:43:11
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.dao;

import java.util.List;

import com.fang.pojoformysql.SysCity;

/**
 * SysCity
 *
 * @author yaokaibao
 */
public interface SysCityDao {

  /**
   * add
   *
   * @param city
   *        city
   * @return int
   */
  int add(SysCity city);

  /**
   * addBatch
   *
   * @param citys
   *        citys
   * @return int
   */
  int addBatch(List<SysCity> citys);

}
