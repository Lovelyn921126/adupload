package com.fang.service;

import java.util.List;

/**
 * 角色与菜单对应关系
 *
 * @author wangzhiyaun
 */
public interface SysRoleMenuService {

  /**
   * 保存更新
   *
   * @param roleId
   *        roleId
   * @param menuIdList
   *        menuIdList
   */
  void saveOrUpdate(String roleId, List<String> menuIdList);

  /**
   * 根据角色ID，获取菜单ID列表
   *
   * @param roleId
   *        roleId
   * @return List
   */
  List<String> queryMenuIdList(String roleId);

}
