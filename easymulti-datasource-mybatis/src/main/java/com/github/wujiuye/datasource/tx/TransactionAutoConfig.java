package com.github.wujiuye.datasource.tx;

import com.github.wujiuye.datasource.config.DataSourceConfig;
import com.github.wujiuye.datasource.constant.AspectJOrderConstant;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.config.TransactionManagementConfigUtils;
import org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.annotation.Resource;

/**
 * 事务自动配置
 *
 * @author wujiuye 2020/08/04
 */
@Configuration
@ConditionalOnClass(Transactional.class)
@AutoConfigureAfter(DataSourceConfig.class)
@EnableConfigurationProperties(TransactionProps.class)
public class TransactionAutoConfig {

    @Resource
    private TransactionProps transactionProps;
    @Resource
    private TransactionManager transactionManager;

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public TransactionAttributeSource transactionAttributeSource() {
        return new AnnotationTransactionAttributeSource();
    }

    @Bean(name = TransactionManagementConfigUtils.TRANSACTION_ADVISOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryTransactionAttributeSourceAdvisor transactionAdvisor() {
        BeanFactoryTransactionAttributeSourceAdvisor advisor = new BeanFactoryTransactionAttributeSourceAdvisor();
        advisor.setTransactionAttributeSource(transactionAttributeSource());
        advisor.setAdvice(easyMutiTransactionInterceptor());
        // 必须要在切换数据源之后
        advisor.setOrder(AspectJOrderConstant.TRANSACTION_ASPECT_ORDER);
        return advisor;
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public TransactionInterceptor easyMutiTransactionInterceptor() {
        TransactionInterceptor interceptor = new TransactionInterceptorDelegate() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                return super.invokeChain(invocation, transactionProps.isOpenChain());
            }
        };
        interceptor.setTransactionManager(transactionManager);
        interceptor.setTransactionAttributeSource(transactionAttributeSource());
        return interceptor;
    }

}
