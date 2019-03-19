package com.fang.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 基础Dao(还需在XML文件里，有对应的SQL语句)
 *
 * @param <T>
 *        T
 *
 * @author wangzhiyuan
 */
public interface BaseDao<T> {

  /**
   * 保存
   *
   * @param t
   *        t
   */
  void save(T t);

  /**
   * 保存
   *
   * @param list
   *        list
   */
  void save(List<T> list);

  /**
   * 保存
   *
   * @param map
   *        map
   */
  void save(Map<String, Object> map);

  /**
   * 批量保存
   *
   * @param list
   *        list
   */
  void saveBatch(List<T> list);

  /**
   * 更新
   *
   * @param t
   *        t
   * @return 更新结果（成功：1,；失败：0）
   */
  int update(T t);

  /**
   * 更新
   *
   * @param map
   *        map
   * @return 执行结果（成功：1,；失败：0）
   */
  int update(Map<String, Object> map);

  /**
   * 删除
   *
   * @param id
   *        id
   * @return 执行结果（成功：1,；失败：0）
   */
  int delete(Object id);

  /**
   * 删除
   *
   * @param map
   *        map
   * @return 执行结果（成功：1,；失败：0）
   */
  int delete(Map<String, Object> map);

  /**
   * 批量删除
   *
   * @param id
   *        id
   * @return 执行结果（成功：1,；失败：0）
   */
  int deleteBatch(Object[] id);

  /**
   * 查询实体
   *
   * @param id
   *        id
   * @return 结果
   */
  T queryObject(Object id);

  /**
   * 查询列表
   *
   * @param map
   *        map
   * @param pageBounds
   *        pageBounds
   * @return list
   */
  List<T> queryList(Map<String, Object> map, PageBounds pageBounds);

  /**
   * 查询列表
   *
   * @param map
   *        map
   * @return list
   */
  List<T> queryList(Map<String, Object> map);

  /**
   * 查询列表
   *
   * @param id
   *        id
   * @return list
   */
  List<T> queryList(Object id);

  /**
   * 查询总数
   *
   * @param map
   *        map
   * @return 结果
   */
  int queryTotal(Map<String, Object> map);

  /**
   * 查询总数
   *
   * @return 结果
   */
  int queryTotal();

  /**
   * 查询是否存在
   *
   * @param obj
   *        obj
   * @return 结果
   */
  int exists(Object obj);
}
