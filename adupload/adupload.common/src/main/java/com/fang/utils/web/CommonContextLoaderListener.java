/**
 * File：CouponContextLoaderListener.java
 * Package：com.fang.utils.web
 * Author：wangzhiyuan
 * Date：2017年5月11日 下午6:19:05
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.utils.web;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;



/**
 * 重写spring的监听器, 项目启动时加载
 * @author wangzhiyuan
 */
public class CommonContextLoaderListener extends ContextLoaderListener {
  @Override
  public void contextInitialized(ServletContextEvent event) {

    // 动态设置log4j2的配置文件的路径和文件名
    //Configurator.initialize("log4j2", "classpath:conf/log/log4j2-" + ConfigUtil.runState + ".xml");

    super.contextInitialized(event);
  }
}
