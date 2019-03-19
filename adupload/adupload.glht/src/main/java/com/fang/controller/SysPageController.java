package com.fang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统页面视图
 *
 * @author wangzhiyuan
 */
@Controller
@RequestMapping("/")
public class SysPageController {

  /**
   * page
   *
   * @param url
   *        url
   * @return page
   */
  @RequestMapping("sys/{url}.html")
  public String page(@PathVariable("url") String url) {
    return "sys/" + url + ".html";
  }

  /**
   * advert
   *
   * @param url
   *        url
   * @return advert
   */
  @RequestMapping("advert/sys/{url}.html")
  public String advertSys(@PathVariable("url") String url) {
    return "advert/" + url + ".html";
  }

  /**
   * advert
   *
   * @param url
   *        url
   * @return advert
   */
  @RequestMapping("advert/{url}.html")
  public String advert(@PathVariable("url") String url) {
    return "advert/" + url + ".html";
  }

  /**
   * common
   *
   * @param url
   *        url
   * @return common
   */
  @RequestMapping("common/{url}.html")
  public String common(@PathVariable("url") String url) {
    return "common/" + url + ".html";
  }
}
