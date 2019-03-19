package com.fang.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fang.dao.SysMenuDao;
import com.fang.entity.vo.SysMenuVo;
import com.fang.service.SysMenuService;
import com.fang.service.SysRoleMenuService;
import com.fang.service.SysUserService;
import com.fang.utils.enums.ShiroEnums;
import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * SysMenuServiceImpl
 *
 * @author wangzhiyuan
 */
public class SysMenuServiceImpl implements SysMenuService {

  /**
   * sysUserService
   */
  @Autowired
  private SysUserService sysUserService;

  /**
   * sysRoleMenuService
   */
  @Autowired
  private SysRoleMenuService sysRoleMenuService;

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  @Override
  public List<SysMenuVo> queryListParentId(String parentId, List<String> menuIdList) {
    List<SysMenuVo> menuList = dynamicSqlSessionTemplate.getMapper(SysMenuDao.class).queryListParentId(parentId);
    if (menuIdList == null) {
      return menuList;
    }

    List<SysMenuVo> userMenuList = new ArrayList<>();
    for (SysMenuVo menu : menuList) {
      if (menuIdList.contains(menu.getMenuId())) {
        userMenuList.add(menu);
      }
    }
    return userMenuList;
  }

  @Override
  public List<SysMenuVo> queryNotButtonList() {
    return dynamicSqlSessionTemplate.getMapper(SysMenuDao.class).queryNotButtonList();
  }

  @Override
  public List<SysMenuVo> getUserMenuList(String userId) {
    // 系统管理员，拥有最高权限
    // 如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
    if (sysUserService.querySuperOrNot(userId)) {
      return getAllMenuList(null);
    }

    // 用户菜单列表
    List<String> menuIdList = sysUserService.queryAllMenuId(userId);
    return getAllMenuList(menuIdList);
  }

  @Override
  public SysMenuVo queryObject(String menuId) {
    return dynamicSqlSessionTemplate.getMapper(SysMenuDao.class).queryObject(menuId);
  }

  @Override
  public List<SysMenuVo> queryList(Map<String, Object> map, PageBounds pageBounds) {
    return dynamicSqlSessionTemplate.getMapper(SysMenuDao.class).queryList(map, pageBounds);
  }

  @Override
  public int queryTotal(Map<String, Object> map) {
    return dynamicSqlSessionTemplate.getMapper(SysMenuDao.class).queryTotal(map);
  }

  @Override
  public void save(SysMenuVo menu) {
    dynamicSqlSessionTemplate.getMapper(SysMenuDao.class).save(menu);
  }

  @Override
  public void update(SysMenuVo menu) {
    dynamicSqlSessionTemplate.getMapper(SysMenuDao.class).update(menu);
  }

  @Override
  @Transactional
  public void deleteBatch(String[] menuIds) {
    dynamicSqlSessionTemplate.getMapper(SysMenuDao.class).deleteBatch(menuIds);
  }

  @Override
  public List<SysMenuVo> queryUserList(String userId) {
    return dynamicSqlSessionTemplate.getMapper(SysMenuDao.class).queryUserList(userId);
  }

  /**
   * 获取所有菜单列表
   *
   * @param menuIdList
   *        菜单ID列表
   * @return 权限内的菜单列表
   */
  private List<SysMenuVo> getAllMenuList(List<String> menuIdList) {
    // 查询根菜单列表
    List<SysMenuVo> menuList = queryListParentId("0", menuIdList);
    // 递归获取子菜单
    menuList = getMenuTreeList(menuList, menuIdList);

    return menuList;
  }

  /**
   * 递归
   *
   * @param menuList
   *        权限内的菜单列表
   * @param menuIdList
   *        菜单ID列表
   * @return 递归取得所有权限内的菜单
   */
  private List<SysMenuVo> getMenuTreeList(List<SysMenuVo> menuList, List<String> menuIdList) {
    List<SysMenuVo> subMenuList = new ArrayList<SysMenuVo>();

    for (SysMenuVo entity : menuList) {
      if (entity.getType() == ShiroEnums.CATALOG.getCode()) { // 目录
        entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
      }
      subMenuList.add(entity);
    }

    return subMenuList;
  }
}
