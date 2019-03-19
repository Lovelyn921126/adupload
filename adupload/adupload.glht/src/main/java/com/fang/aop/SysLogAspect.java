package com.fang.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fang.entity.SysLog;
import com.fang.service.SysLogService;
import com.fang.utils.IPUtil;
import com.fang.utils.JsonXmlObjUtils;
import com.fang.utils.ShiroUtil;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.web.HttpContextUtil;

import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;

/**
 * 系统日志，切面处理类
 *
 * @author wangzhiyuan
 */
@Aspect
@Component
public class SysLogAspect {

  /**
   * sysLogService
   */
  @Autowired
  private SysLogService sysLogService;

  /**
   * logPointCut
   */
  @Pointcut("@annotation(com.fang.annotation.SysLog)")
  public void logPointCut() {

  }

  /**
   * saveSysLog
   *
   * @param joinPoint
   *        joinPoint
   */
  @Before("logPointCut()")
  public void saveSysLog(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();

    SysLog sysLog = new SysLog();
    com.fang.annotation.SysLog annoSyslog = method.getAnnotation(com.fang.annotation.SysLog.class);
    if (annoSyslog != null) {
      // 注解上的描述
      sysLog.setOperation(annoSyslog.value());
    }

    // 请求的方法名
    String className = joinPoint.getTarget().getClass().getName();
    String methodName = signature.getName();
    sysLog.setMethod(className + "." + methodName + "()");

    // 请求的参数
    Object[] args = joinPoint.getArgs();
    String params = JsonXmlObjUtils.obj2json(args[0]);
    sysLog.setParams(params);

    // 获取request
    HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
    // 设置IP地址
    sysLog.setIp(IPUtil.getIpAddr(request));

    // 用户名
    String username = ShiroUtil.getUser().getUsername();
    sysLog.setUsername(username);

    sysLog.setCreateDate(DateTime.now().toDate());
    // 保存系统日志
    sysLogService.save(sysLog);
  }

}
