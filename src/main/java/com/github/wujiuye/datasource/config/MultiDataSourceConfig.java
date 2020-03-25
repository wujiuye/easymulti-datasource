package com.github.wujiuye.datasource.config;

import com.github.wujiuye.datasource.annotation.EasyMutiDataSource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 非主从的多数据源配置
 *
 * @author wujiuye
 * @version 1.0 on 2020/03/15
 */
public class MultiDataSourceConfig extends AbstractDataSourceConfig {

    public MultiDataSourceConfig(DataSourcePropertys propertys) {
        super(propertys);
    }

    @Override
    public EasyMutiDataSource.MultipleDataSource defaultDataSource() {
        String defaultDb = propertys.getDefalutDataSource();
        if (!StringUtils.isEmpty(defaultDb)) {
            return EasyMutiDataSource.MultipleDataSource.valueOf(defaultDb);
        }
        return EasyMutiDataSource.MultipleDataSource.First;
    }

    @Override
    public Map<Object, DataSource> dataSource() {
        Map<Object, DataSource> dataSourceMap = new HashMap<>();
        if (propertys.getFirst() != null) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.First,
                    createDataSource(propertys.getMaster(), propertys.getPool()));
        }
        if (propertys.getSecond() != null) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Second,
                    createDataSource(propertys.getMaster(), propertys.getPool()));
        }
        if (propertys.getThird() != null) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Third,
                    createDataSource(propertys.getMaster(), propertys.getPool()));
        }
        if (propertys.getFourth() != null) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Fourth,
                    createDataSource(propertys.getMaster(), propertys.getPool()));
        }
        if (propertys.getFifth() != null) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Fifth,
                    createDataSource(propertys.getMaster(), propertys.getPool()));
        }
        if (propertys.getSixth() != null) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Sixth,
                    createDataSource(propertys.getMaster(), propertys.getPool()));
        }
        if (propertys.getSeventh() != null) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Seventh,
                    createDataSource(propertys.getMaster(), propertys.getPool()));
        }
        if (propertys.getEighth() != null) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Eighth,
                    createDataSource(propertys.getMaster(), propertys.getPool()));
        }
        if (propertys.getNinth() != null) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Ninth,
                    createDataSource(propertys.getMaster(), propertys.getPool()));
        }
        if (propertys.getTenth() != null) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Tenth,
                    createDataSource(propertys.getMaster(), propertys.getPool()));
        }
        return dataSourceMap;
    }

}
