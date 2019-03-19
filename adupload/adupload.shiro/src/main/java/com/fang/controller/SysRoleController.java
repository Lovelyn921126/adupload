package com.fang.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fang.annotation.SysLog;
import com.fang.entity.vo.SysRoleVo;
import com.fang.service.SysRoleMenuService;
import com.fang.service.SysRoleService;
import com.fang.service.SysUserRoleService;
import com.fang.utils.IdUtil;
import com.fang.utils.lang.ArrayUtil;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.web.PageQuery;
import com.fang.utils.web.PageUtil;
import com.fang.utils.web.R;
import com.fang.validator.ValidatorUtil;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author wangzhiyuan
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {

  /**
   * 注入sysRoleService
   */
  @Autowired
  private SysRoleService sysRoleService;

  /**
   * 注入sysRoleMenuService
   */
  @Autowired
  private SysRoleMenuService sysRoleMenuService;

  /**
   * sysUserRoleService
   */
  @Autowired
  private SysUserRoleService sysUserRoleService;

  /**
   * 角色列表
   *
   * @param params
   *        params
   * @return list
   */
  @RequestMapping("/list")
  @RequiresPermissions("sys:role:list")
  public R list(@RequestParam Map<String, Object> params) {

    // 查询列表数据
    PageQuery query = new PageQuery(params);

    // 如果不是超级管理员，则只查询自己创建的角色列表
    if (getUserType() != 3) {
      List<SysRoleVo> list = null;
      List<SysRoleVo> listTemp = null;

      // 获取用户所属的角色列表
      List<String> roleIdList = sysUserRoleService.queryRoleIdList(getUserId());
      if (roleIdList.size() > 0) {
        list = new PageList<SysRoleVo>();
        for (String roleId : roleIdList) {
          query.put("roleId", roleId);
          listTemp = sysRoleService.queryList(query, new PageBounds());
          list.addAll(listTemp);
          if (listTemp != null) {
            for (SysRoleVo rolevo : listTemp) {
              query.put("roleId", rolevo.getRoleId());
              list.addAll(sysRoleService.queryList(query, new PageBounds()));
            }
          }
        }
        list = ArrayUtil.distinct(list);
      }
      PageUtil pageUtil = new PageUtil(list, list.size(), query.getLimit(), query.getPage());
      return R.ok().put("page", pageUtil);
    }

    String order = query.getOrderby().length() == 0 ? "order_num.desc" : query.getOrderby();
    PageBounds pageBounds = new PageBounds(query.getPage(), query.getLimit(), Order.formString(order), true);
    PageList<SysRoleVo> list = (PageList<SysRoleVo>) sysRoleService.queryList(query, pageBounds);
    PageUtil pageUtil = new PageUtil(list, list.getPaginator().getTotalCount(), query.getLimit(), query.getPage());

    return R.ok().put("page", pageUtil);
  }

  /**
   * 角色列表
   *
   * @return select
   */
  @RequestMapping("/select")
  @RequiresPermissions("sys:role:select")
  public R select() {
    Map<String, Object> map = new HashMap<>();
    List<SysRoleVo> list = null;
    List<SysRoleVo> listTemp = null;
    // 如果不是超级管理员，则只查询自己所拥有的角色列表
    if (getUserType() != 3) {
      // 获取用户所属的角色列表
      List<String> roleIdList = sysUserRoleService.queryRoleIdList(getUserId());
      if (roleIdList.size() > 0) {
        list = new ArrayList<SysRoleVo>();
        for (String roleId : roleIdList) {
          map.put("roleId", roleId);
          listTemp = sysRoleService.queryList(map, new PageBounds());
          list.addAll(listTemp);
          if (listTemp != null) {
            for (SysRoleVo rolevo : listTemp) {
              map.put("roleId", rolevo.getRoleId());
              list.addAll(sysRoleService.queryList(map, new PageBounds()));
            }
          }
        }
        list = ArrayUtil.distinct(list);
      }

    } else {
      list = sysRoleService.queryList(map, new PageBounds());
    }
    return R.ok().put("list", list);
  }

  /**
   * 角色信息
   *
   * @param roleId
   *        roleId
   * @return info
   */
  @RequestMapping("/info/{roleId}")
  @RequiresPermissions("sys:role:info")
  public R info(@PathVariable("roleId") String roleId) {
    SysRoleVo role = (SysRoleVo) sysRoleService.queryObject(roleId);

    // 查询角色对应的菜单
    List<String> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
    role.setMenuIdList(menuIdList);

    return R.ok().put("role", role);
  }

  /**
   * 保存角色
   *
   * @param role
   *        role
   * @return save
   */
  @SysLog("保存角色")
  @RequestMapping("/save")
  @RequiresPermissions("sys:role:save")
  public R save(@RequestBody SysRoleVo role) {
    ValidatorUtil.validateEntity(role);

    role.setRoleId(IdUtil.newGuid());
    role.setCreateUserId(getUserId());
    // role.setIsDelete(0);
    role.setCreateTime(DateTime.now().toDate());

    sysRoleService.save(role);

    return R.ok();
  }

  /**
   * 修改角色
   *
   * @param role
   *        role
   * @return update
   */
  @SysLog("修改角色")
  @RequestMapping("/update")
  @RequiresPermissions("sys:role:update")
  public R update(@RequestBody SysRoleVo role) {
    ValidatorUtil.validateEntity(role);

    role.setCreateUserId(getUserId());

    role.setUpdateTime(DateTime.now().toDate());

    sysRoleService.update(role);

    return R.ok();
  }

  /**
   * 删除角色
   *
   * @param roleIds
   *        roleIds
   * @return delete
   */
  @SysLog("删除角色")
  @RequestMapping("/delete")
  @RequiresPermissions("sys:role:delete")
  public R delete(@RequestBody String[] roleIds) {
    sysRoleService.deleteBatch(roleIds);

    return R.ok();
  }
}
