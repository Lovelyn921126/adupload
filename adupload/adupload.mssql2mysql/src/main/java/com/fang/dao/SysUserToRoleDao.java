/**
 * File：SysUserToRoleDao.java
 * Package：com.fang.dao
 * Author：yaokaibao
 * Date：2017年5月19日 下午5:52:56
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.dao;

import java.util.List;
import java.util.Map;

import com.fang.pojoformysql.SysUser;

/**
 * SysUserToCityDao
 *
 * @author yaokaibao
 */
public interface SysUserToRoleDao {

  /**
   * addBatch
   *
   * @param map
   *        map
   * @return int
   */
  int addBatch(List<Map<String, Object>> map);

  /**
   * delete
   *
   * @param user
   *        user
   * @return int
   */
  int delete(SysUser user);
}
