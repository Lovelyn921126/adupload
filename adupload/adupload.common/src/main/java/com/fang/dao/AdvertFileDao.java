/**
 * File：AdvertFileDao.java
 * Package：com.fang.dao
 * Author：yaokaibao
 * Date：2017年4月25日 上午9:39:15
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.dao;

import java.util.List;
import java.util.Map;

import com.fang.entity.vo.AdvertFileVo;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * AdvertFileDao
 *
 * @author yaokaibao
 */
public interface AdvertFileDao extends BaseDao<AdvertFileVo> {

  /**
   * zipUpdate
   *
   * @param advert
   *        advert
   * @return int
   */
  int zipUpdate(AdvertFileVo advert);

  /**
   * batchUpdate
   *
   * @param advert
   *        advert
   * @return int
   */
  int batchUpdate(AdvertFileVo advert);

  /**
   * 根据id查找
   *
   * @param ids
   *        ids
   * @return 数据
   */
  List<AdvertFileVo> querySelectedlist(String[] ids);

  /**
   * 查询id列表
   *
   * @param map
   *        map
   * @param pageBounds
   *        pageBounds
   * @return list
   */
  List<String> queryIdList(Map<String, Object> map, PageBounds pageBounds);

  /**
   * 根据id查找
   *
   * @param ids
   *        ids
   * @param pageBounds
   *        pageBounds
   * @return list
   */
  List<AdvertFileVo> queryListByIds(List<String> ids, PageBounds pageBounds);
}
