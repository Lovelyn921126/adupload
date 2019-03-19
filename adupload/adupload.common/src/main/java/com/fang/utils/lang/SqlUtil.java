/**
 * File：SqlUtil.java
 * Package：com.fang.framework.lang
 * Author：wangzhiyuan
 * Date：2016年7月21日 下午3:59:40
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.lang;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * 安全Sql字符串工具类
 *
 * @author wangzhiyuan
 */
public class SqlUtil {

  /**
   * sqlCommandKeywords <br>
   * "and|exec|execute|insert|select|delete|update|count|chr|mid|master|"
   * +
   * "char|declare|sitename|net user|xp_cmdshell|or|create|drop|table|from|grant|use|group_concat|column_name|"
   * +
   * "information_schema.columns|table_schema|union|where|select|delete|update|orderhaving|having|by|count|*|truncate|like"
   */
  private static final String SQLCOMMANDKEYWORDS = "and|exec|execute|insert|select|delete|update|count|chr|mid|master|"
      + "char|declare|sitename|net user|xp_cmdshell|or|create|drop|table|from|grant|use|group_concat|column_name|"
      + "information_schema.columns|table_schema|union|where|select|delete|update|orderhaving|having|by|count|*|truncate|like";

  /**
   * sqlSeparatKeywords <br>
   * "'|;|--|\'|\"|/*|%|#"
   */
  private static final String SQLSEPARATKEYWORDS = "'|;|--|\'|\"|/*|%|#";

  /**
   * sqlKeywordsArray
   */
  private static final ArrayList<String> SQLKEYWORDSARRAY = new ArrayList<String>();

  /**
   * 静态构造函数
   */
  static {
    String[] sqlSeparatKeywordsArray = SQLSEPARATKEYWORDS.split("\\|");
    for (String sqlSeparatKeyword : sqlSeparatKeywordsArray) {
      SQLKEYWORDSARRAY.add(sqlSeparatKeyword);
    }

    String[] sqlCommandKeywordsArray = SQLCOMMANDKEYWORDS.split("\\|");
    for (String sqlCommandKeyword : sqlCommandKeywordsArray) {
      SQLKEYWORDSARRAY.add(sqlCommandKeyword + " ");
      SQLKEYWORDSARRAY.add(" " + sqlCommandKeyword);
    }
  }

  /**
   * 返回安全字符串
   *
   * @param input
   *        待过滤字符串
   * @return 返回结果
   */
  public static String getSafetySql(String input) {

    if (StringUtil.isBlank(input)) {
      return "";
    }
    for (String sqlKeyword : SQLKEYWORDSARRAY) {
      if (StringUtil.indexOfIgnoreCase(input, sqlKeyword) >= 0) {
        input = StringUtil.removeIgnoreCase(input, sqlKeyword);
      }
    }
    return input;
  }

  /**
   * like 123,456 => '123','456'
   *
   * @param input
   *        input
   * @return 123,456 => '123','456'
   */
  public static String getInSql(String input) {
    String str1 = "''{0}''";
    ArrayList<String> strArray = new ArrayList<String>();
    String[] sarr = input.split(",");
    for (String str : sarr) {
      strArray.add(MessageFormat.format(str1, str));
    }
    return StringUtil.join(strArray, ",");
  }

}
