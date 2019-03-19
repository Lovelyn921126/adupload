package com.fang.dao;

import java.util.List;

import com.fang.entity.SysUserRole;

/**
 * 用户与角色对应关系
 *
 * @author wangzhiyuan
 */
public interface SysUserRoleDao extends BaseDao<SysUserRole> {

  /**
   * 根据用户ID，获取角色ID列表
   *
   * @param userId
   *        userId
   * @return List
   */
  List<String> queryRoleIdList(String userId);
}
