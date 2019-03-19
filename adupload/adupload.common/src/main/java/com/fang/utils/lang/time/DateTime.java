/**
 * File：DateTime.java
 * Package：com.fang.framework.lang.time
 * Author："wangzhiyuan"
 * Date：2016年9月28日 下午2:05:40
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.lang.time;

import java.io.Serializable;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间处理工具类
 *
 * @author wangzhiyuan
 */
public class DateTime implements Serializable, Cloneable, Comparable<DateTime> {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 2204457031217727572L;

  /**
   * 转换日期时间格式
   */
  private static final String[] PARSEPATTERNS = {
      "yyyy-MM-dd HH:mm:ss.SSS",
      "yyyy-MM-dd HH:mm:ss.SSS",
      "yyyy-MM-dd HH:mm:ss",
      "yyyy-MM-dd",
      "yyyy/MM/dd HH:mm:ss.SSS",
      "yyyy/MM/dd HH:mm:ss",
      "yyyy/MM/dd",
      "yyyy-M-d H:m:ss.SSS",
      "yyyy-M-d H:m:ss",
      "yyyy-M-d",
      "yyyy/M/d H:m:ss.SSS",
      "yyyy/M/d H:m:ss",
      "yyyy/M/d"
  };

  /**
   * Calendar
   */
  private Calendar c;

  /**
   * 默认时间 1900-01-01 0:00:00
   */
  private static Calendar defaultC = Calendar.getInstance();

  /**
   * 静态
   */
  static {
    defaultC.set(Calendar.YEAR, 1900);
    defaultC.set(Calendar.MONTH, 0);
    defaultC.set(Calendar.DATE, 1);
    defaultC.set(Calendar.HOUR_OF_DAY, 0);
    defaultC.set(Calendar.MINUTE, 0);
    defaultC.set(Calendar.SECOND, 0);
    defaultC.set(Calendar.MILLISECOND, 0);
  }

  // =======================实例化===============================
  /**
   * 获取一个DateTime,此DateTime尚未初始化,表示的时间是1970-1-1 00:00:00.000
   * <p>
   * 要获取当前系统时间,请用DateTime.now();
   * </p>
   */
  public DateTime() {
    c = Calendar.getInstance();
    c.clear();

  }

  /**
   * 设置时间
   * <p>
   * 可以传入一个时间对象，将会被转换为DateTime类型
   * </p>
   *
   * @param date
   *        时间对象
   */
  public DateTime(java.util.Date date) {
    c = Calendar.getInstance();
    c.setTime(date);
  }

  /**
   * 设置时间
   * <p>
   * 可以传入一个日历对象，将会被转换为DateTime类型
   * </p>
   *
   * @param calendar
   *        日历对象
   */
  public DateTime(java.util.Calendar calendar) {
    c = calendar;
  }

  /**
   * 用毫秒来设置时间, 时间的基数是1970-1-1 00:00:00.000;
   * <p>
   * 比如,new DateTime(1000) 则表示1970-1-1 00:00:01.000;<br>
   * 用负数表示基础时间以前的时间
   * </p>
   *
   * @param milliseconds
   *        毫秒
   */
  public DateTime(long milliseconds) {
    c = Calendar.getInstance();
    c.setTimeInMillis(milliseconds);
  }

  // =======================获取日期时间各部分===============================
  /**
   * 获取当前系统时间
   *
   * @return DateTime 当前系统时间
   */
  public static DateTime now() {
    Calendar calendar = Calendar.getInstance();
    return new DateTime(calendar);
  }

  /**
   * 获取此实例代表的小时部分 <br/>
   * 返回 0 ~ 23 之间的正整数
   *
   * @return int 实例代表的小时部分
   */
  public int hour() {
    return c.get(Calendar.HOUR_OF_DAY);
  }

  /**
   * 获取此实例代表的年部分 <br/>
   * 返回 1 ~ 9999 之间的正整数
   *
   * @return int 实例代表的年部分
   */
  public int year() {
    return c.get(Calendar.YEAR);
  }

  /**
   * 获取此实例代表的日期部分<br/>
   * 时分秒毫秒部分是0的实例
   *
   * @return int 此实例代表的日期部分
   */
  public DateTime date() {
    Calendar calendar = (Calendar) c.clone();
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    return new DateTime(calendar);
  }

  /**
   * 获取日期是年中第几周 <br/>
   * 返回1 ~ 52之间的正整数
   *
   * @return int 获取日期是年中第几周
   */
  public int weekOfYear() {
    return c.get(Calendar.WEEK_OF_YEAR);
  }

  /**
   * 返回自 1970-1-1 0:0:0 至此时间的毫秒数
   *
   * @return long 毫秒数
   */
  public long getTimeInMillis() {
    return c.getTimeInMillis();
  }

  /**
   * 返回自 1970-1-1 0:0:0 至此时间的毫秒数
   *
   * @return long 毫秒的一万分之一数
   */
  public long getTimeInTicksMillis() {
    return c.getTimeInMillis() * 10000;
  }

  // =======================转换===============================
  /**
   * 转换成 日历对象
   *
   * @return Calendar对象
   */
  public Calendar toCalendar() {
    return c;
  }

  /**
   * 转换为Date类型
   *
   * @return Date时间
   */
  public Date toDate() {
    return c.getTime();
  }

  /**
   * 转坏成指定格式的时间字符串
   *
   * @param pattern
   *        字符格式
   * @return 日期时间字符串
   */
  public String toString(final String pattern) {
    return DateFormatUtils.format(c.getTime(), pattern);
  }

  /**
   * 转换为 yyyy-MM-dd格式字符串
   *
   * @return yyyy-MM-dd格式字符串
   */
  public String toDateString() {
    return DateFormatUtils.format(c.getTime(), DateFormat.YYYY_MM_DD.getValue());
  }

  /**
   * 转换为 HH:mm:ss格式字符串
   *
   * @return HH:mm:ss格式字符串
   */
  public String toTimeString() {
    return DateFormatUtils.format(c.getTime(), DateFormat.HH_MM_SS.getValue());
  }

  /**
   * 转换为 yyyy-MM-dd HH:mm:ss格式字符串
   *
   * @return yyyy-MM-dd HH:mm:ss 格式字符串
   */
  public String toDateTimeString() {
    return DateFormatUtils.format(c.getTime(), DateFormat.YYYY_MM_DD_HH_MM_SS.getValue());
  }

  /**
   * 转换为 yyyy-MM-dd HH:mm:ss.SSS格式字符串
   *
   * @return yyyy-MM-dd HH:mm:ss.SSS 格式字符串
   */
  public String toDateTimeMillisString() {
    return DateFormatUtils.format(c.getTime(), DateFormat.YYYY_MM_DD_HH_MM_SS_SSS.getValue());
  }

  // =======================解析===============================
  /**
   * <pre>
   * 将一个字符串格式的日期，根据指定的日期格式
   * 将期转换为Date类型的日期对象.
   * Note:这里需要强制指定日期格式，目的是要提高代码的运行效率
   * parsePatterns常用格式
   * yyyyMMddHHmmss -> 20150609134055
   * yyMMdd -> 150609
   * 其中HH表示24小时制 ,hh则表示12小时制，月份为大写M,分钟为小写m
   * yyyy-MM-dd HH:mm:ss -> 2015-06-09 13:40:55
   * yyyy-MM-dd -> 2015-06-09
   * yy-MM-dd -> 15-06-09
   * yyyy/MM/dd -> 2015/06/09
   * yyyy/MM/dd -> 2015/06/09
   *
   * </pre>
   * <p>
   * Parses a string representing a date by trying a variety of different parsers.
   * </p>
   *
   * <p>
   * The parse will try each parse pattern in turn. A parse is only deemed successful if it parses
   * the whole of the input string. If no parse patterns match, a ParseException is thrown.
   * </p>
   * The parser will be lenient toward the parsed date.
   *
   * @param str
   *        the date to parse, not null
   * @return the parsed date
   * @throws IllegalArgumentException
   *         if the date string or pattern array is null
   * @throws ParseException
   *         if none of the date patterns were suitable (or there were none)
   */
  public static DateTime parse(final String str) throws ParseException {
    return parse(str, PARSEPATTERNS);
  }

  /**
   * <pre>
   * 将一个字符串格式的日期，根据指定的日期格式
   * 将期转换为Date类型的日期对象.
   * Note:这里需要强制指定日期格式，目的是要提高代码的运行效率
   * parsePatterns常用格式
   * yyyyMMddHHmmss -> 20150609134055
   * yyMMdd -> 150609
   * 其中HH表示24小时制 ,hh则表示12小时制，月份为大写M,分钟为小写m
   * yyyy-MM-dd HH:mm:ss -> 2015-06-09 13:40:55
   * yyyy-MM-dd -> 2015-06-09
   * yy-MM-dd -> 15-06-09
   * yyyy/MM/dd -> 2015/06/09
   * yyyy/MM/dd -> 2015/06/09
   *
   * </pre>
   * <p>
   * Parses a string representing a date by trying a variety of different parsers.
   * </p>
   *
   * <p>
   * The parse will try each parse pattern in turn. A parse is only deemed successful if it parses
   * the whole of the input string. If no parse patterns match, a ParseException is thrown.
   * </p>
   * The parser will be lenient toward the parsed date.
   *
   * @param str
   *        the date to parse, not null
   * @param parsePatterns
   *        the date format patterns to use, see SimpleDateFormat, not null
   * @return the parsed date
   * @throws IllegalArgumentException
   *         if the date string or pattern array is null
   * @throws ParseException
   *         if none of the date patterns were suitable (or there were none)
   */
  public static DateTime parse(final String str, final String... parsePatterns) throws ParseException {

    if (str == null || parsePatterns == null) {
      throw new IllegalArgumentException("Date and Patterns must not be null");
    }

    SimpleDateFormat parser = new SimpleDateFormat();

    final ParsePosition pos = new ParsePosition(0);
    for (final String parsePattern : parsePatterns) {

      String pattern = parsePattern;

      // LANG-530 - need to make sure 'ZZ' output doesn't get passed to SimpleDateFormat
      if (parsePattern.endsWith("ZZ")) {
        pattern = pattern.substring(0, pattern.length() - 1);
      }

      parser.applyPattern(pattern);
      pos.setIndex(0);

      String str2 = str;
      // LANG-530 - need to make sure 'ZZ' output doesn't hit SimpleDateFormat as it will
      // ParseException
      if (parsePattern.endsWith("ZZ")) {
        str2 = str.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2");
      }

      final Date date = parser.parse(str2, pos);
      if (date != null) {
        return new DateTime(date);
      }
    }
    throw new ParseException("Unable to parse the date: " + str, -1);
  }

  /**
   * 转换时间<br/>
   * 如果转换失败，则输出默认时间
   *
   * @param str
   *        str
   * @param defaultDate
   *        defaultDate
   * @param parsePatterns
   *        parsePatterns
   * @return DateTime
   * @throws ParseException
   *         ParseException
   */
  public static DateTime parse(final String str, String defaultDate, final String... parsePatterns) throws ParseException {
    try {
      return parse(str, parsePatterns);
    } catch (Exception e) {
      try {
        return parse(defaultDate, parsePatterns);
      } catch (Exception e2) {
        return defaultDate(null);
      }
    }
  }

  /**
   * 转换时间<br/>
   * 如果转换失败，则输出默认时间
   *
   * @param str
   *        str
   * @param defaultDate
   *        defaultDate
   * @return DateTime
   * @throws ParseException
   *         ParseException
   */
  public static DateTime parse(final String str, String defaultDate) throws ParseException {
    try {
      return parse(str);
    } catch (Exception e) {
      try {
        return parse(defaultDate);
      } catch (Exception e2) {
        return defaultDate(null);
      }
    }
  }

  /**
   * 转换时间<br/>
   * 如果转换失败，则输出默认时间
   *
   * @param str
   *        str
   * @return DateTime
   * @throws ParseException
   *         ParseException
   */
  public static DateTime parseOrDefaultDate(final String str) throws ParseException {
    try {
      return parse(str);
    } catch (Exception e) {
      return defaultDate(null);
    }
  }

  // =======================运算===============================
  /**
   * 将此 DateTime 与指定 Object 比较，返回判断结果。
   * <p>
   * 此方法等效于：compareTo(when)<br>
   * 当 when > DateTime，返回:大于0的整数<br>
   * 当 when = DateTime，返回:0<br>
   * 当 when < DateTime，返回:小于0的整数
   * </p>
   *
   * @param when
   *        要比较的 Object
   * @return 当 when > DateTime，返回:小于0的整数<br>
   *         当 when = DateTime，返回:0<br>
   *         当 when < DateTime，返回:大于0的整数
   */
  public int compareTo(DateTime when) {
    return c.compareTo(((DateTime) when).c);
  }

  /**
   * 创建并返回此对象的一个副本
   *
   * @return 日期时间对象
   */
  @Override
  public Object clone() {
    return new DateTime((Calendar) c.clone());
  }

  /**
   * 返回该此日历的哈希码
   *
   * @return 此对象的哈希码值。
   * @see Object
   */
  @Override
  public int hashCode() {
    return c.hashCode();
  }

  /**
   * 将此 DateTime 与指定 Object 比较是否相等。
   *
   * @param obj
   *        - 要与之比较的对象。
   * @return 如果此对象等于 obj，则返回 true；否则返回 false。
   * @see Object
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DateTime) {
      return c.equals(((DateTime) obj).toCalendar());
    }
    if (obj instanceof Calendar) {
      return c.equals(obj);
    }
    if (obj instanceof java.util.Date) {
      return c.getTime().equals(obj);
    }
    return false;
  }

  /**
   * 返回一个添加对应天数的新对象
   *
   * @param amount
   *        天数
   * @return DateTime
   */
  public DateTime addDays(final int amount) {
    Calendar calendar = (Calendar) c.clone();
    calendar.add(Calendar.DAY_OF_MONTH, amount);
    return new DateTime(calendar);
  }

  /**
   * 返回一个新的DateTime对象，它将指定的小时数加到此实例的值上 （24小时制）
   *
   * @param amount
   *        小时数
   * @return DateTime
   */
  public DateTime addHours(final int amount) {
    Calendar calendar = (Calendar) c.clone();
    calendar.add(Calendar.HOUR_OF_DAY, amount);
    return new DateTime(calendar);
  }

  /**
   * 返回一个新的DateTime对象，它将指定的秒数加到此实例的值上
   *
   * @param amount
   *        秒数
   * @return DateTime
   */
  public DateTime addSeconds(final int amount) {
    Calendar calendar = (Calendar) c.clone();
    calendar.add(Calendar.SECOND, amount);
    return new DateTime(calendar);
  }

  /**
   * 返回一个新的TimeSpan对象，它将减去相对应实例
   *
   * @param date
   *        参与运算的实例
   * @return TimeSpan
   */
  public TimeSpan subtract(final DateTime date) {
    return new TimeSpan(c.getTimeInMillis() * 10000 - date.getTimeInTicksMillis());
  }

  /**
   * 返回一个新的TimeSpan对象，它将减去相对应实例
   *
   * @param d1
   *        左
   * @param d2
   *        右
   * @return TimeSpan
   */
  public static TimeSpan subtract(DateTime d1, DateTime d2) {
    return new TimeSpan(d1.getTimeInTicksMillis() - d2.getTimeInTicksMillis());
  }

  // =======================其他===============================
  /**
   * 如果日期参数为NULL 则返回默认日期
   *
   * @param date
   *        日期参数
   * @return 比较后的日期
   * @throws ParseException
   *         ParseException
   */
  public static DateTime defaultDate(final DateTime date) throws ParseException {
    return date == null ? new DateTime(defaultC) : date;
  }

  /**
   * 返回默认时日期<br/>
   * 1900-01-01
   *
   * @return DateTime 默认时日期
   * @throws ParseException
   *         ParseException
   */
  public static DateTime defaultDate() throws ParseException {
    return defaultDate(null);
  }

}
