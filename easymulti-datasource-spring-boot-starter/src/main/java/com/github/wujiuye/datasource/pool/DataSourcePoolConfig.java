package com.github.wujiuye.datasource.pool;

import com.github.wujiuye.datasource.config.DataSourcePropertys;

import javax.sql.DataSource;

/**
 * @author wujiuye
 * @version 1.0 on 2020/03/15
 */
public abstract class DataSourcePoolConfig {

    /**
     * 根据数据库连接池自动配置连接池数据源
     *
     * @param username
     * @param password
     * @param driver
     * @param jdbcUrl
     * @return
     */
    public abstract DataSource usePoolWithConfig(String driver, String jdbcUrl, String username, String password,
                                                 final DataSourcePropertys.ConnectionPool connectionPool);

}
