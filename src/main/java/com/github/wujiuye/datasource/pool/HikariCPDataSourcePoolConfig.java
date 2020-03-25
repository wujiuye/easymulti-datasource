package com.github.wujiuye.datasource.pool;

import com.github.wujiuye.datasource.config.DataSourcePropertys;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * HikariCP数据库连接池
 *
 * @author wujiuye
 * @version 1.0 on 2020/03/15
 */
public class HikariCPDataSourcePoolConfig extends DataSourcePoolConfig {

    /**
     * 使用HikariCP数据库连接池
     *
     * @param driver
     * @param jdbcUrl
     * @param username
     * @param password
     * @return
     */
    @Override
    public DataSource usePoolWithConfig(String driver, String jdbcUrl, String username, String password,
                                        final DataSourcePropertys.ConnectionPool connectionPool) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        this.applyDefaultPoolConfig(hikariConfig, connectionPool);
        return new HikariDataSource(hikariConfig);
    }

    /**
     * 连接池配置
     *
     * @param hikariConfig
     */
    private void applyDefaultPoolConfig(final HikariConfig hikariConfig, final DataSourcePropertys.ConnectionPool connectionPool) {
        // 最大池大小
        hikariConfig.setMaximumPoolSize(connectionPool.getMaxPoolSize());
        // 连接超时
        hikariConfig.setConnectionTimeout(connectionPool.getConnectionTimeout());
        // 连接的最大生命周期
        hikariConfig.setMaxLifetime(connectionPool.getMaxLifetime());
        // idle超时
        hikariConfig.setIdleTimeout(connectionPool.getIdleTimeout());
        // 验证超时间隔
        hikariConfig.setValidationTimeout(3 * 1000L);
    }

}
