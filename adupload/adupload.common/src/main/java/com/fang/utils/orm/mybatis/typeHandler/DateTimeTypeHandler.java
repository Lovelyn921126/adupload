/**
 * File：DateTimeTypeHandler.java
 * Package：com.fang.framework.datasource.orm.mybatis.typeHandler
 * Author："wangzhiyuan"
 * Date：2016年9月30日 上午10:10:10
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.orm.mybatis.typeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.fang.utils.lang.time.DateTime;

/**
 * DateTime类型转换器
 *
 * @author wangzhiyuan
 */
public class DateTimeTypeHandler extends BaseTypeHandler<DateTime> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, DateTime parameter, JdbcType jdbcType) throws SQLException {
    ps.setTimestamp(i, new Timestamp(parameter.getTimeInMillis()));
  }

  @Override
  public DateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
    Timestamp sqlTimestamp = rs.getTimestamp(columnName);
    if (sqlTimestamp != null) {
      return new DateTime(sqlTimestamp.getTime());
    }
    return null;
  }

  @Override
  public DateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
    if (sqlTimestamp != null) {
      return new DateTime(sqlTimestamp.getTime());
    }
    return null;
  }

  @Override
  public DateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    Timestamp sqlTimestamp = cs.getTimestamp(columnIndex);
    if (sqlTimestamp != null) {
      return new DateTime(sqlTimestamp.getTime());
    }
    return null;
  }

}
