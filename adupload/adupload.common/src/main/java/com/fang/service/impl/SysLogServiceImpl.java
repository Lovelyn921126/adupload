package com.fang.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.fang.dao.SysLogDao;
import com.fang.entity.SysLog;
import com.fang.service.SysLogService;
import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

import java.util.List;
import java.util.Map;

/**
 * 系统日志
 *
 * @author wangzhiyuan
 */
public class SysLogServiceImpl implements SysLogService {

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  @Override
  public SysLog queryObject(Long id) {
    return dynamicSqlSessionTemplate.getMapper(SysLogDao.class).queryObject(id);
  }

  @Override
  public List<SysLog> queryList(Map<String, Object> map, PageBounds pageBounds) {
    return dynamicSqlSessionTemplate.getMapper(SysLogDao.class).queryList(map, pageBounds);
  }

  @Override
  public int queryTotal(Map<String, Object> map) {
    return dynamicSqlSessionTemplate.getMapper(SysLogDao.class).queryTotal(map);
  }

  @Override
  public void save(SysLog sysLog) {
    dynamicSqlSessionTemplate.getMapper(SysLogDao.class).save(sysLog);
  }

  @Override
  public void update(SysLog sysLog) {
    dynamicSqlSessionTemplate.getMapper(SysLogDao.class).update(sysLog);
  }

  @Override
  public void delete(Long id) {
    dynamicSqlSessionTemplate.getMapper(SysLogDao.class).delete(id);
  }

  @Override
  public void deleteBatch(Long[] ids) {
    dynamicSqlSessionTemplate.getMapper(SysLogDao.class).deleteBatch(ids);
  }

}
