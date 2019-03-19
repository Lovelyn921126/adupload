package com.fang.utils.orm.mybatis;

import static java.lang.reflect.Proxy.newProxyInstance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 动态代理SqlSessionTemplate
 * 在执行SqlSessionTemplate操作数据库方法之前，根据方法名
 * 动态判断是发往主库还是从库
 * 如果方法是在spring的事务中，则跳过此环节
 *
 * @author wangzhiyuan
 */
public class DynamicSqlSessionTemplate implements SqlSession {

  /**
   * select
   */
  private static final String SELECT = "select";

  /**
   * insert
   */
  private static final String INSERT = "insert";

  /**
   * delete
   */
  private static final String DELETE = "delete";

  /**
   * update
   */
  private static final String UPDATE = "update";

  /**
   * sqlSessionTemplate
   */
  private SqlSessionTemplate sqlSessionTemplate;

  /**
   * sqlSessionProxy
   */
  private final SqlSession sqlSessionProxy;

  /**
   * 有参构造
   *
   * @param sqlSessionTemplate
   *        sqlSessionTemplate
   */
  public DynamicSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
    this.sqlSessionTemplate = sqlSessionTemplate;
    this.sqlSessionProxy = (SqlSession) newProxyInstance(SqlSessionFactory.class.getClassLoader(), new Class[] {SqlSession.class}, new SqlSessionInterceptor());
  }

  /**
   * 方法拦截
   * 此处是拦截SqlSessionTemplate的方法
   * 进行读写分离
   *
   * @author wangzhiyuan
   */
  private class SqlSessionInterceptor implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      try {
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        if (synchronizationActive) {
          // 在spring的事务中，不做任何处理
          // do nothing
          return method.invoke(sqlSessionTemplate, args);
        } else {
          String methodName = method.getName();
          if (methodName.startsWith(SELECT)) {
            // 获取读集群的数据源
            DataSourceHolder.setSlave();
          } else if (methodName.startsWith(INSERT) || methodName.startsWith(UPDATE) || methodName.startsWith(DELETE)) {
            // 获取主库数据源
            DataSourceHolder.setMaster();
          }
          Object result = method.invoke(sqlSessionTemplate, args);
          // 清理工作
          DataSourceHolder.clearDataSource();
          return result;
        }
      } catch (Exception e) {
        throw e;
      }
    }
  }

  /**
   * Retrieve a single row mapped from the statement key
   *
   * @param statement
   *        statement
   * @param <T>
   *        the returned object type
   * @return Mapped object
   */
  public <T> T selectOne(String statement) {
    return sqlSessionProxy.selectOne(statement);
  }

  /**
   * Retrieve a single row mapped from the statement key and parameter.
   *
   * @param <T>
   *        the returned object type
   * @param statement
   *        Unique identifier matching the statement to use.
   * @param parameter
   *        A parameter object to pass to the statement.
   * @return Mapped object
   */
  public <T> T selectOne(String statement, Object parameter) {
    return sqlSessionProxy.selectOne(statement, parameter);
  }

  /**
   * Retrieve a list of mapped objects from the statement key and parameter.
   *
   * @param <E>
   *        the returned list element type
   * @param statement
   *        Unique identifier matching the statement to use.
   * @return List of mapped object
   */
  public <E> List<E> selectList(String statement) {
    return sqlSessionProxy.selectList(statement);
  }

  /**
   * Retrieve a list of mapped objects from the statement key and parameter.
   *
   * @param <E>
   *        the returned list element type
   * @param statement
   *        Unique identifier matching the statement to use.
   * @param parameter
   *        A parameter object to pass to the statement.
   * @return List of mapped object
   */
  public <E> List<E> selectList(String statement, Object parameter) {
    return sqlSessionProxy.selectList(statement, parameter);
  }

  /**
   * Retrieve a list of mapped objects from the statement key and parameter,
   * within the specified row bounds.
   *
   * @param <E>
   *        the returned list element type
   * @param statement
   *        Unique identifier matching the statement to use.
   * @param parameter
   *        A parameter object to pass to the statement.
   * @param rowBounds
   *        Bounds to limit object retrieval
   * @return List of mapped object
   */
  public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
    return sqlSessionProxy.selectList(statement, parameter, rowBounds);
  }

  /**
   * The selectMap is a special case in that it is designed to convert a list
   * of results into a Map based on one of the properties in the resulting
   * objects.
   * Eg. Return a of Map[Integer,Author] for selectMap("selectAuthors","id")
   *
   * @param <K>
   *        the returned Map keys type
   * @param <V>
   *        the returned Map values type
   * @param statement
   *        Unique identifier matching the statement to use.
   * @param mapKey
   *        The property to use as key for each value in the list.
   * @return Map containing key pair data.
   */
  public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
    return sqlSessionProxy.selectMap(statement, mapKey);
  }

  /**
   * The selectMap is a special case in that it is designed to convert a list
   * of results into a Map based on one of the properties in the resulting
   * objects.
   *
   * @param <K>
   *        the returned Map keys type
   * @param <V>
   *        the returned Map values type
   * @param statement
   *        Unique identifier matching the statement to use.
   * @param parameter
   *        A parameter object to pass to the statement.
   * @param mapKey
   *        The property to use as key for each value in the list.
   * @return Map containing key pair data.
   */
  public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
    return sqlSessionProxy.selectMap(statement, parameter, mapKey);
  }

  /**
   * The selectMap is a special case in that it is designed to convert a list
   * of results into a Map based on one of the properties in the resulting
   * objects.
   *
   * @param <K>
   *        the returned Map keys type
   * @param <V>
   *        the returned Map values type
   * @param statement
   *        Unique identifier matching the statement to use.
   * @param parameter
   *        A parameter object to pass to the statement.
   * @param mapKey
   *        The property to use as key for each value in the list.
   * @param rowBounds
   *        Bounds to limit object retrieval
   * @return Map containing key pair data.
   */
  public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
    return sqlSessionProxy.selectMap(statement, parameter, mapKey, rowBounds);
  }

  /**
   * Retrieve a single row mapped from the statement key and parameter
   * using a {@code ResultHandler}.
   *
   * @param statement
   *        Unique identifier matching the statement to use.
   * @param parameter
   *        A parameter object to pass to the statement.
   * @param handler
   *        ResultHandler that will handle each retrieved row
   */
  @SuppressWarnings("rawtypes")
  public void select(String statement, Object parameter, ResultHandler handler) {
    sqlSessionProxy.select(statement, parameter, handler);
  }

  /**
   * Retrieve a single row mapped from the statement
   * using a {@code ResultHandler}.
   *
   * @param statement
   *        Unique identifier matching the statement to use.
   * @param handler
   *        ResultHandler that will handle each retrieved row
   */
  @SuppressWarnings("rawtypes")
  public void select(String statement, ResultHandler handler) {
    sqlSessionProxy.select(statement, handler);
  }

  /**
   * Retrieve a single row mapped from the statement key and parameter
   * using a {@code ResultHandler} and {@code RowBounds}
   *
   * @param statement
   *        Unique identifier matching the statement to use.
   * @param parameter
   *        A parameter object to pass to the statement.
   * @param rowBounds
   *        RowBound instance to limit the query results
   * @param handler
   *        ResultHandler that will handle each retrieved row
   */
  @SuppressWarnings("rawtypes")
  public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
    sqlSessionProxy.select(statement, parameter, rowBounds, handler);
  }

  /**
   * Execute an insert statement.
   *
   * @param statement
   *        Unique identifier matching the statement to execute.
   * @return int The number of rows affected by the insert.
   */
  public int insert(String statement) {
    return sqlSessionProxy.insert(statement);
  }

  /**
   * Execute an insert statement with the given parameter object. Any generated
   * autoincrement values or selectKey entries will modify the given parameter
   * object properties. Only the number of rows affected will be returned.
   *
   * @param statement
   *        Unique identifier matching the statement to execute.
   * @param parameter
   *        A parameter object to pass to the statement.
   * @return int The number of rows affected by the insert.
   */
  public int insert(String statement, Object parameter) {
    return sqlSessionProxy.insert(statement, parameter);
  }

  /**
   * Execute an update statement. The number of rows affected will be returned.
   *
   * @param statement
   *        Unique identifier matching the statement to execute.
   * @return int The number of rows affected by the update.
   */
  public int update(String statement) {
    return sqlSessionProxy.update(statement);
  }

  /**
   * Execute an update statement. The number of rows affected will be returned.
   *
   * @param statement
   *        Unique identifier matching the statement to execute.
   * @param parameter
   *        A parameter object to pass to the statement.
   * @return int The number of rows affected by the update.
   */
  public int update(String statement, Object parameter) {
    return sqlSessionProxy.update(statement, parameter);
  }

  /**
   * Execute a delete statement. The number of rows affected will be returned.
   *
   * @param statement
   *        Unique identifier matching the statement to execute.
   * @return int The number of rows affected by the delete.
   */
  public int delete(String statement) {
    return sqlSessionProxy.delete(statement);
  }

  /**
   * Execute a delete statement. The number of rows affected will be returned.
   *
   * @param statement
   *        Unique identifier matching the statement to execute.
   * @param parameter
   *        A parameter object to pass to the statement.
   * @return int The number of rows affected by the delete.
   */
  public int delete(String statement, Object parameter) {
    return sqlSessionProxy.delete(statement, parameter);
  }

  /**
   * Flushes batch statements and commits database connection.
   * Note that database connection will not be committed if no updates/deletes/inserts were called.
   * To force the commit call {@link SqlSession#commit(boolean)}
   */
  public void commit() {
    sqlSessionProxy.commit();
  }

  /**
   * Flushes batch statements and commits database connection.
   *
   * @param force
   *        forces connection commit
   */
  public void commit(boolean force) {
    sqlSessionProxy.commit(force);
  }

  /**
   * Discards pending batch statements and rolls database connection back.
   * Note that database connection will not be rolled back if no updates/deletes/inserts were
   * called.
   * To force the rollback call {@link SqlSession#rollback(boolean)}
   */
  public void rollback() {
    sqlSessionProxy.rollback();
  }

  /**
   * Discards pending batch statements and rolls database connection back.
   * Note that database connection will not be rolled back if no updates/deletes/inserts were
   * called.
   *
   * @param force
   *        forces connection rollback
   */
  public void rollback(boolean force) {
    sqlSessionProxy.rollback(force);
  }

  /**
   * Flushes batch statements.
   *
   * @return BatchResult list of updated records
   */
  public List<BatchResult> flushStatements() {
    return sqlSessionProxy.flushStatements();
  }

  /**
   * Closes the session
   */
  public void close() {
    sqlSessionProxy.close();
  }

  /**
   * Clears local session cache
   */
  public void clearCache() {
    sqlSessionProxy.clearCache();
  }

  /**
   * Retrieves current configuration
   *
   * @return Configuration
   */
  public Configuration getConfiguration() {
    return sqlSessionProxy.getConfiguration();
  }

  /**
   * Retrieves a mapper.
   *
   * @param <T>
   *        the mapper type
   * @param type
   *        Mapper interface class
   * @return a mapper bound to this SqlSession
   */
  public <T> T getMapper(Class<T> type) {
    return sqlSessionProxy.getMapper(type);
  }

  /**
   * Retrieves inner database connection
   *
   * @return Connection
   */
  public Connection getConnection() {
    return sqlSessionProxy.getConnection();
  }

  @Override
  public <T> Cursor<T> selectCursor(String statement) {
    return sqlSessionProxy.selectCursor(statement);
  }

  @Override
  public <T> Cursor<T> selectCursor(String statement, Object parameter) {
    return sqlSessionProxy.selectCursor(statement, parameter);
  }

  @Override
  public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
    return sqlSessionProxy.selectCursor(statement, parameter, rowBounds);
  }

}
