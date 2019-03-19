package com.fang.dao;

import java.util.List;
import java.util.Map;

import com.fang.entity.SysUser;
import com.fang.entity.vo.SysUserVo;

/**
 * 系统用户
 *
 * @author wangzhiyuan
 */
public interface SysUserDao extends BaseDao<SysUserVo> {

  /**
   * 查询用户的所有权限
   *
   * @param userId
   *        用户ID
   * @return List
   */
  List<String> queryAllPerms(String userId);

  /**
   * 查询用户的所有菜单ID
   *
   * @param userId
   *        用户ID
   * @return List
   */
  List<String> queryAllMenuId(String userId);

  /**
   * 根据用户名，查询系统用户
   *
   * @param username
   *        用户名
   * @return SysUser
   */
  SysUser queryByUserName(String username);

  /**
   * 修改密码
   *
   * @param map
   *        map
   * @return 执行结果（1：成功；0：失败）
   */
  int updatePassword(Map<String, Object> map);

  /**
   * 查询用户是否是超级用户
   *
   * @param userId
   *        用户ID
   * @return 返回3就是超级用户，反之不是
   */
  int querySuperOrNot(String userId);

  /**
   * 更新用户的创建者
   *
   * @param user
   *        user
   * @return 执行结果（1：成功；0：失败）
   */
  int changecreateuser(SysUserVo user);
}
