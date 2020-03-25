package com.github.wujiuye.datasource.config;

import com.github.wujiuye.datasource.annotation.EasyMutiDataSource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 主从数据源配置
 *
 * @author wujiuye
 * @version 1.0 on 2020/03/15
 */
public class MasterSlaveDataSourceConfig extends AbstractDataSourceConfig {

    public MasterSlaveDataSourceConfig(DataSourcePropertys propertys) {
        super(propertys);
    }

    @Override
    public EasyMutiDataSource.MultipleDataSource defaultDataSource() {
        String defaultDb = propertys.getDefalutDataSource();
        if (!StringUtils.isEmpty(defaultDb)) {
            return EasyMutiDataSource.MultipleDataSource.valueOf(defaultDb);
        }
        return EasyMutiDataSource.MultipleDataSource.Master;
    }

    @Override
    public Map<Object, DataSource> dataSource() {
        Map<Object, DataSource> dataSourceMap = new HashMap<>();
        DataSource master = createDataSource(propertys.getMaster(), propertys.getPool());
        DataSource slave = createDataSource(propertys.getSlave(), propertys.getPool());
        if (master != null) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Master, master);
        }
        if (slave != null) {
            dataSourceMap.put(EasyMutiDataSource.MultipleDataSource.Slave, slave);
        }
        return dataSourceMap;
    }

}
