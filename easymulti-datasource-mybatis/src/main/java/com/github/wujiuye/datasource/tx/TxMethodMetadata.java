package com.github.wujiuye.datasource.tx;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.lang.reflect.Method;

/**
 * 事务方法元数据信息
 *
 * @author wujiuye 2020/08/04
 */
public class TxMethodMetadata {

    private Method method;
    private Transactional transactional;
    private TransactionAttribute transactionalAttribute;
    private Throwable throwable;

    public Method getMethod() {
        return method;
    }

    void setMethod(Method method) {
        this.method = method;
    }

    void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    void setTransactional(Transactional transactional) {
        this.transactional = transactional;
    }

    void setTransactionalAttribute(TransactionAttribute transactionalAttribute) {
        this.transactionalAttribute = transactionalAttribute;
    }

    /**
     * 获取事务方法的隔离级别
     *
     * @return
     */
    Propagation getPropagation() {
        Propagation propagationBehavior = Propagation.REQUIRES_NEW;
        // 有注解则优先从注解获取事务信息
        if (transactional != null) {
            propagationBehavior = transactional.propagation();
        } else if (transactionalAttribute != null) {
            for (Propagation propagation : Propagation.values()) {
                if (propagation.value() == transactionalAttribute.getPropagationBehavior()) {
                    propagationBehavior = propagation;
                }
            }
        }
        return propagationBehavior;
    }

    /**
     * 是否回滚了
     *
     * @return
     */
    public boolean isRollback() {
        // 判断是否事务回滚
        if (throwable != null) {
            if (transactional != null) {
                Class<?>[] roForClasss = transactional.rollbackFor();
                if (roForClasss.length == 0) {
                    return (throwable instanceof RuntimeException) || (throwable instanceof Error);
                }
                // 是否回滚的异常类型
                Class<?> current = throwable.getClass();
                for (Class<?> cls : roForClasss) {
                    if (cls.isAssignableFrom(current)) {
                        return true;
                    }
                }
            } else if (transactionalAttribute != null) {
                return transactionalAttribute.rollbackOn(throwable);
            }
        }
        return false;
    }

}
