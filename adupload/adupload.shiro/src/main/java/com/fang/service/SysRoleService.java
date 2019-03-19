package com.fang.service;

import java.util.List;
import java.util.Map;

import com.fang.entity.SysRole;
import com.fang.entity.vo.SysRoleVo;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 角色
 *
 * @author wangzhiyuan
 */
public interface SysRoleService {

  /**
   * queryObject
   *
   * @param roleId
   *        roleId
   * @return SysRole
   */
  SysRole queryObject(String roleId);

  /**
   * 返回页面查找
   *
   * @param map
   *        map
   * @param pageBounds
   *        pageBounds
   * @return 数据
   */
  List<SysRoleVo> queryList(Map<String, Object> map, PageBounds pageBounds);

  /**
   * 查询总数
   *
   * @param map
   *        map
   * @return queryTotal
   */
  int queryTotal(Map<String, Object> map);

  /**
   * save
   *
   * @param role
   *        role
   */
  void save(SysRoleVo role);

  /**
   * update
   *
   * @param role
   *        role
   */
  void update(SysRoleVo role);

  /**
   * deleteBatch
   *
   * @param roleIds
   *        roleIds
   */
  void deleteBatch(String[] roleIds);

  /**
   * 查询用户创建的角色ID列表
   *
   * @param createUserId
   *        createUserId
   * @return List
   */
  List<String> queryRoleIdList(String createUserId);
}
