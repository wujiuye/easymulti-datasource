package com.github.wujiuye.datasource.sqlwatcher;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 插件注册
 *
 * @author wujiuye 2020/07/10
 */
@Configuration
@ConditionalOnBean(SqlWatcherProps.class)
public class WatcherAutoConfiguration {

    @Resource
    private SqlWatcherProps sqlWatcherProps;

    @Bean
    @ConditionalOnMissingBean
    public SqlWatcherPlugin dataChangePlugin() {
        return new SqlWatcherPlugin();
    }

    @Bean
    @ConditionalOnMissingBean
    public ModifySqlParser dataChangeSqlParser() {
        return new ModifySqlParser();
    }

    @Bean
    @ConditionalOnMissingBean
    public TableFieldChangeWatcher tableFieldChangeWatcher() {
        return new TableFieldSubject();
    }

    @Bean
    @ConditionalOnMissingBean
    public RealExcSqlLogger realExcSqlLogger() {
        RealExcSqlLogger logger = new RealExcSqlLogger();
        logger.setShowInvokeLink(sqlWatcherProps.getShowRealLogInvokeLink());
        return logger;
    }

}
