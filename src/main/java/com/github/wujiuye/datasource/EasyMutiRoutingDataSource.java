package com.github.wujiuye.datasource;

import com.github.wujiuye.datasource.annotation.DataSourceContextHolder;
import com.github.wujiuye.datasource.annotation.EasyMutiDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 *
 * @author wujiuye 2020/03/15
 */
public class EasyMutiRoutingDataSource extends AbstractRoutingDataSource {

    private EasyMutiDataSource.MultipleDataSource defaultDb;

    /**
     * 指定默认数据源
     *
     * @param defaultDb
     */
    public void setDefaultDataSource(EasyMutiDataSource.MultipleDataSource defaultDb) {
        this.defaultDb = defaultDb;
    }

    /**
     * 使用默认数据源之前先确保数据源存在
     *
     * @return
     */
    private Object ensureDefaultDataSource() {
        if (this.defaultDb == null) {
            throw new RuntimeException("未设置默认数据源，无法使用默认数据源！");
        }
        return this.defaultDb;
    }

    /**
     * 动态设置数据源
     *
     * @return 配置多数据源时map的key
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource() == null ?
                ensureDefaultDataSource() : DataSourceContextHolder.getDataSource();
    }

}