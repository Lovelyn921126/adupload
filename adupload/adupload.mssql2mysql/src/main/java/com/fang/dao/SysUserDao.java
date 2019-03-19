/**
 * File：SysUserDao.java
 * Package：com.fang.dao
 * Author：yaokaibao
 * Date：2017年5月18日 下午5:49:20
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.dao;

import java.util.List;

import com.fang.pojoformysql.SysUser;


/**
 * SysUserDao
 * @author yaokaibao
 */
public interface SysUserDao {

  /**
   * add
   *
   * @param user
   *        user
   * @return int
   */
  int add(SysUser user);

  /**
   * addBatch
   *
   * @param users
   *        users
   * @return int
   */
  int addBatch(List<SysUser> users);

  /**
   * delete
   *
   * @param user
   *        user
   * @return int
   */
  int delete(SysUser user);
}
