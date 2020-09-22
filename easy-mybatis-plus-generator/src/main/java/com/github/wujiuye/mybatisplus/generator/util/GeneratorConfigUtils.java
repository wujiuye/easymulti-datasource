package com.github.wujiuye.mybatisplus.generator.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * 通用配置
 *
 * @author wujiuye 2020/04/22
 */
public class GeneratorConfigUtils {

    private final static String CONFIG_FILE_PATH = "mybatis-plus-generator.properties";
    private final static String DRIVER_NAME_MYSQL = "com.mysql.jdbc.Driver";

    private static DbType checkDbType(String driverName) {
        if (DRIVER_NAME_MYSQL.equals(driverName)) {
            return DbType.MYSQL;
        }
        throw new UnsupportedOperationException("未识别...");
    }

    public static GlobalConfig getGlobalConfig() throws Exception {
        GlobalConfig config = PropertiesUtils.getPropertiesConfig(GlobalConfig.class, "globalconfig", CONFIG_FILE_PATH);
        config.setActiveRecord(false)
                .setFileOverride(true)
                .setFileOverride(true)
                .setBaseResultMap(true)
                .setEnableCache(false);
        return config;
    }

    public static DataSourceConfig getDataSourceConfig() throws Exception {
        DataSourceConfig dataSourceConfig = PropertiesUtils.getPropertiesConfig(DataSourceConfig.class, "datasource.jdbc", CONFIG_FILE_PATH);
        dataSourceConfig.setDbType(checkDbType(dataSourceConfig.getDriverName()));
        return dataSourceConfig;
    }

    public static StrategyConfig getStrategyConfig(String... table) {
        return new StrategyConfig()
                .setCapitalMode(true)
                .setEntityLombokModel(true)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setNaming(NamingStrategy.underline_to_camel)
                .setSuperMapperClass(com.baomidou.mybatisplus.core.mapper.BaseMapper.class.getName())
                .setInclude(table);
    }

}
