package com.fang.validator;

import java.util.regex.Pattern;

import com.fang.common.MsgConfig;
import com.fang.utils.exception.CommonException;
import com.fang.utils.lang.StringUtil;

/**
 * 数据校验
 *
 * @author wangzhiyuan
 */
public abstract class Assert {

  /**
   * 验证数字正则
   */
  private static final String INTPATTERN = "^(0|[1-9][0-9]*)$";

  /**
   * isBlank
   *
   * @param str
   *        str
   * @param message
   *        message
   */
  public static void isBlank(String str, String message) {
    if (StringUtil.isBlank(str)) {
      throw new CommonException(message);
    }
  }

  /**
   * isNull
   *
   * @param object
   *        object
   * @param message
   *        message
   */
  public static void isNull(Object object, String message) {
    if (object == null) {
      throw new CommonException(message);
    }
  }

  /**
   * 通用字符串校验
   *
   * @param mustOrNot
   *        是否需要校验
   * @param checkName
   *        参数名
   * @param checkValueOld
   *        参数值
   * @param checkLength
   *        最大长度 （0 时表示不限定最大长度）
   * @param isNum
   *        是否校验是数字
   */
  public static void commonCheck(boolean mustOrNot, String checkName, Object checkValueOld, int checkLength, boolean isNum) {
    try {

      if (mustOrNot) {
        if (checkValueOld == null) {
          throw new CommonException(errorMsg(MsgConfig.API_PARAM_INCOMPLETE, checkName));
        }
        if (StringUtil.isBlank(String.valueOf(checkValueOld))) {
          throw new CommonException(errorMsg(MsgConfig.API_PARAM_INCOMPLETE, checkName));
        }
      }
      if (checkValueOld != null) {
        String checkValue = String.valueOf(checkValueOld);
        if (isNum) {
          if (!Pattern.compile(INTPATTERN).matcher(checkValue + "").matches()) {
            throw new CommonException(errorMsg(MsgConfig.API_PARAM_ILLEGAL, checkName));
          }
        }
        if (checkLength != 0 && checkLength > 0) {
          if (checkValue.length() > checkLength) {
            throw new CommonException(errorMsg(MsgConfig.API_PARAM_ILLEGAL, checkName));
          }
        }
      }
    } catch (Exception e) {
      throw new CommonException(errorMsg(MsgConfig.API_PARAM_ILLEGAL, checkName));
    }
  }

  /**
   * 拼接error返回信息
   *
   * @param msg
   *        信息
   * @param name
   *        注明字段
   * @return 返回信息
   */
  public static String errorMsg(String msg, String name) {
    StringBuffer message = new StringBuffer(msg.length() + name.length());
    message.append(msg);
    message.append("[");
    message.append(name);
    message.append("]");
    return message.toString();
  }

}
