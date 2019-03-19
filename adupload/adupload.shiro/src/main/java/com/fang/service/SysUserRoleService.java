package com.fang.service;

import java.util.List;

/**
 * 用户与角色对应关系
 *
 * @author wangzhiyuan
 */
public interface SysUserRoleService {

  /**
   * saveOrUpdate
   *
   * @param userId
   *        userId
   * @param roleIdList
   *        roleIdList
   */
  void saveOrUpdate(String userId, List<String> roleIdList);

  /**
   * 根据用户ID，获取角色ID列表
   *
   * @param userId
   *        userId
   * @return List
   */
  List<String> queryRoleIdList(String userId);

  /**
   * delete
   *
   * @param userId
   *        userId
   */
  void delete(String userId);
}
