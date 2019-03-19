package com.fang.dao;

import java.util.List;

import com.fang.entity.SysRoleMenu;

/**
 * 角色与菜单对应关系
 *
 * @author wangzhiyuan
 */
public interface SysRoleMenuDao extends BaseDao<SysRoleMenu> {

  /**
   * 根据角色ID，获取菜单ID列表
   *
   * @param roleId
   *        roleId
   * @return List<String>
   */
  List<String> queryMenuIdList(String roleId);
}
