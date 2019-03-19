/**
 * File：SysCityDao.java
 * Package：com.fang.dao
 * Author：wangzhiyuan
 * Date：2017年4月11日 下午2:29:55
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.dao;

import java.util.List;

import com.fang.entity.SysCity;

/**
 * 系统城市
 *
 * @author wangzhiyuan
 */
public interface SysCityDao extends BaseDao<SysCity> {

  /**
   * 用户权限内的城市
   *
   * @param userId
   *        userId
   * @return List
   */
  List<SysCity> queryUserList(String userId);

  /**
   * 查询全部可用城市列表
   *
   * @return 城市列表
   */
  List<SysCity> select();

  /**
   * 查找所有城市个数
   *
   * @return int
   *         int
   */
  int getAllCityCount();
  /**
   * 根据名字查询城市
   *
   * @param name
   *        name 城市名称
   *
   * @return SysCity 城市
   */

  SysCity queryObjectByName(String name);

}
