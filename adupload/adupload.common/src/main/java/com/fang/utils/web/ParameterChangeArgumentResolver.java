/**
 * File：ParameterChangeArgumentResolver.java
 * Package：com.fang.coupon.core
 * Author："wangzhiyuan"
 * Date：2016年10月10日 下午2:27:33
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import com.fang.utils.CodecUtil;
import com.fang.utils.lang.MapUtil;
import com.fang.utils.lang.SqlUtil;
import com.fang.utils.lang.StringUtil;

/**
 * 改变request中的参数名<br/>
 * 1.url解码<br/>
 * 2.sql注入过滤<br/>
 *
 * @author wangzhiyuan
 */
public class ParameterChangeArgumentResolver implements WebArgumentResolver {

  @Override
  public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
    Class<?> parameterType = methodParameter.getParameterType();
    if (parameterType != null) {

      HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
      Map<String, Object> map = new HashMap<String, Object>();
      String ptemp = StringUtil.EMPTY;
      for (Map.Entry<String, String[]> temp : request.getParameterMap().entrySet()) {
        ptemp = temp.getValue()[0];
        // 1.url解码
        ptemp = CodecUtil.urlDecode(ptemp);
        // 2.sql注入过滤
        ptemp = SqlUtil.getSafetySql(ptemp);

        map.put(temp.getKey(), ptemp);
      }
      return MapUtil.mapToObj(map, parameterType);
    }

    return UNRESOLVED;
  }

}
