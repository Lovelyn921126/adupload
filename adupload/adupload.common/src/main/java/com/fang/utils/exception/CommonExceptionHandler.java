package com.fang.utils.exception;

import java.io.PrintWriter;

import com.alibaba.fastjson.JSON;
import com.fang.utils.web.R;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理器
 *
 * @author wangzhiyuan
 */
@Component
public class CommonExceptionHandler implements HandlerExceptionResolver {

  /**
   * logger
   */
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    R r = new R();
    PrintWriter writer = null;
    try {
      response.setContentType("application/json;charset=utf-8");
      response.setCharacterEncoding("utf-8");

      if (ex instanceof CommonException) {
        r.put("code", ((CommonException) ex).getCode());
        r.put("msg", ((CommonException) ex).getMessage());
      } else if (ex instanceof DuplicateKeyException) {
        r = R.error("数据库中已存在该记录");
      } else if (ex instanceof AuthorizationException) {
        r = R.error("没有权限，请联系管理员授权");
      } else {
        r = R.error();
      }

      // 记录异常日志
      if (!(ex instanceof AuthorizationException || ex instanceof DuplicateKeyException || ex instanceof CommonException)) {
        logger.error(ex.getMessage(), ex);
      }

      String json = JSON.toJSONString(r);
      writer = response.getWriter();
      writer.write(json);
    } catch (Exception e) {
      logger.error("CommonExceptionHandler 异常处理失败", e);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
    return new ModelAndView();
  }
}
