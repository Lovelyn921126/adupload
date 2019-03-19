package com.fang.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fang.dao.SysRoleMenuDao;
import com.fang.entity.SysRoleMenu;
import com.fang.service.SysRoleMenuService;
import com.fang.utils.IdUtil;
import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;

/**
 * 角色与菜单对应关系
 *
 * @author wangzhiyuan
 */
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  @Override
  @Transactional
  public void saveOrUpdate(String roleId, List<String> menuIdList) {
    if (menuIdList.size() == 0) {
      return;
    }
    // 先删除角色与菜单关系
    dynamicSqlSessionTemplate.getMapper(SysRoleMenuDao.class).delete(roleId);

    // 保存角色与菜单关系
    // Map<String, Object> map = new HashMap<>();
    // map.put("id", IdUtil.newGuid());
    // map.put("roleId", roleId);
    // map.put("menuIdList", menuIdList);

    List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
    for (String menuId : menuIdList) {
      SysRoleMenu sysRoleMenu = new SysRoleMenu();
      sysRoleMenu.setId(IdUtil.newGuid());
      sysRoleMenu.setRoleId(roleId);
      sysRoleMenu.setMenuId(menuId);
      list.add(sysRoleMenu);
    }

    dynamicSqlSessionTemplate.getMapper(SysRoleMenuDao.class).save(list);
  }

  @Override
  public List<String> queryMenuIdList(String roleId) {
    return dynamicSqlSessionTemplate.getMapper(SysRoleMenuDao.class).queryMenuIdList(roleId);
  }

}
