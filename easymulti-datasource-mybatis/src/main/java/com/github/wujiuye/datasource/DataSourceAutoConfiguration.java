package com.github.wujiuye.datasource;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.github.wujiuye.datasource.config.DataSourcePropertys;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 数据源自动配置starter
 * 注意：
 * ⚠ ️需要排除其它的start自动配置：@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
 *
 * @author wujiuye
 * @version 1.0 on 2020/03/15
 */
@Configuration
@EnableConfigurationProperties(DataSourcePropertys.class)
@Import({DataSourceAutoConfigRegistrar.class})
public class DataSourceAutoConfiguration {

    /**
     * 分页查询插件
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
