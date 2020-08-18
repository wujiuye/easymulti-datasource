package com.github.wujiuye.datasource.plus;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Mybatisplus配置
 *
 * @author wujiuye 2020/03/15
 */
@Configuration
@ConditionalOnBean(DataSource.class)
public class MybatisplusConfig {

    /**
     * 要使用分页查询功能，就需要配置分页拦截器
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}