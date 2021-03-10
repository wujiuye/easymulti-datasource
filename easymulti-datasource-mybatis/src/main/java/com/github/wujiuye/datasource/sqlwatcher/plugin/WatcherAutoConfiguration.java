package com.github.wujiuye.datasource.sqlwatcher.plugin;

import com.github.wujiuye.datasource.config.DataSourceConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

/**
 * 插件注册
 *
 * @author wujiuye 2020/07/10
 */
@Configuration
@ConditionalOnProperty(value = "easymuti.sql-watcher.enable", havingValue = "true")
@AutoConfigureBefore(DataSourceConfig.class)
@Import(SqlWatcherProps.class)
public class WatcherAutoConfiguration {

    @Resource
    private SqlWatcherProps sqlWatcherProps;

    @Bean
    public SqlWatcherPlugin dataChangePlugin() {
        return new SqlWatcherPlugin();
    }

    @Bean
    public ModifySqlParser dataChangeSqlParser() {
        return new ModifySqlParser();
    }

    @Bean
    public TableFieldChangeWatcher tableFieldChangeWatcher() {
        return new TableFieldSubject();
    }

    @Bean
    @ConditionalOnMissingBean
    public RealExcSqlLogger realExcSqlLogger() {
        RealExcSqlLogger logger = new RealExcSqlLogger();
        logger.setShowInvokeLink(sqlWatcherProps.isShowRealLogInvokeLink());
        return logger;
    }

}
