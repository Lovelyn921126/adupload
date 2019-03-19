/**
 * File：AdvertFileDao.java
 * Package：com.fang.dao
 * Author：yaokaibao
 * Date：2017年4月21日 下午8:35:07
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.dao;

import java.util.List;

import com.fang.pojoformysql.AdvertFile;

/**
 * AdvertFileDao
 *
 * @author yaokaibao
 */
public interface AdvertFileDao {

  /**
   * add
   *
   * @param file
   *        file
   * @return int
   */
  int add(AdvertFile file);

  /**
   * addBatch
   *
   * @param files
   *        files
   * @return int
   */
  int addBatch(List<AdvertFile> files);

  /**
   * delete
   *
   * @param file
   *        file
   * @return int
   */
  int delete(AdvertFile file);
}
