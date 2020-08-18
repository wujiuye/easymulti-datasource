package com.github.wujiuye.datasource.sqlwatcher;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;

/**
 * 增删改监听mybatis插件
 *
 * @author wujiuye 2020/07/10
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
class SqlWatcherPlugin implements Interceptor {

    private final Logger logger = LoggerFactory.getLogger(SqlWatcherPlugin.class);

    /**
     * 适配Druid连接池
     */
    private static final String DruidPooledPreparedStatement = "com.alibaba.druid.pool.DruidPooledPreparedStatement";
    private Method druidGetSQLMethod;
    /**
     * 适配Hikari连接池
     */
    private static final String HikariPreparedStatementWrapper = "com.zaxxer.hikari.pool.HikariProxyPreparedStatement";

    @Autowired
    private ModifySqlParser modifySqlParser;
    @Autowired
    private TableFieldChangeWatcher watcher;
    @Autowired(required = false)
    private RealExcSqlLogger realExcSqlLogger;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Statement statement;
        Object firstArg = invocation.getArgs()[0];
        if (Proxy.isProxyClass(firstArg.getClass())) {
            statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
        } else {
            statement = (Statement) firstArg;
        }
        try {
            statement = (Statement) SystemMetaObject.forObject(statement).getValue("stmt.statement");
        } catch (Exception e) {
            // do nothing
        }

        String originalSql = null;
        String stmtClassName = statement.getClass().getName();
        if (DruidPooledPreparedStatement.equals(stmtClassName)) {
            try {
                if (druidGetSQLMethod == null) {
                    Class<?> clazz = Class.forName(DruidPooledPreparedStatement);
                    druidGetSQLMethod = clazz.getMethod("getSql");
                }
                Object stmtSql = druidGetSQLMethod.invoke(statement);
                if (stmtSql instanceof String) {
                    originalSql = (String) stmtSql;
                }
            } catch (Exception e) {
                // do nothing
            }
        } else if (HikariPreparedStatementWrapper.equals(stmtClassName)) {
            try {
                Object sqlStatement = SystemMetaObject.forObject(statement).getValue("delegate.sqlStatement");
                if (sqlStatement != null) {
                    originalSql = sqlStatement.toString();
                }
            } catch (Exception e) {
                //ignore
            }
        }
        if (originalSql == null) {
            originalSql = statement.toString();
        }

        int index = originalSql.indexOf(':');
        if (index > 0) {
            originalSql = originalSql.substring(index + 1);
        }

        // 是否需要打印日记
        if (realExcSqlLogger != null) {
            realExcSqlLogger.showRealLog(originalSql);
        }

        // 获取匹配结果
        String transactionId = matchWatchSql(originalSql);
        try {
            Object result = invocation.proceed();
            if (transactionId != null) {
                watcher.watchSuccess(transactionId);
            }
            return result;
        } catch (Throwable ex) {
            if (transactionId != null) {
                watcher.watchFail(transactionId, ex);
            }
            throw ex;
        }
    }

    private String matchWatchSql(String originalSql) {
        String transactionId = null;
        try {
            // 获取匹配结果
            MatchResult matchResult = modifySqlParser.matchResult(watcher, originalSql);
            // 通知监听器
            if (matchResult != null) {
                transactionId = UUID.randomUUID().toString();
                if (!CollectionUtils.isEmpty(matchResult.getMatchFields())) {
                    logger.info("监听到SQL：{} ", originalSql);
                    watcher.watchStart(transactionId, matchResult);
                }
            }
        } catch (Throwable e) {
            logger.error("sql 解析异常：" + e.getMessage() + "，sql==>" + originalSql, e);
        }
        return transactionId;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
