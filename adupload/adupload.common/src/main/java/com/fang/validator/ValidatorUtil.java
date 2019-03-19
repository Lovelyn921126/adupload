package com.fang.validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.fang.utils.exception.CommonException;

import java.util.Set;

/**
 * hibernate-validator校验工具类
 *
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 *
 * @author wangzhiyuan
 */
public class ValidatorUtil {

  /**
   * validator
   */
  private static Validator validator;

  /**
   * static
   */
  static {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  /**
   * 校验对象
   *
   * @param object
   *        待校验对象
   * @param groups
   *        待校验的组
   * @throws CommonException
   *         校验不通过，则报RRException异常
   */
  public static void validateEntity(Object object, Class<?>... groups) throws CommonException {
    Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
    if (!constraintViolations.isEmpty()) {
      ConstraintViolation<Object> constraint = (ConstraintViolation<Object>) constraintViolations.iterator().next();
      throw new CommonException(constraint.getMessage());
    }
  }
}
