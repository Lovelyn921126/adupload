/**
 * File：SimpleDateTimeFormatSerializer.java
 * Package：com.fang.framework.lang.time
 * Author：yaokaibao
 * Date：2017年4月10日 下午3:10:37
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.utils.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.fang.utils.lang.time.DateTime;

/**
 * SimpleDateTimeFormatSerializer
 *
 * @author yaokaibao
 */
public class SimpleDateTimeFormatSerializer implements ObjectSerializer {

  /**
   * pattern
   */
  private final String pattern;

  /**
   * SimpleDateTimeFormatSerializer
   *
   * @param pattern
   *        pattern
   */
  public SimpleDateTimeFormatSerializer(String pattern) {
    this.pattern = pattern;
  }

  @Override
  public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
    if (object == null) {
      serializer.out.writeNull();
      return;
    }

    DateTime date = (DateTime) object;
    SimpleDateFormat format = new SimpleDateFormat(pattern, JSON.defaultLocale);
    format.setTimeZone(JSON.defaultTimeZone);

    String text = format.format(date.toDate());
    serializer.write(text);
  }
}
