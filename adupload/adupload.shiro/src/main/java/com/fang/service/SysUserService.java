/**
 * File：SysUserService.java
 * Package：com.fang.shiro.service
 * Author：wangzhiyuan
 * Date：2017年3月29日 下午1:47:32
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service;

import java.util.List;
import java.util.Map;

import com.fang.entity.SysUser;
import com.fang.entity.vo.SysUserVo;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 系统用户
 *
 * @author wangzhiyuan
 */
public interface SysUserService {

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
   *        userId
   * @return List List
   */
  List<String> queryAllMenuId(String userId);

  /**
   * 根据用户名，查询系统用户
   *
   * @param username
   *        username
   * @return SysUser
   */
  SysUser queryByUserName(String username);

  /**
   * 根据用户ID，查询用户
   *
   * @param userId
   *        userId
   * @return SysUser
   */
  SysUserVo queryObject(String userId);

  /**
   * 查询用户列表
   *
   * @param map
   *        map
   * @param pageBounds
   *        pageBounds
   * @return List
   */
  List<SysUserVo> queryList(Map<String, Object> map, PageBounds pageBounds);

  /**
   * 查询总数
   *
   * @param map
   *        map
   * @return 总数
   */
  int queryTotal(Map<String, Object> map);

  /**
   * 保存用户
   *
   * @param user
   *        user
   */
  void save(SysUserVo user);

  /**
   * 修改用户
   *
   * @param user
   *        user
   */
  void update(SysUserVo user);

  /**
   * 删除用户
   *
   * @param userIds
   *        userIds
   */
  void deleteBatch(String[] userIds);

  /**
   * 修改密码
   *
   * @param userId
   *        用户ID
   * @param password
   *        原密码
   * @param newPassword
   *        新密码
   * @return 执行结果
   */
  int updatePassword(String userId, String password, String newPassword);

  /**
   * 判断用户是否是超级用户
   *
   * @param userId
   *        用户ID
   * @return true false
   */
  boolean querySuperOrNot(String userId);

  /**
   * 更新用户的创建者
   *
   * @param user
   *        user
   */
  void changecreateuser(SysUserVo user);
}
