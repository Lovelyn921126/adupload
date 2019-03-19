package com.fang.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fang.entity.vo.OnlineUser;
import com.fang.service.SysOnlineUserService;
import com.fang.utils.web.R;

/**
 * 在线人数
 *
 * @author liyanan
 *
 */
@RestController
@RequestMapping("/sys/online")
public class SysOnlineUserController {

  /**
   * onlineuserservice
   */
  @Autowired
  private SysOnlineUserService sysOnlineUserService;

  /**
   * 所有菜单列表
   *
   * @return list
   */
  @RequestMapping("/list")
  @RequiresPermissions("sys:online:list")
  public R list() {
    // 查询列表数据
    /*
     * PageQuery query = new PageQuery(params);
     * String order = query.getOrderby().length() == 0 ? "order_num.desc" : query.getOrderby();
     */
    // PageBounds pageBounds = new PageBounds(query.getPage(), query.getLimit(),
    // Order.formString(order), true);
    List<OnlineUser> list = sysOnlineUserService.queryAll();
    /*
     * PageUtil pageUtil = new PageUtil(list, list.getPaginator().getTotalCount(), query.getLimit(),
     * query.getPage());
     */

    return R.ok().put("list", list);
  }

}
