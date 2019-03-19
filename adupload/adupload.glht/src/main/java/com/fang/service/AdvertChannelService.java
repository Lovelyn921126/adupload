package com.fang.service;

import java.util.List;
import java.util.Map;

import com.fang.entity.vo.AdvertChannelVo;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 广告频道
 *
 * @author wangbingyuan
 */
public interface AdvertChannelService {

  /**
   * 查询广告频道列表
   *
   * @param map
   *        map
   * @param pageBounds
   *        pageBounds
   * @return List<AdvertChannelVo>
   */
  List<AdvertChannelVo> queryList(Map<String, Object> map, PageBounds pageBounds);

  /**
   * 根据频道名查询
   *
   * @param name
   *        name
   * @return AdvertChannelVo
   */
  AdvertChannelVo getByName(String name);

  /**
   * 新增频道
   *
   * @param channel
   *        channel
   */
  void save(AdvertChannelVo channel);

  /**
   * 根据id查询
   *
   * @param id
   *        id
   * @return AdvertChannelVo
   */
  AdvertChannelVo queryObject(Integer id);

  /**
   * 更新频道
   *
   * @param channel
   *        channel
   *
   */
  void update(AdvertChannelVo channel);

  /**
   * 批量删除频道
   *
   * @param ids
   *        ids
   */
  void deleteBatch(Integer[] ids);

  /**
   * 下拉列表
   *
   * @return List<AdvertChannelVo>
   */
  List<AdvertChannelVo> select();
}
