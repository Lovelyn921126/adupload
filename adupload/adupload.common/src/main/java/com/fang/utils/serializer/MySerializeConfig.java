/**
 * File：MySerializeConfig.java
 * Package：com.fang.utils.web
 * Author：wangzhiyuan
 * Date：2017年4月13日 下午8:28:49
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.utils.serializer;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.fang.utils.lang.time.DateTime;

/**
 * MySerializeConfig
 *
 * @author wangzhiyuan
 */
public class MySerializeConfig extends SerializeConfig {

  /**
   * MySerializeConfig
   *
   * @param dateTimeFormat
   *        dateTimeFormat
   */
  public MySerializeConfig(String dateTimeFormat) {
    super.put(DateTime.class, new SimpleDateTimeFormatSerializer(dateTimeFormat));
  }

}
