package com.github.wujiuye.datasource.tx;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * 事务拦截器委托
 *
 * @author wujiuye 2020/08/12
 */
public abstract class TransactionInterceptorDelegate extends TransactionInterceptor {

    /**
     * 追踪调用链路
     *
     * @param invocation
     * @param openChain
     * @return
     * @throws Throwable
     */
    protected Object invokeChain(MethodInvocation invocation, boolean openChain) throws Throwable {
        if (!openChain) {
            return super.invoke(invocation);
        }
        Class<?> tagClass = invocation.getThis().getClass();
        Transactional transactional = invocation.getMethod().getAnnotation(Transactional.class);
        TransactionAttribute attribute = null;
        if (transactional == null) {
            transactional = tagClass.getAnnotation(Transactional.class);
        }
        if (transactional == null) {
            TransactionAttributeSource attributeSource = getTransactionAttributeSource();
            if (attributeSource != null) {
                attribute = attributeSource.getTransactionAttribute(invocation.getMethod(), tagClass);
            }
        }
        TransactionInvokeContext.push(invocation.getMethod(), transactional, attribute);
        try {
            return super.invoke(invocation);
        } catch (Throwable throwable) {
            TransactionInvokeContext.setThrowable(throwable);
            throw throwable;
        } finally {
            TransactionInvokeContext.pop();
        }
    }

}
