package com.github.wujiuye.r2dbc.config;

/**
 * @author wujiuye 2020/11/03
 */
public class ConnectionPoolConfig {

    private int initialSize;
    private int maxSize;
    /**
     * 连接空间超时，单位秒
     */
    private int idelTimeout;

    private String validationQuery = "select 1";

    public int getIdelTimeout() {
        return idelTimeout;
    }

    public void setIdelTimeout(int idelTimeout) {
        this.idelTimeout = idelTimeout;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

}
