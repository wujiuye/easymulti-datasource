package com.github.wujiuye.datasource;

import com.github.wujiuye.datasource.annotation.EasyMutiDataSourceAspect;
import com.github.wujiuye.datasource.config.DataSourceConfig;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 注册bean
 *
 * @author wujiuye
 * @version 1.0 on 2020/03/15
 */
public class DataSourceAutoConfigRegistrar implements ImportBeanDefinitionRegistrar {

    private void registerDataSourceAnnotationAutoConfigIfNecessary(BeanDefinitionRegistry registry) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(DataSourceConfig.class);
        registry.registerBeanDefinition("com.github.wujiuye.datasource.DataSourceConfig", beanDefinition);
    }

    private void registerDataSourceAspectIfNecessary(BeanDefinitionRegistry registry) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(EasyMutiDataSourceAspect.class);
        registry.registerBeanDefinition("com.github.wujiuye.datasource.annotation.EasyMutiDataSourceAspect", beanDefinition);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        try {
            registerDataSourceAnnotationAutoConfigIfNecessary(beanDefinitionRegistry);
            registerDataSourceAspectIfNecessary(beanDefinitionRegistry);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

}
