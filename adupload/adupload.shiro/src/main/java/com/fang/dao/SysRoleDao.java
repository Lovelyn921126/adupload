package com.fang.dao;

import java.util.List;

import com.fang.entity.vo.SysRoleVo;

/**
 * 角色管理
 *
 * @author wangzhiyuan
 */
public interface SysRoleDao extends BaseDao<SysRoleVo> {

  /**
   * 查询用户创建的角色ID列表
   *
   * @param createUserId
   *        createUserId
   * @return List<String>
   */
  List<String> queryRoleIdList(String createUserId);
}
