/**
 * File：SysGroupServiceImpl.java
 * Package：com.fang.service.impl
 * Author：wangzhiyuan
 * Date：2017年4月10日 下午4:18:05
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.fang.dao.SysGroupDao;
import com.fang.entity.SysGroup;
import com.fang.entity.vo.SysGroupVo;
import com.fang.service.SysGroupService;
import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 系统集团
 *
 * @author wangzhiyuan
 */
public class SysGroupServiceImpl implements SysGroupService {

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  @Override
  public List<SysGroup> queryList(Map<String, Object> map, PageBounds pageBounds) {
    return dynamicSqlSessionTemplate.getMapper(SysGroupDao.class).queryList(map, pageBounds);
  }

  @Override
  public SysGroup queryObject(String groupId) {
    return dynamicSqlSessionTemplate.getMapper(SysGroupDao.class).queryObject(groupId);
  }

  @Override
  public void save(SysGroupVo groupVo) {
     dynamicSqlSessionTemplate.getMapper(SysGroupDao.class).save(groupVo);
  }

  @Override
  public void update(SysGroupVo groupVo) {
    dynamicSqlSessionTemplate.getMapper(SysGroupDao.class).update(groupVo);

  }

  @Override
  public void deleteBatch(String[] menuIds) {
    dynamicSqlSessionTemplate.getMapper(SysGroupDao.class).deleteBatch(menuIds);

  }

}
