/**
 * File：AdvertFileService.java
 * Package：com.fang.service
 * Author：yaokaibao
 * Date：2017年4月24日 下午6:23:30
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service;

import java.util.List;

import com.fang.entity.vo.AdvertFileVo;
import com.fang.utils.web.PageQuery;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * AdvertFileService
 *
 * @author yaokaibao
 */
public interface AdvertFileService {

  /**
   * 列表数据
   *
   * @param map
   *        map
   * @param pageBounds
   *        pageBounds
   * @return list
   */
  List<AdvertFileVo> queryList(PageQuery map, PageBounds pageBounds);

  /**
   * 查询广告
   *
   * @param id
   *        id
   * @return advert
   */
  AdvertFileVo queryObject(String id);

  /**
   * 保存
   *
   * @param advert
   *        advert
   */
  void save(AdvertFileVo advert);

  /**
   * 修改
   *
   * @param advert
   *        advert
   */
  void update(AdvertFileVo advert);

  /**
   * 修改
   *
   * @param advert
   *        advert
   */
  void zipUpdate(AdvertFileVo advert);

  /**
   * 修改
   *
   * @param advert
   *        advert
   */
  void batchUpdate(AdvertFileVo advert);

  /**
   * 批量删除
   *
   * @param ids
   *        ids
   */
  void deleteBatch(String[] ids);

  /**
   * 判断存在
   *
   * @param advert
   *        advert
   * @return boolean
   */
  boolean exists(AdvertFileVo advert);

  /**
   * 根据id查找
   *
   * @param ids
   *        ids
   * @return 数据
   */
  List<AdvertFileVo> querySelectedlist(String[] ids);

  /**
   * 根据id查找列表数据
   *
   * @param map
   *        map
   * @param pageBounds
   *        pageBounds
   * @return list
   */
  List<AdvertFileVo> queryListByIds(PageQuery map, PageBounds pageBounds);
}
