/**
 * File：SysCityServiceImpl.java
 * Package：com.fang.service.impl
 * Author：wangzhiyuan
 * Date：2017年4月11日 下午2:28:26
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fang.dao.SysCityDao;
import com.fang.entity.SysCity;
import com.fang.service.SysCityService;
import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 系统城市
 *
 * @author wangzhiyuan
 */
public class SysCityServiceImpl implements SysCityService {

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  @Override
  public List<SysCity> queryList(HashMap<String, Object> map, PageBounds pageBounds) {
    return dynamicSqlSessionTemplate.getMapper(SysCityDao.class).queryList(map, pageBounds);
  }

  @Override
  public List<SysCity> select() {
    return dynamicSqlSessionTemplate.getMapper(SysCityDao.class).select();
  }

  @Override
  public List<SysCity> queryUserList(String userId) {
    return dynamicSqlSessionTemplate.getMapper(SysCityDao.class).queryUserList(userId);
  }

  @Override
  public void deleteBatch(String[] cityIds) {
    dynamicSqlSessionTemplate.getMapper(SysCityDao.class).deleteBatch(cityIds);

  }

  @Override
  public SysCity queryObject(String id) {
    return dynamicSqlSessionTemplate.getMapper(SysCityDao.class).queryObject(id);
  }

  @Override
  public void save(SysCity city) {
    dynamicSqlSessionTemplate.getMapper(SysCityDao.class).save(city);

  }

  @Override
  public void update(SysCity city) {
    dynamicSqlSessionTemplate.getMapper(SysCityDao.class).update(city);
  }

  @Override
  public int getAllCityCount() {
    return dynamicSqlSessionTemplate.getMapper(SysCityDao.class).getAllCityCount();
  }

  @Override
  public SysCity queryObjectByName(String name) {
    return dynamicSqlSessionTemplate.getMapper(SysCityDao.class).queryObjectByName(name);
  }
}
