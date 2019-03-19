package com.fang.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fang.dao.AdvertChannelDao;
import com.fang.entity.vo.AdvertChannelVo;
import com.fang.service.AdvertChannelService;
import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 广告频道管理serviceImpl
 *
 * @author wangbingyuan
 */
public class AdvertChannelServiceImpl implements AdvertChannelService {

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  /**
   * 查询广告频道列表
   */
  @Override
  public List<AdvertChannelVo> queryList(Map<String, Object> map, PageBounds pageBounds) {
    return dynamicSqlSessionTemplate.getMapper(AdvertChannelDao.class).queryList(map, pageBounds);
  }

  /**
   * 全部频道列表
   */
  @Override
  public List<AdvertChannelVo> select() {

    return dynamicSqlSessionTemplate.getMapper(AdvertChannelDao.class).select();
  }

  /**
   * 根据频道名查询
   */
  @Override
  public AdvertChannelVo getByName(String name) {
    return dynamicSqlSessionTemplate.getMapper(AdvertChannelDao.class).getByName(name);
  }

  /**
   * 新增频道
   */
  @Override
  @Transactional
  public void save(AdvertChannelVo channel) {
    dynamicSqlSessionTemplate.getMapper(AdvertChannelDao.class).save(channel);
  }

  /**
   * 根据id查询
   */
  @Override
  public AdvertChannelVo queryObject(Integer id) {
    return dynamicSqlSessionTemplate.getMapper(AdvertChannelDao.class).queryObject(id);
  }

  /**
   * 更新频道
   */
  @Override
  @Transactional
  public void update(AdvertChannelVo channel) {
    dynamicSqlSessionTemplate.getMapper(AdvertChannelDao.class).update(channel);
  }

  /**
   * 批量删除频道
   */
  @Override
  @Transactional
  public void deleteBatch(Integer[] ids) {
    dynamicSqlSessionTemplate.getMapper(AdvertChannelDao.class).deleteBatch(ids);

  }

}
