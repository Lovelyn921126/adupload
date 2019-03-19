/**
 * File：SysCityService.java
 * Package：com.fang.service
 * Author：wangzhiyuan
 * Date：2017年4月11日 下午2:26:10
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service;

import java.util.HashMap;
import java.util.List;

import com.fang.entity.SysCity;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 系统城市
 *
 * @author wangzhiyuan
 */
public interface SysCityService {

  /**
   * 查询全部城市列表
   *
   * @param hashMap
   *        hashMap
   * @param pageBounds
   *        pageBounds
   * @return 城市列表
   */
  List<SysCity> queryList(HashMap<String, Object> hashMap, PageBounds pageBounds);

  /**
   * 查询用户的权限城市列表
   *
   * @param userId
   *        userId
   * @return 城市列表
   */
  List<SysCity> queryUserList(String userId);

  /**
   * 批量删除
   *
   * @param cityIds
   *        cityIds
   */
  void deleteBatch(String[] cityIds);

  /**
   * 查询全部可用城市列表
   *
   * @return 城市列表
   */
  List<SysCity> select();

  /**
   * 查询指定城市
   *
   * @param id
   *        id
   * @return 指定城市
   */
  SysCity queryObject(String id);

  /**
   * 保存城市
   *
   * @param city
   *        city
   */
  void save(SysCity city);

  /**
   * 更新城市
   *
   * @param city
   *        city
   */
  void update(SysCity city);

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
