/**
 * File：AdvertFileServiceImpl.java
 * Package：com.fang.service.impl
 * Author：yaokaibao
 * Date：2017年4月25日 上午9:37:08
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service.impl;

import com.fang.dao.AdvertFileDao;
import com.fang.entity.vo.AdvertFileVo;
import com.fang.service.AdvertFileService;
import com.fang.utils.lang.StringUtil;
import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;
import com.fang.utils.web.PageQuery;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * AdvertFileServiceImpl
 *
 * @author yaokaibao
 */
public class AdvertFileServiceImpl implements AdvertFileService {

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  @Override
  public List<AdvertFileVo> queryList(PageQuery map, PageBounds pageBounds) {
    return dynamicSqlSessionTemplate.getMapper(AdvertFileDao.class).queryList(map, pageBounds);
  }

  @Override
  public AdvertFileVo queryObject(String id) {
    return dynamicSqlSessionTemplate.getMapper(AdvertFileDao.class).queryObject(id);
  }

  @Override
  public void save(AdvertFileVo advert) {
    dynamicSqlSessionTemplate.getMapper(AdvertFileDao.class).save(advert);
  }

  @Override
  public void update(AdvertFileVo advert) {
    dynamicSqlSessionTemplate.getMapper(AdvertFileDao.class).update(advert);
  }

  @Override
  public void zipUpdate(AdvertFileVo advert) {
    dynamicSqlSessionTemplate.getMapper(AdvertFileDao.class).zipUpdate(advert);
  }

  @Override
  public void batchUpdate(AdvertFileVo advert) {
    dynamicSqlSessionTemplate.getMapper(AdvertFileDao.class).batchUpdate(advert);

  }

  @Override
  public void deleteBatch(String[] ids) {
    dynamicSqlSessionTemplate.getMapper(AdvertFileDao.class).deleteBatch(ids);
  }

  @Override
  public boolean exists(AdvertFileVo advert) {
    return dynamicSqlSessionTemplate.getMapper(AdvertFileDao.class).exists(advert) > 0;
  }

  @Override
  public List<AdvertFileVo> querySelectedlist(String[] ids) {
    return dynamicSqlSessionTemplate.getMapper(AdvertFileDao.class).querySelectedlist(ids);
  }

  @Override
  public List<AdvertFileVo> queryListByIds(PageQuery map, PageBounds pageBounds) {
    PageBounds pb = new PageBounds(pageBounds.getPage(), pageBounds.getLimit());
    String sort = StringUtil.isBlank(map.getSort()) ? "create_time" : map.getSort();
    String order = StringUtil.isBlank(map.getOrder()) ? "desc" : map.getOrder();
    map.put("orderby", sort + " " + order);
    PageList<String> ids = (PageList<String>) dynamicSqlSessionTemplate.getMapper(AdvertFileDao.class).queryIdList(map, pb);

    List<AdvertFileVo> files = new ArrayList<>();
    if (ids != null && ids.size() > 0) {
      pb = new PageBounds(Order.formString(sort + "." + order));
      files = dynamicSqlSessionTemplate.getMapper(AdvertFileDao.class).queryListByIds(ids, pb);
    }
    PageList<AdvertFileVo> result = new PageList<>(files, ids != null ? ids.getPaginator() : null);

    return result;
  }

}
