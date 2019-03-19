/**
 * File：TimeSpan.java
 * Package：com.fang.framework.lang.time
 * Author：wangzhiyuan
 * Date：2016年11月24日 下午3:24:54
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.lang.time;

import java.io.Serializable;

/**
 * 时间间隔类
 *
 * @author wangzhiyuan
 */
public class TimeSpan implements Serializable, Cloneable, Comparable<TimeSpan> {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -3636315294836229325L;

  /**
   * TicksPerMillisecond
   */
  public static final long TICKSPERMILLISECOND = 10000;

  /**
   * MillisecondsPerTick
   */
  private static final double MILLISECONDSPERTICK = 1.0 / TICKSPERMILLISECOND;

  /**
   * TicksPerSecond
   */
  public static final long TICKSPERSECOND = TICKSPERMILLISECOND * 1000; // 10,000,000

  /**
   * SecondsPerTick
   */
  private static final double SECONDSPERTICK = 1.0 / TICKSPERSECOND; // 0.0001

  /**
   * TicksPerMinute
   */
  public static final long TICKSPERMINUTE = TICKSPERSECOND * 60; // 600,000,000

  /**
   * MinutesPerTick
   */
  private static final double MINUTESPERTICK = 1.0 / TICKSPERMINUTE; // 1.6666666666667e-9

  /**
   * TicksPerHour
   */
  public static final long TICKSPERHOUR = TICKSPERMINUTE * 60; // 36,000,000,000

  /**
   * HoursPerTick
   */
  private static final double HOURSPERTICK = 1.0 / TICKSPERHOUR; // 2.77777777777777778e-11

  /**
   * TicksPerDay
   */
  public static final long TICKSPERDAY = TICKSPERHOUR * 24; // 864,000,000,000

  /**
   * DaysPerTick
   */
  private static final double DAYSPERTICK = 1.0 / TICKSPERDAY; // 1.1574074074074074074e-12

  /**
   * MillisPerSecond
   */
  private static final int MILLISPERSECOND = 1000;

  /**
   * MillisPerMinute
   */
  private static final int MILLISPERMINUTE = MILLISPERSECOND * 60; // 60,000

  /**
   * MillisPerHour
   */
  private static final int MILLISPERHOUR = MILLISPERMINUTE * 60; // 3,600,000

  /**
   * MillisPerDay
   */
  @SuppressWarnings("unused")
  private static final int MILLISPERDAY = MILLISPERHOUR * 24; // 86,400,000

  /**
   * MaxSeconds
   */
  @SuppressWarnings("unused")
  private static final long MAXSECONDS = 9223372036854775807L / TICKSPERSECOND;

  /**
   * MinSeconds
   */
  @SuppressWarnings("unused")
  private static final long MINSECONDS = -9223372036854775808L / TICKSPERSECOND;

  /**
   * MaxMilliSeconds
   */
  private static final long MAXMILLISECONDS = 9223372036854775807L / TICKSPERMILLISECOND;

  /**
   * MinMilliSeconds
   */
  private static final long MINMILLISECONDS = -9223372036854775808L / TICKSPERMILLISECOND;

  /**
   * TicksPerTenthSecond
   */
  public static final long TICKSPERTENTHSECOND = TICKSPERMILLISECOND * 100;

  /**
   * Zero
   */
  public static final TimeSpan ZERO = new TimeSpan(0);

  /**
   * MaxValue
   */
  public static final TimeSpan MAXVALUE = new TimeSpan(9223372036854775807L);

  /**
   * MinValue
   */
  public static final TimeSpan MINVALUE = new TimeSpan(-9223372036854775808L);

  /**
   * ticks
   */
  private long ticks;

  /**
   * Constructs a <code>Timestamp</code> object
   * using a milliseconds time value. The
   * integral seconds are stored in the underlying date value; the
   * fractional seconds are stored in the <code>nanos</code> field of
   * the <code>Timestamp</code> object.
   *
   * @param ticks
   *        milliseconds since January 1, 1970, 00:00:00 GMT.
   *        A negative number is the number of milliseconds before
   *        January 1, 1970, 00:00:00 GMT.
   * @see java.util.Calendar
   */
  public TimeSpan(long ticks) {
    this.ticks = ticks;
  }

  /**
   * getTicks()
   *
   * @return ticks
   */
  public long ticks() {
    return ticks;
  }

  /**
   * 获取当前TimeSpan结构所表示的时间间隔的天数部分
   *
   * @return 天数
   */
  public int days() {
    return (int) (ticks / TICKSPERDAY);
  }

  /**
   * 获取当前TimeSpan结构所表示的时间间隔的小时数部分
   *
   * @return 小时数
   */
  public int hours() {
    return (int) ((ticks / TICKSPERHOUR) % 24);
  }

  /**
   * 获取当前TimeSpan结构所表示的时间间隔的毫秒数部分
   *
   * @return 毫秒数
   */
  public int milliseconds() {
    return (int) ((ticks / TICKSPERMILLISECOND) % 1000);
  }

  /**
   * 获取当前TimeSpan结构所表示的时间间隔的分钟数部分
   *
   * @return 分钟数
   */
  public int minutes() {
    return (int) ((ticks / TICKSPERMINUTE) % 60);
  }

  /**
   * 获取当前TimeSpan结构所表示的时间间隔的秒数部分
   *
   * @return 秒数
   */
  public int seconds() {
    return (int) ((ticks / TICKSPERSECOND) % 60);
  }

  /**
   * 获取以整天数和天的小数部分表示的当前TimeSpan结构的值
   *
   * @return 整天数和天的小数部分
   */
  public double totalDays() {
    return ((double) ticks) * DAYSPERTICK;
  }

  /**
   * 获取以整小时数和小时的小数部分表示的当前TimeSpan结构的值
   *
   * @return 小时数和小时的小数部分
   */
  public double totalHours() {
    return (double) ticks * HOURSPERTICK;
  }

  /**
   * 获取以整毫秒数和毫秒的小数部分表示的当前TimeSpan结构的值
   *
   * @return 毫秒数和毫秒的小数部分
   */
  public double totalMilliseconds() {
    double temp = (double) ticks * MILLISECONDSPERTICK;
    if (temp > MAXMILLISECONDS) {
      return (double) MAXMILLISECONDS;
    }

    if (temp < MINMILLISECONDS) {
      return (double) MINMILLISECONDS;
    }

    return temp;
  }

  /**
   * 获取以整分钟数和分钟的小数部分表示的当前TimeSpan结构的值
   *
   * @return 分钟数和分钟的小数部分
   */
  public double totalMinutes() {
    return (double) ticks * MINUTESPERTICK;
  }

  /**
   * 获取以整秒数和秒的小数部分表示的当前TimeSpan结构的值
   *
   * @return 秒数和秒的小数部分
   */
  public double totalSeconds() {
    return (double) ticks * SECONDSPERTICK;
  }

  /**
   * 将指定的TimeSpan添加到此实例中
   *
   * @param ts
   *        TimeSpan
   * @return new TimeSpan
   */
  public TimeSpan add(TimeSpan ts) {
    long result = ticks + ts.ticks;
    // Overflow if signs of operands was identical and result's
    // sign was opposite.
    // >> 63 gives the sign bit (either 64 1's or 64 0's).
    if ((ticks >> 63 == ts.ticks >> 63) && (ticks >> 63 != result >> 63)) {
      throw new RuntimeException("Overflow_TimeSpanTooLong");
    }
    return new TimeSpan(result);
  }

  /**
   * 两个TimeSpan对象比较
   *
   * @param t1
   *        要比较的对象TimeSpan
   * @param t2
   *        要比较的对象TimeSpan
   * @return
   *         左 > 右，返回1<br/>
   *         左 < 右，返回-1<br/>
   *         左 = 右，返回0
   */
  public static int compare(TimeSpan t1, TimeSpan t2) {
    if (t1.ticks > t2.ticks) {
      return 1;
    }
    if (t1.ticks < t2.ticks) {
      return -1;
    }
    return 0;
  }

  /**
   * 将此实例与指定TimeSpan进行比较，并返回一个整数，该整数指示此实例是短于，等于，大于指定TimeSpan
   *
   * @param value
   *        value
   * @return 结果
   */
  public int compareTo(TimeSpan value) {
    long t = value.ticks;
    if (ticks > t) {
      return 1;
    }
    if (ticks < t) {
      return -1;
    }
    return 0;
  }

}
