/**
 * File：JiankongController.java
 * Package：com.fang.controller
 * Author：wangzhiyuan
 * Date：2017年5月31日 下午4:50:00
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.controller;

import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;
import com.fang.utils.web.R;
import com.fang.utils.web.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 监控
 *
 * @author wangzhiyuan
 */
@RestController
public class JiankongController {

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  /**
   * 监控
   *
   * @param params
   *        params
   * @param request
   *        request
   * @param response
   *        response
   * @return R
   */
  @RequestMapping("/SysUser/JianKong.aspx")
  public R jianKong(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
    String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?> <root><result>{Result}</result><errormessage>{Error}</errormessage></root>";

    String result = "";
    String error = "";
    try {
      String v = toString(params.get("v"));
      String id = toString(params.get("id"));

      if (!v.equals("A6D3C71476545")) {
        result = "101";
        error = "验证码不为A6D3C71476545";
      } else {

        boolean totalityResult = true;
        boolean testAll = false;

        String[] idArray = {"-1"};
        try {
          idArray = id.split(",");
          if (id.equals("null") || "".equals(idArray[0])) {
            testAll = true;
          }
        } catch (Exception e) {
          testAll = true;
        }
        result = "102";
        if (contains(idArray, "1") || testAll) {
          error += "IIS服务器可以访问";
        }
        if (contains(idArray, "2") || testAll) {
          totalityResult = connectTest();
          error += "广告上传数据库" + (totalityResult ? "" : "不") + "可以访问";
        }
        if (totalityResult) {
          error += "  监控总体结果：AduploadIsAllSuccess";
        } else {
          result = "110";
          error += "  监控总体结果：AduploadIsError";
        }
      }

    } catch (Exception ex) {
      result = "";
      error = "";
      result = "120";
      error = "接口异常：" + ex.toString();
    } finally {
      xml = xml.replace("{Result}", result).replace("{Error}", error);
      WebUtil.sendXmlResponse(xml, response);
    }
    return null;
  }

  /**
   * 将接受的参数的值转换为String
   *
   * @param obj
   *        参数的值
   * @return String
   */
  private String toString(Object obj) {
    if (obj == null) {
      return "";
    }
    return String.valueOf(obj);
  }

  /**
   * 判断字符串数组中是否包含指定字符串
   *
   * @param array
   *        字符串数组
   * @param s
   *        指定字符串
   * @return boolean
   */
  private boolean contains(String[] array, String s) {
    for (String str : array) {
      if (str.equals(s)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 检测数据库连接是否正常
   *
   * @return boolean
   */
  private boolean connectTest() {
    boolean isCanConnectioned;
    try {
      dynamicSqlSessionTemplate.getConnection();
      isCanConnectioned = true;
    } catch (Exception ex) {
      isCanConnectioned = false;
    }
    return isCanConnectioned;
  }
}
