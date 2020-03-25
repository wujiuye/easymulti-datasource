package com.github.wujiuye.datasource.config;

import com.github.wujiuye.datasource.annotation.EasyMutiDataSource;
import com.github.wujiuye.datasource.pool.HikariCPDataSourcePoolConfig;
import com.zaxxer.hikari.util.DriverDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * 抽象多数据源配置
 *
 * @author wujiuye 2020/03/15
 */
public abstract class AbstractDataSourceConfig {

    protected DataSourcePropertys propertys;

    public AbstractDataSourceConfig(DataSourcePropertys propertys) {
        this.propertys = propertys;
    }

    /**
     * 返回默认使用哪个数据源
     *
     * @return
     */
    public abstract EasyMutiDataSource.MultipleDataSource defaultDataSource();

    /**
     * 返回所有数据源
     *
     * @return
     */
    public abstract Map<Object, DataSource> dataSource();

    /**
     * 创建数据源
     *
     * @param masterSlaveDataSource 数据源配置
     * @param connectionPool        连接池配置
     * @return
     */
    protected DataSource createDataSource(DataSourcePropertys.MasterSlaveDataSource masterSlaveDataSource, DataSourcePropertys.ConnectionPool connectionPool) {
        if (connectionPool == null || !connectionPool.isUseConnPool()) {
            return new HikariCPDataSourcePoolConfig()
                    .usePoolWithConfig("com.mysql.jdbc.Driver",
                            masterSlaveDataSource.getJdbcUrl(),
                            masterSlaveDataSource.getUsername(),
                            masterSlaveDataSource.getPassword(),
                            connectionPool);
        } else {
            return new DriverDataSource(masterSlaveDataSource.getJdbcUrl(),
                    "com.mysql.jdbc.Driver",
                    new Properties(),
                    masterSlaveDataSource.getUsername(),
                    masterSlaveDataSource.getPassword());
        }
    }

}
