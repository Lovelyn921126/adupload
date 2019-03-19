/**
 * File：AdvertLocationServiceImpl.java
 * Package：com.fang.service.impl
 * Author：yaokaibao
 * Date：2017年4月14日 下午4:50:00
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fang.dao.AdvertLocationDao;
import com.fang.entity.vo.AdvertLocationVo;
import com.fang.service.AdvertLocationService;
import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;
import com.fang.utils.web.PageQuery;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * AdvertLocationServiceImpl
 *
 * @author yaokaibao
 */
public class AdvertLocationServiceImpl implements AdvertLocationService {

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  @Override
  public List<AdvertLocationVo> queryList(PageQuery map, PageBounds pageBounds) {
    return dynamicSqlSessionTemplate.getMapper(AdvertLocationDao.class).queryList(map, pageBounds);
  }

  @Override
  public AdvertLocationVo queryObject(String id) {
    return dynamicSqlSessionTemplate.getMapper(AdvertLocationDao.class).queryObject(id);
  }

  @Override
  public void save(AdvertLocationVo location) {
    dynamicSqlSessionTemplate.getMapper(AdvertLocationDao.class).save(location);
  }

  @Override
  public void update(AdvertLocationVo location) {
    dynamicSqlSessionTemplate.getMapper(AdvertLocationDao.class).update(location);
  }

  @Override
  public void deleteBatch(String[] ids) {
    dynamicSqlSessionTemplate.getMapper(AdvertLocationDao.class).deleteBatch(ids);

  }

  @Override
  public boolean exists(AdvertLocationVo location) {
    return dynamicSqlSessionTemplate.getMapper(AdvertLocationDao.class).exists(location) > 0;
  }

  @Override
  public boolean existsByChannelId(Integer channelId) {
    return dynamicSqlSessionTemplate.getMapper(AdvertLocationDao.class).existsByChannelId(channelId) > 0;
  }

  @Override
  public List<AdvertLocationVo> select(String id) {
    return dynamicSqlSessionTemplate.getMapper(AdvertLocationDao.class).select(id);
  }

  @Override
  public AdvertLocationVo queryByName(String name) {
    return dynamicSqlSessionTemplate.getMapper(AdvertLocationDao.class).queryByName(name);
  }

}
