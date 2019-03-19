package com.fang.dao;

import java.util.List;

import com.fang.entity.vo.SysMenuVo;

/**
 * 菜单管理
 *
 * @author wangzhiyuan
 */
public interface SysMenuDao extends BaseDao<SysMenuVo> {

  /**
   * 根据父菜单，查询子菜单
   *
   * @param parentId
   *        父菜单ID
   * @return List<SysMenuVo>
   */
  List<SysMenuVo> queryListParentId(String parentId);

  /**
   * 获取不包含按钮的菜单列表
   *
   * @return List<SysMenuVo>
   */
  List<SysMenuVo> queryNotButtonList();

  /**
   * 查询用户的权限列表
   *
   * @param userId
   *        userId
   * @return List<SysMenuVo>
   */
  List<SysMenuVo> queryUserList(String userId);
}
