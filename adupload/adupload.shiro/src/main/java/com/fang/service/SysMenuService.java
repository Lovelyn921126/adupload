/**
 * File：SysMenuService.java
 * Package：com.fang.shiro.service
 * Author：wangzhiyuan
 * Date：2017年3月29日 下午1:52:46
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service;

import java.util.List;
import java.util.Map;

import com.fang.entity.vo.SysMenuVo;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 菜单管理
 *
 * @author wangzhiyuan
 */
public interface SysMenuService {

  /**
   * 根据父菜单，查询子菜单
   *
   * @param parentId
   *        父菜单ID
   * @param menuIdList
   *        用户菜单ID
   * @return 子菜单列表
   */
  List<SysMenuVo> queryListParentId(String parentId, List<String> menuIdList);

  /**
   * 获取不包含按钮的菜单列表
   *
   * @return 菜单列表
   */
  List<SysMenuVo> queryNotButtonList();

  /**
   * 获取用户菜单列表
   *
   * @param userId
   *        userId
   *
   * @return 菜单列表
   */
  List<SysMenuVo> getUserMenuList(String userId);

  /**
   * 查询菜单
   *
   * @param menuId
   *        menuId
   *
   * @return 菜单对象
   */
  SysMenuVo queryObject(String menuId);

  /**
   * 查询全部菜单列表
   *
   * @param map
   *        map
   * @param pageBounds
   *        pageBounds
   * @return 菜单列表
   */
  List<SysMenuVo> queryList(Map<String, Object> map, PageBounds pageBounds);

  /**
   * 查询总数
   *
   * @param map
   *        map
   *
   * @return 总数
   */
  int queryTotal(Map<String, Object> map);

  /**
   * 保存菜单
   *
   * @param menu
   *        menu
   */
  void save(SysMenuVo menu);

  /**
   * 修改
   *
   * @param menu
   *        menu
   */
  void update(SysMenuVo menu);

  /**
   * 删除
   *
   * @param menuIds
   *        menuIds
   */
  void deleteBatch(String[] menuIds);

  /**
   * 查询用户的权限列表
   *
   * @param userId
   *        userId
   *
   * @return 菜单列表
   */
  List<SysMenuVo> queryUserList(String userId);
}
