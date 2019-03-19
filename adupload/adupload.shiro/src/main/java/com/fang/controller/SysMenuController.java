package com.fang.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fang.annotation.SysLog;
import com.fang.common.ShiroConfig;
import com.fang.entity.SysMenu;
import com.fang.entity.vo.SysMenuVo;
import com.fang.service.SysMenuService;
import com.fang.utils.IdUtil;
import com.fang.utils.enums.ShiroEnums;
import com.fang.utils.exception.CommonException;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.web.PageQuery;
import com.fang.utils.web.PageUtil;
import com.fang.utils.web.R;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统菜单
 *
 * @author wangzhiyuan
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {

  /**
   * 注入sysMenuService
   */
  @Autowired
  private SysMenuService sysMenuService;

  /**
   * REMOVEMENUID
   */
  //private static final String REMOVEMENUID = "53881cfc334f4f8c8015675cfd2be978";

  /**
   * 用户权限内的菜单列表
   *
   * @return user
   */
  @RequestMapping("/user")
  public R user() {
    List<SysMenuVo> menuList = sysMenuService.getUserMenuList(getUserId());
//    List<SysMenuVo> menuList = new ArrayList<SysMenuVo>();
//    for (SysMenuVo vo : menuListOld) {
//      if (!vo.getMenuId().equals(REMOVEMENUID)) {
//        menuList.add(vo);
//      }
//    }
    return R.ok().put("menuList", menuList);
  }

  /**
   * 所有菜单列表
   *
   * @param params
   *        params
   * @return list
   */
  @RequestMapping("/list")
  @RequiresPermissions("sys:menu:list")
  public R list(@RequestParam Map<String, Object> params) {
    // 查询列表数据
    PageQuery query = new PageQuery(params);
    String order = query.getOrderby().length() == 0 ? "order_num.desc" : query.getOrderby();
    PageBounds pageBounds = new PageBounds(query.getPage(), query.getLimit(), Order.formString(order), true);
    PageList<SysMenuVo> list = (PageList<SysMenuVo>) sysMenuService.queryList(query, pageBounds);
    PageUtil pageUtil = new PageUtil(list, list.getPaginator().getTotalCount(), query.getLimit(), query.getPage());

    return R.ok().put("page", pageUtil);
  }

  /**
   * 选择菜单(添加、修改菜单)
   *
   * @return select
   */
  @RequestMapping("/select")
  @RequiresPermissions("sys:menu:select")
  public R select() {
    // 查询列表数据
    List<SysMenuVo> menuList = sysMenuService.queryNotButtonList();

    // 添加顶级菜单
    SysMenuVo root = new SysMenuVo();
    root.setMenuId(ShiroConfig.ROOT_MENU_ID);
    root.setName("一级菜单");
    root.setParentId("-1");
    root.setOpen(true);
    menuList.add(root);

    return R.ok().put("menuList", menuList);
  }

  /**
   * 角色授权菜单
   *
   * @return perms
   */
  @RequestMapping("/perms")
  @RequiresPermissions("sys:menu:perms")
  public R perms() {
    // 查询列表数据
    List<SysMenuVo> menuList = null;

    // 只有超级管理员，才能查看所有管理员列表
    if (getUserType() == 3) {
      menuList = sysMenuService.queryList(new HashMap<String, Object>(), new PageBounds());
    } else {
      menuList = sysMenuService.queryUserList(getUserId());
    }

    return R.ok().put("menuList", menuList);
  }

  /**
   * 菜单信息
   *
   * @param menuId
   *        menuId
   * @return info
   */
  @RequestMapping("/info/{menuId}")
  @RequiresPermissions("sys:menu:info")
  public R info(@PathVariable("menuId") String menuId) {
    SysMenuVo menu = sysMenuService.queryObject(menuId);
    return R.ok().put("menu", menu);
  }

  /**
   * 保存
   *
   * @param menu
   *        menu
   * @return save
   */
  @SysLog("保存菜单")
  @RequestMapping("/save")
  @RequiresPermissions("sys:menu:save")
  public R save(@RequestBody SysMenuVo menu) {
    // 数据校验
    verifyForm(menu);

    menu.setCreateTime(DateTime.now().toDate());
    menu.setCreateUserId(getUserId());
    // menu.setIsDelete(0);
    menu.setMenuId(IdUtil.newGuid());

    sysMenuService.save(menu);

    return R.ok();
  }

  /**
   * 修改
   *
   * @param menu
   *        menu
   * @return update
   */
  @SysLog("修改菜单")
  @RequestMapping("/update")
  @RequiresPermissions("sys:menu:update")
  public R update(@RequestBody SysMenuVo menu) {
    // 数据校验
    verifyForm(menu);

    menu.setUpdateTime(DateTime.now().toDate());

    sysMenuService.update(menu);

    return R.ok();
  }

  /**
   * 删除
   *
   * @param menuIds
   *        menuIds
   * @return delete
   */
  @SysLog("删除菜单")
  @RequestMapping("/delete")
  @RequiresPermissions("sys:menu:delete")
  public R delete(@RequestBody String[] menuIds) {
    /*
     * for (String menuId : menuIds) {
     * if (menuId.longValue() <= 30) {
     * return R.error("系统菜单，不能删除");
     * }
     * }
     */
    sysMenuService.deleteBatch(menuIds);

    return R.ok();
  }

  /**
   * 验证参数是否正确
   *
   * @param menu
   *        menu
   */
  private void verifyForm(SysMenu menu) {
    if (StringUtils.isBlank(menu.getName())) {
      throw new CommonException("菜单名称不能为空");
    }

    if (menu.getParentId() == null) {
      throw new CommonException("上级菜单不能为空");
    }

    // 菜单
    if (menu.getType() == ShiroEnums.MENU.getCode()) {
      if (StringUtils.isBlank(menu.getUrl())) {
        throw new CommonException("菜单URL不能为空");
      }
    }

    // 上级菜单类型
    int parentType = ShiroEnums.CATALOG.getCode();
    if (!menu.getParentId().equals(ShiroConfig.ROOT_MENU_ID)) {
      SysMenuVo parentMenu = sysMenuService.queryObject(menu.getParentId());
      parentType = parentMenu.getType();
    }

    // 目录、菜单
    if (menu.getType() == ShiroEnums.CATALOG.getCode() || menu.getType() == ShiroEnums.MENU.getCode()) {
      if (parentType != ShiroEnums.CATALOG.getCode()) {
        throw new CommonException("上级菜单只能为目录类型");
      }
      return;
    }

    // 按钮
    if (menu.getType() == ShiroEnums.BUTTON.getCode()) {
      if (parentType != ShiroEnums.MENU.getCode()) {
        throw new CommonException("上级菜单只能为菜单类型");
      }
      return;
    }
  }
}
