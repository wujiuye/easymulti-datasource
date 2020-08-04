package com.github.wujiuye.datasource.tx;

import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * 事务方法元数据信息
 *
 * @author wujiuye 2020/08/04
 */
public class TxMethodMetadata {

    private Method method;
    private Transactional transactional;
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

    public Transactional getTransactional() {
        return transactional;
    }

}
