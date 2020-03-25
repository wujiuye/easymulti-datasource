package com.github.wujiuye.datasource.config;

import com.github.wujiuye.datasource.annotation.EasyMutiDataSource;
import org.springframework.util.StringUtils;

/**
 * 数据源配置工厂类
 *
 * @author wujiuye
 * @version 1.0 on 2020/03/25
 */
public class DataSourceConfigFactory {

    private static void checkMasterSavle(DataSourcePropertys propertys) {
        if (propertys.getFirst() != null || propertys.getSecond() != null
                || propertys.getThird() != null || propertys.getFourth() != null
                || propertys.getFifth() != null || propertys.getSixth() != null
                || propertys.getSeventh() != null || propertys.getEighth() != null
                || propertys.getNinth() != null || propertys.getTenth() != null) {
            throw new RuntimeException("动态数据源配置出错，使用主从数据源就不能使用其它数据源！");
        }
    }

    private static void checkOneToThen(DataSourcePropertys propertys) {
        if (propertys.getMaster() != null || propertys.getSlave() != null) {
            throw new RuntimeException("动态数据源配置出错，使用1~10数据源就不能使用主从数据源！");
        }
    }

    /**
     * 根据配置文件选择不同的数据源配置器
     * 注意：如果使用了主从数据源，则不能使用1～10的数据源
     *
     * @param propertys
     * @return
     */
    public static AbstractDataSourceConfig getDataSourceConfig(DataSourcePropertys propertys) {
        if (propertys.getMaster() != null) {
            checkMasterSavle(propertys);
            return new MasterSlaveDataSourceConfig(propertys);
        } else {
            checkOneToThen(propertys);
            if (StringUtils.isEmpty(propertys.getDefalutDataSource()) && propertys.getFirst() != null) {
                throw new RuntimeException("如果不配置默认使用的数据源，那么first库就不能为空，因为不配置默认使用的数据源，就会使用first作为默认的数据源！");
            }
            return new MultiDataSourceConfig(propertys);
        }
    }

}
