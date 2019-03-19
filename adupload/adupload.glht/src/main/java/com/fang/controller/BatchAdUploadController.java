/**
 * File：BatchAdUploadController.java
 * Package：com.fang.controller
 * Author：wangzhiyuan
 * Date：2017年4月27日 下午4:03:47
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fang.annotation.SysLog;
import com.fang.entity.vo.AdvertFileVo;
import com.fang.service.AdvertFileService;
import com.fang.utils.IdUtil;
import com.fang.utils.exception.CommonException;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.web.R;

/**
 * 批量上传
 *
 * @author wangzhiyuan
 */
@RestController
@RequestMapping("/advert/batchAdUpload")
public class BatchAdUploadController extends AbstractController {

  /**
   * advertFileService
   */
  @Autowired
  private AdvertFileService advertFileService;

  /**
   * 批量上传保存
   *
   * @param file
   *        file
   * @return 保存
   * @throws Exception
   *         Exception
   */
  @SysLog("图片批量上传")
  @RequestMapping("/save")
  @RequiresPermissions("advert:advert:batchsave")
  public R save(AdvertFileVo file) throws Exception {

    verifyForm(file);

    file.setId(IdUtil.newGuid());
    file.setCreateTime(DateTime.now().toDate());
    file.setUploadUserId(getUserId());
    file.setUploadUsername(getUser().getUsername());
    file.setAcquireInfoWay(1);
    file.setIsDelete(0);

    advertFileService.save(file);

    return R.ok();

  }

  /**
   * 批量上传保存
   *
   * @param file
   *        file
   * @return 保存
   * @throws Exception
   *         Exception
   */
  @SysLog("图片批量上传-修改")
  @RequestMapping("/update")
  @RequiresPermissions("advert:advert:batchsave")
  public R update(AdvertFileVo file) throws Exception {

    verifyForm(file);

//    AdvertFileVo oldFile = advertFileService.queryObject(file.getSourceId());
//    if (!oldFile.getUploadUserId().equals(getUserId()) && !isHost()) {
//      throw new CommonException("只能修改自己上传的文件");
//    }

    file.setUpdateTime(DateTime.now().toDate());

    advertFileService.batchUpdate(file);

    return R.ok();

  }

  /**
   * 验证参数是否正确
   *
   * @param advert
   *        advert
   */
  private void verifyForm(AdvertFileVo advert) {
    if (StringUtils.isBlank(advert.getProjectName())) {
      throw new CommonException("广告项目名称不能为空");
    }

  }

}
