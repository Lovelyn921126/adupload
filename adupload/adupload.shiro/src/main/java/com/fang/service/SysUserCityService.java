/**
 * File：SysUserCityService.java
 * Package：com.fang.service
 * Author：wangzhiyuan
 * Date：2017年4月11日 下午4:26:19
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service;

import java.util.List;

/**
 * 用户和城市关系
 *
 * @author wangzhiyuan
 */
public interface SysUserCityService {

  /**
   * 权限内的城市ID列表
   *
   * @param userId
   *        userId
   * @return ID列表
   */
  List<Integer> queryCityIdList(String userId);

  /**
   * saveOrUpdate
   *
   * @param userId
   *        userId
   * @param cityIdList
   *        cityIdList
   */
  void saveOrUpdate(String userId, List<Integer> cityIdList);

}
