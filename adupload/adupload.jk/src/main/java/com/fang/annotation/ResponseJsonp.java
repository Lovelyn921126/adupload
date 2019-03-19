/**
 * File：ResponseJsonp.java
 * Package：com.fang.annotation
 * Author：wangzhiyuan
 * Date：2017年5月12日 下午1:44:11
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 返回值是jsonp数据
 *
 * @author wangzhiyuan
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseJsonp {

}
