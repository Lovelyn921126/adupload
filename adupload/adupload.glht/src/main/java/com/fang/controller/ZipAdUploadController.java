/**
 * File：ZipAdUploadController.java
 * Package：com.fang.controller
 * Author：wangzhiyuan
 * Date：2017年4月25日 上午10:33:06
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.controller;

import com.alibaba.fastjson.JSONObject;
import com.fang.annotation.SysLog;
import com.fang.entity.vo.AdvertFileVo;
import com.fang.service.AdvertFileService;
import com.fang.utils.CLogger;
import com.fang.utils.CodecUtil;
import com.fang.utils.IdUtil;
import com.fang.utils.JsonXmlObjUtils;
import com.fang.utils.exception.CommonException;
import com.fang.utils.file.FileHandler;
import com.fang.utils.file.FileUpload;
import com.fang.utils.lang.CharsetsUtil;
import com.fang.utils.lang.StringUtil;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.web.HttpClientUtil;
import com.fang.utils.web.R;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * ZIP文件上传
 *
 * @author wangzhiyuan
 */
@RestController
@RequestMapping("/advert/zipAdUpload")
public class ZipAdUploadController extends AbstractController {

  /**
   * advertFileService
   */
  @Autowired
  private AdvertFileService advertFileService;

  /**
   * httpClientUtil
   */
  @Autowired
  private HttpClientUtil httpClientUtil;

  /**
   * 获取文件上传后的地址
   *
   * @param file
   *        file
   * @return 地址
   * @throws Exception
   *         Exception
   */
  @SysLog("zip广告上传")
  @RequestMapping("/save")
  @RequiresPermissions("advert:advert:zipsave")
  public R save(@RequestBody AdvertFileVo file) throws Exception {

    verifyForm(file);

    AdvertFileVo oldFile = advertFileService.queryObject(file.getSourceId());
    if (oldFile != null && oldFile.getIsDelete() == 0) {
      oldFile.setOriAdver(oldFile.getSourceUrl());
      return R.ok().put("file", oldFile);
    }

    file.setCityName(cityFilter(file.getCityName()));

    String[] data = getFileBack(file, 20);
    if (data == null) {
      return R.error("文件上传失败，本后台上传功能受限于其他上传服务器,出现问题请尝试重新上传此文件");
    }

    file.setId(IdUtil.newGuid());
    file.setSourceUrl(data[4]);
    file.setSourceSize(Double.valueOf(data[1]));
    file.setCreateTime(DateTime.now().toDate());
    file.setUploadUserId(getUserId());
    file.setUploadUsername(getUser().getUsername());
    file.setAcquireInfoWay(1);
    file.setSourceWidth(0);
    file.setSourceHeight(0);
    file.setIsDelete(0);

    if (Boolean.TRUE.equals(file.getYunUsed())) {
      file.setSourceUrl(updateUrl(file));
      if (file.getCreatable()) {
        if (!handleIndex(file)) {
          return R.error("转换index失败");
        }
      }
    }

    advertFileService.save(file);

    file.setOriAdver(data[6]);

    return R.ok().put("file", file);

  }

  /**
   * 获取文件上传回传信息
   *
   * @param file
   *        file
   * @param tryTimes
   *        tryTimes
   * @return 文件上传回传信息
   */
  private String[] getFileBack(AdvertFileVo file, int tryTimes) {
    try {
      String result = httpClientUtil.get(MessageFormat.format("{0}?i={1}&city={2}&channel={3}", file.getNetRoad(), file.getSourceId(), file.getCityName(), file.getChannelName()));
      if (StringUtil.isBlank(result)) {
        throw new CommonException("aging");
      }
      String[] re = result.split(",");
      if (re[1] == null || re[4] == null) {
        throw new CommonException("aging");
      }
      return re;
    } catch (Exception e) {
      if (tryTimes != 0) {
        tryTimes--;
        return getFileBack(file, tryTimes);
      }
      CLogger.error("zipGetFileBack-get file info form laolv is fail");
    }
    return null;
  }

  /**
   * 获取文件上传后的地址
   *
   * @param file
   *        file
   * @return 地址
   * @throws Exception
   *         Exception
   */
  @SysLog("zip广告上传-修改")
  @RequestMapping("/update")
  @RequiresPermissions("advert:advert:update")
  public R update(@RequestBody AdvertFileVo file) throws Exception {

    verifyForm(file);

//    AdvertFileVo oldFile = advertFileService.queryObject(file.getSourceId());
//    if (!oldFile.getUploadUserId().equals(getUserId()) && !isHost()) {
//      throw new CommonException("只能修改自己上传的文件");
//    }
    file.setUpdateTime(DateTime.now().toDate());
    file.setCityName(cityFilter(file.getCityName()));

    if (Boolean.TRUE.equals(file.getYunUsed())) {
      file.setSourceUrl(updateUrl(file));
      if (file.getCreatable()) {
        if (!handleIndex(file)) {
          return R.error("转换index失败");
        }
      }
    }

    advertFileService.zipUpdate(file);

    // file.setOriAdver(file.getSourceUrl());

    return R.ok().put("file", file);

  }

  /**
   * 获取文件上传后的地址
   *
   * @param file
   *        file
   * @return 地址
   * @throws Exception
   *         Exception
   */
  @SysLog("zip广告上传-修改-文件变更")
  @RequestMapping("/updateWithFile")
  @RequiresPermissions("advert:advert:update")
  public R updateWithFile(@RequestBody AdvertFileVo file) throws Exception {

    verifyForm(file);

//    AdvertFileVo oldFile = advertFileService.queryObject(file.getSourceId());
//    if (!oldFile.getUploadUserId().equals(getUserId()) && !isHost()) {
//      throw new CommonException("只能修改自己上传的文件");
//    }
    file.setCityName(cityFilter(file.getCityName()));

    String url = MessageFormat.format("{0}?i={1}&city={2}&channel={3}&url={4}", file.getNetRoad(), file.getSourceId(), CodecUtil.urlEncode(file.getCityName()),
        CodecUtil.urlEncode(file.getChannelName()), StringUtil.removeEnd(file.getOriAdver(), "/") + ".zip");
    String result = httpClientUtil.get(url);
    if (StringUtil.isBlank(result)) {
      return null;
    }
    String[] data = result.split(",");
    file.setSourceSize(Double.valueOf(data[1]));
    file.setUpdateTime(DateTime.now().toDate());

    if (Boolean.TRUE.equals(file.getYunUsed())) {
      file.setSourceUrl(updateUrl(file));
      if (file.getCreatable()) {
        if (!handleIndex(file)) {
          return R.error("转换index失败");
        }
      }
    }
    advertFileService.zipUpdate(file);

    // file.setOriAdver(data[6]);

    return R.ok().put("file", file);

  }

  /**
   * handleIndex
   *
   * @param file
   *        file
   * @return boolean
   */
  private boolean handleIndex(AdvertFileVo file) {

    try {
      FileHandler.handle(file, httpClientUtil);
      return true;
    } catch (Exception ex) {
      CLogger.error("转换index失败 - " + ex);
      return false;
    }
  }

  /**
   * uploadImg
   *
   * @param file
   *        file
   * @return R
   */
  @RequestMapping("/uploadImg")
  // @RequiresPermissions("advert:advert:uploadImg")
  public R uploadImg(MultipartFile file) {
    if (file != null && file.getSize() > 0) {
      String pattern = ".+(.JPG|.jpg|.GIF|.gif|.PNG|.png|.SWF|.swf)$";
      String contentType = file.getContentType();
      String name = file.getOriginalFilename();
      // 图片格式判断
      if (!contentType.startsWith("image") || !Pattern.compile(pattern).matcher(name).matches()) {
        return R.error("文件格式不支持！");
      } else {
        try {
          String requestUrl = "http://img360u.fang.com/upload/pic?city=%E5%8c%97%E4%BA%AC";
          String imgUrl = FileUpload.upload(requestUrl, file);
          if (StringUtil.isNotBlank(imgUrl)) {
            return R.ok().put("url", imgUrl);
          } else {
            return R.error("上传失败！");
          }
        } catch (Exception ex) {
          return R.error(ex.getMessage());
        }
      }

    } else {
      return R.error("未选择任何文件！");
    }
  }

  /**
   * verifyNewCode
   *
   * @param params
   *        params
   * @return R
   * @throws Exception
   *         Exception
   */
  @RequestMapping("/verifyNewCode")
  // @RequiresPermissions("advert:advert:verifyNewCode")
  public R verifyNewCode(@RequestBody Map<String, Object> params) throws Exception {
    String urlN;
    String urlS;
    String text;

    urlN = "http://search1s.fang.com/newsearch3";
    urlS = "http://search1.fang.com/newsearch3";

    /*
     * 由于不清楚该调用那个接口，故先尝试调用其中一个。
     */
    try {
      String code = String.valueOf(params.get("newcode"));
      text = verifyNewCode(urlN, code);
      if (StringUtil.isBlank(text)) {
        text = verifyNewCode(urlS, code);
        if (StringUtil.isBlank(text)) {
          return R.ok();
        }
      }
      return R.ok().put("text", text);
    } catch (Exception ex) {
      throw new CommonException("验证失败！");
    }
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

  /**
   * 对360全景广告URL进行可能的参数拼接
   *
   * @param file
   *        file
   * @return 拼接后的URL
   */
  private String updateUrl(AdvertFileVo file) {
    String sourceUrl = file.getSourceUrl();
    if (file.getServiceType() == 2 && Boolean.TRUE.equals(file.getYunUsed())) {
      if (sourceUrl.indexOf("?") > -1) {
        sourceUrl = sourceUrl.substring(0, sourceUrl.indexOf("?"));
      } else {
        sourceUrl += "html/index.html";
      }
      String params = "";
      if (StringUtil.isNotBlank(file.getProjectId())) {
        params = params + (params.length() == 0 ? "?" : "&") + "nc=" + file.getProjectId();
      }
      if (StringUtil.isNotBlank(file.getProjectType())) {
        params = params + (params.length() == 0 ? "?" : "&") + "type=" + file.getProjectType();
      }
      if (StringUtil.isNotBlank(file.getGroup())) {
        params = params + (params.length() == 0 ? "?" : "&") + "channel=" + file.getGroup();
      }
      String p = "";
      p += (file.getPv() ? "1" : "0");
      p += (file.getUp() ? "1" : "0");
      p += (file.getSm() ? "1" : "0");
      if (p.length() > 0) {
        params = params + (params.length() == 0 ? "?" : "&") + "p=" + p;
      }
      sourceUrl += params;
    }
    return sourceUrl;
  }

  /**
   * verifyNewCode
   *
   * @param url
   *        url
   * @param code
   *        code
   * @return String
   * @throws Exception
   *         Exception
   */
  private String verifyNewCode(String url, String code) throws Exception {
    url += "?strNewCode=";
    url += code;
    String res = httpClientUtil.get(url, CharsetsUtil.GBK);

    JSONObject json = JsonXmlObjUtils.json2JsonObject(JsonXmlObjUtils.xml2json(res));
    if (StringUtil.isNoneBlank(json.getString("hits"))) {
      JSONObject hits = JsonXmlObjUtils.json2JsonObject(json.getString("hits"));

      if (StringUtil.isNoneBlank(hits.getString("hit"))) {
        JSONObject hit = JsonXmlObjUtils.json2JsonObject(hits.getString("hit"));

        if (StringUtil.isNoneBlank(hit.getString("title"))) {
          return hit.getString("title");
        }
      }
    }
    return null;
  }
}
