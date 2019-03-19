package com.fang.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fang.common.ShiroConfig;
import com.fang.entity.SysUser;
import com.fang.utils.RefUtil;
import com.fang.utils.ShiroUtil;
import com.fang.utils.SsoUtil;
import com.fang.utils.lang.StringUtil;
import com.fang.utils.web.R;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

/**
 * 登录相关
 *
 * @author wangzhiyuan
 */
@Controller
public class SysLoginController {

  /**
   * 注入单点登录
   */
  @Autowired
  private SsoUtil ssoUtil;

  /**
   * 注入验证码生成器
   */
  @Autowired
  private Producer producer;

  /**
   * 生成验证码
   *
   * @param response
   *        response
   * @throws ServletException
   *         ServletException
   * @throws IOException
   *         IOException
   */
  @RequestMapping("captcha.jpg")
  public void captcha(HttpServletResponse response) throws ServletException, IOException {
    response.setHeader("Cache-Control", "no-store, no-cache");
    response.setContentType("image/jpeg");

    // 生成文字验证码
    String text = producer.createText();
    // 生成图片验证码
    BufferedImage image = producer.createImage(text);
    // 保存到shiro session
    ShiroUtil.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

    ServletOutputStream out = response.getOutputStream();
    ImageIO.write(image, "jpg", out);
  }

  /**
   * 登录
   *
   * @param username
   *        username
   * @param password
   *        password
   * @param captcha
   *        captcha
   * @return 登录
   * @throws IOException
   *         IOException
   */
  @ResponseBody
  @RequestMapping(value = "/sys/login.do", method = RequestMethod.POST)
  public R login(String username, String password, String captcha) throws IOException {
    String kaptcha = ShiroUtil.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
    if (!captcha.equalsIgnoreCase(kaptcha)) {
      return R.error("验证码不正确");
    }

    if (StringUtil.containsAny(username, "@fang.com", "@soufun.com")) {
      return R.error("搜房员工账号，请通过搜房办公OA工作台登录");
    }

    try {
      Subject subject = ShiroUtil.getSubject();
      // sha256加密
      password = new Sha256Hash(password).toHex();
      UsernamePasswordToken token = new UsernamePasswordToken(username, password);
      subject.login(token);
    } catch (UnknownAccountException e) {
      return R.error(e.getMessage());
    } catch (IncorrectCredentialsException e) {
      return R.error(e.getMessage());
    } catch (LockedAccountException e) {
      return R.error(e.getMessage());
    } catch (AuthenticationException e) {
      return R.error("账户验证失败");
    }

    return R.ok();

  }

  /**
   * 自动登录
   *
   * @return 页面跳转
   * @throws IOException
   *         IOException
   */
  @RequestMapping(value = "/autologin.html")
  public String autoLogin() throws IOException {
    try {
      RefUtil<String> mail = new RefUtil<String>("");
      boolean result = ssoUtil.autoLogin(ShiroConfig.SSOOAVERIFY, ShiroConfig.SSOSERVICEID, mail);
      if (result && StringUtil.isNotBlank(mail.get())) {
        Subject subject = ShiroUtil.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(mail.get() + "@fang.com", new char[] {'a'});
        subject.login(token);
      }
      if (result) {
        return "redirect:index.html";
      }

      return "redirect:autologinpage.html";
    } catch (Exception e) {
      if (e.getCause() != null && e.getCause().getMessage().equals("User")) {
        return e.getMessage();
      }
      return "redirect:unauthor.html";
    }
  }

  /**
   * 自动登录
   *
   * @return 页面跳转
   * @throws IOException
   *         IOException
   */
  @ResponseBody
  @RequestMapping(value = "/sys/checkAutologin.do", method = RequestMethod.POST)
  public R checkAutologin() throws IOException {
    try {
      RefUtil<String> mail = new RefUtil<String>("");
      boolean result = ssoUtil.autoLogin(ShiroConfig.SSOOAVERIFY, ShiroConfig.SSOSERVICEID, mail);
      if (result && StringUtil.isNotBlank(mail.get())) {
        Subject subject = ShiroUtil.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(mail.get() + "@fang.com", new char[] {'a'});
        subject.login(token);
      }
      if (result) {
        return R.ok().put("username", mail.get() + "@fang.com");
      }

      return R.error("");
    } catch (Exception e) {
      if (e.getCause() != null && e.getCause().getMessage().equals("User")) {
        return R.error(e.getMessage());
      }
      if (e instanceof LockedAccountException) {
        return R.error(e.getMessage());
      }
      return R.error("自动登录失败，请输入用户名密码，或者前去OA登录");
    }
  }

  /**
   * logout
   *
   * @param res
   *        res
   * @return 页面跳转
   * @throws IOException
   *         IOException
   */
  @RequestMapping(value = "/logout")
  public String exit(HttpServletResponse res) throws IOException {
    SysUser sysUser = ShiroUtil.getUser();
    ShiroUtil.logout();
    if (sysUser.getType().equals(0)) {
      //ShiroUtil.clearCookies(res);
      return "redirect:autologinpage.html";
    }
    return "redirect:login2.html";
  }

}
