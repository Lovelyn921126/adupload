/**
 * File：SysUserCityDao.java
 * Package：com.fang.dao
 * Author：wangzhiyuan
 * Date：2017年4月11日 下午4:52:15
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.dao;

import java.util.List;

import com.fang.entity.SysUserCity;

/**
 * 用户和城市关系
 *
 * @author wangzhiyuan
 */
public interface SysUserCityDao extends BaseDao<SysUserCity> {

  /**
   * 权限内的城市ID列表
   *
   * @param userId
   *        userId
   * @return ID列表
   */
  List<Integer> queryCityIdList(String userId);

}
