/**
 * File：AdvertCommentServiceImpl.java
 * Package：com.fang.service.impl
 * Author：wangzhiyuan
 * Date：2017年5月15日 上午11:28:06
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.fang.dao.AdvertCommentDao;
import com.fang.entity.AdvertComment;
import com.fang.service.AdvertCommentService;
import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 说一说
 *
 * @author wangzhiyuan
 */
public class AdvertCommentServiceImpl implements AdvertCommentService {

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  @Override
  public boolean save(AdvertComment comment) {
    return dynamicSqlSessionTemplate.getMapper(AdvertCommentDao.class).saveBack(comment) > 0;
  }

  @Override
  public List<AdvertComment> queryList(Map<String, Object> map, PageBounds pageBounds) {
    return dynamicSqlSessionTemplate.getMapper(AdvertCommentDao.class).queryList(map, pageBounds);
  }

  @Override
  public void deleteBatch(String[] commentIds) {
    dynamicSqlSessionTemplate.getMapper(AdvertCommentDao.class).deleteBatch(commentIds);
  }

}
