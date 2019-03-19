/**
 * File：CommonController.java
 * Package：com.fang.controller
 * Author：wangzhiyuan
 * Date：2017年4月19日 下午7:08:34
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fang.utils.IdUtil;
import com.fang.utils.web.R;

/**
 * 公共控制器
 *
 * @author wangzhiyuan
 */
@RestController
@RequestMapping("/common")
public class CommonController extends AbstractController {

  /**
   * 获得新的guid
   *
   * @return R
   */
  @RequestMapping("getNewGuid")
  public R newGuid() {
    return R.ok().put("guid", IdUtil.newGuid());
  }

}
