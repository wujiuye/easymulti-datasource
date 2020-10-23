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
            return EasyMutiDataSource.MultipleDataSource.valueBy(defaultDb);
        }
        return EasyMutiDataSource.MultipleDataSource.First;
    }

    @Override
    public Map<Object, DataSource> dataSource() {
        Map<Object, DataSource> dataSourceMap = new HashMap<>();
        if (propertys.getFirst() != null && !propertys.getFirst().isEmpty()) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.First,
                    createDataSource(propertys.getFirst()));
        }
        if (propertys.getSecond() != null && !propertys.getSecond().isEmpty()) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Second,
                    createDataSource(propertys.getSecond()));
        }
        if (propertys.getThird() != null && !propertys.getThird().isEmpty()) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Third,
                    createDataSource(propertys.getThird()));
        }
        if (propertys.getFourth() != null && !propertys.getFourth().isEmpty()) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Fourth,
                    createDataSource(propertys.getFourth()));
        }
        if (propertys.getFifth() != null && !propertys.getFifth().isEmpty()) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Fifth,
                    createDataSource(propertys.getFifth()));
        }
        if (propertys.getSixth() != null && !propertys.getSixth().isEmpty()) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Sixth,
                    createDataSource(propertys.getSixth()));
        }
        if (propertys.getSeventh() != null && !propertys.getSeventh().isEmpty()) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Seventh,
                    createDataSource(propertys.getSeventh()));
        }
        if (propertys.getEighth() != null && !propertys.getEighth().isEmpty()) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Eighth,
                    createDataSource(propertys.getEighth()));
        }
        if (propertys.getNinth() != null && !propertys.getNinth().isEmpty()) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Ninth,
                    createDataSource(propertys.getNinth()));
        }
        if (propertys.getTenth() != null && !propertys.getTenth().isEmpty()) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Tenth,
                    createDataSource(propertys.getTenth()));
        }
        return dataSourceMap;
    }

}
