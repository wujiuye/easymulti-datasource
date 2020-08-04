package com.github.wujiuye.datasource.tx;

import com.github.wujiuye.datasource.config.DataSourceConfig;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.transaction.PlatformTransactionManager;
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
    @Resource(name = "easyMutiPlatformTransactionManager")
    private PlatformTransactionManager platformTransactionManager;

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
        advisor.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return advisor;
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public TransactionInterceptor easyMutiTransactionInterceptor() {
        TransactionInterceptor interceptor = new TransactionInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                if (!transactionProps.isOpenChain()) {
                    return super.invoke(invocation);
                }
                Class<?> tagClass = invocation.getThis().getClass();
                TransactionInvokeContext.push(tagClass, invocation.getMethod());
                try {
                    return super.invoke(invocation);
                } catch (Throwable throwable) {
                    TransactionInvokeContext.setThrowable(throwable);
                    throw throwable;
                } finally {
                    TransactionInvokeContext.pop();
                }
            }
        };
        interceptor.setTransactionManager(platformTransactionManager);
        interceptor.setTransactionAttributeSource(transactionAttributeSource());
        return interceptor;
    }

}
