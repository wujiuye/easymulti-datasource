package com.github.wujiuye.datasource.config;

import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.baomidou.mybatisplus.spring.boot.starter.MybatisPlusProperties;
import com.baomidou.mybatisplus.spring.boot.starter.SpringBootVFS;
import com.github.wujiuye.datasource.EasyMutiRoutingDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源自动配置
 *
 * @author wujiuye
 * @version 1.0 on 2020/03/15
 * }
 */
@Import(MybatisPlusProperties.class)
public class DataSourceConfig {

    /**
     * 动态数据源
     *
     * @return
     */
    @Bean
    @Primary
    public EasyMutiRoutingDataSource multipleDataSource(@Autowired DataSourcePropertys propertys) throws Exception {
        EasyMutiRoutingDataSource easyMutiRoutingDataSource = new EasyMutiRoutingDataSource();
        AbstractDataSourceConfig config = DataSourceConfigFactory.getDataSourceConfig(propertys);
        Map<Object, DataSource> dataSourceMap = config.dataSource();
        assert dataSourceMap != null;
        Map<Object, Object> tds = new HashMap<>(dataSourceMap);
        easyMutiRoutingDataSource.setTargetDataSources(tds);
        easyMutiRoutingDataSource.afterPropertiesSet();
        easyMutiRoutingDataSource.setDefaultDataSource(config.defaultDataSource());
        return easyMutiRoutingDataSource;
    }

    /**
     * 不能使用mybatis-plus的自动配置了，所以只能自己写
     * 因为排除了：@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
     *
     * @param dataSource
     * @param properties
     * @return
     * @throws Exception
     */
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(@Autowired DataSource dataSource, @Autowired MybatisPlusProperties properties) throws Exception {
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        MybatisConfiguration configuration = properties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(properties.getConfigLocation())) {
            configuration = new MybatisConfiguration();
        }
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        factory.setConfiguration(configuration);
        if (properties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(properties.getConfigurationProperties());
        }
        if (StringUtils.hasLength(properties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(properties.getTypeAliasesPackage());
        }
        if (StringUtils.hasLength(properties.getTypeEnumsPackage())) {
            factory.setTypeEnumsPackage(properties.getTypeEnumsPackage());
        }
        if (StringUtils.hasLength(properties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(properties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(properties.resolveMapperLocations())) {
            factory.setMapperLocations(properties.resolveMapperLocations());
        }
        if (!ObjectUtils.isEmpty(properties.getGlobalConfig())) {
            // 框架默认不提供主键自动生成策略，使用数据库的自动生成策略
            properties.getGlobalConfig().setIdType(IdType.AUTO.getKey());
            factory.setGlobalConfig(properties.getGlobalConfig().convertGlobalConfiguration());
        }
        return factory.getObject();
    }

    /**
     * 事务管理者
     *
     * @param easyMutiRoutingDataSource
     * @return
     */
    @Bean
    public PlatformTransactionManager mysqlPlatformTransactionManager(@Autowired EasyMutiRoutingDataSource easyMutiRoutingDataSource) {
        return new DataSourceTransactionManager(easyMutiRoutingDataSource);
    }


    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(@Autowired SqlSessionFactory sqlSessionFactor) {
        return new SqlSessionTemplate(sqlSessionFactor);
    }

}
