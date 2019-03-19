package com.fang.service;

import java.util.List;
import java.util.Map;

import com.fang.entity.SysLog;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 系统日志
 *
 * @author wangzhiyuan
 */
public interface SysLogService {

  /**
   * 查询实体
   *
   * @param id
   *        id
   * @return SysLog SysLog
   */
  SysLog queryObject(Long id);

  /**
   * 查询列表
   *
   * @param map
   *        map
   * @param pageBounds
   *        pageBounds
   * @return List
   */
  List<SysLog> queryList(Map<String, Object> map, PageBounds pageBounds);

  /**
   * 查询总数
   *
   * @param map
   *        map
   * @return 总数
   */
  int queryTotal(Map<String, Object> map);

  /**
   * 保存
   *
   * @param sysLog
   *        sysLog
   */
  void save(SysLog sysLog);

  /**
   * 更新
   *
   * @param sysLog
   *        sysLog
   */
  void update(SysLog sysLog);

  /**
   * 删除
   *
   * @param id
   *        id
   */
  void delete(Long id);

  /**
   * 批量删除
   *
   * @param ids
   *        ids
   */
  void deleteBatch(Long[] ids);
}
