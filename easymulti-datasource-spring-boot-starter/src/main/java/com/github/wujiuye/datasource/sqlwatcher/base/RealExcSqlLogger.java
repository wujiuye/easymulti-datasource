package com.github.wujiuye.datasource.sqlwatcher.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 打印真正需要执行的SQL
 *
 * @author wujiuye 2020/07/12
 */
class RealExcSqlLogger {

    private final Logger logger = LoggerFactory.getLogger(RealExcSqlLogger.class);

    /**
     * 是否需要打印调用链
     */
    private boolean showInvokeLink = false;

    public void setShowInvokeLink(boolean showInvokeLink) {
        this.showInvokeLink = showInvokeLink;
    }

    public RealExcSqlLogger() {

    }

    public void showRealLog(String reqlSql) {
        if (!showInvokeLink) {
            logger.info("sql===> {}", reqlSql);
        } else {
            try {
                throw new RuntimeException();
            } catch (RuntimeException e) {
                logger.error("sql===> {}, invoke link===>{}", reqlSql, e);
            }
        }
    }

}
