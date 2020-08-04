package com.github.wujiuye.datasource.tx;

import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * 事务调用链路上下文
 *
 * @author wujiuye 2020/08/04
 */
public class TransactionInvokeContext {

    private final static ThreadLocal<TransactionInvokeChain> CONTEXT = new ThreadLocal<>();

    private static TransactionInvokeChain ensureChain() {
        TransactionInvokeChain chain = CONTEXT.get();
        if (chain == null) {
            CONTEXT.set(new TransactionInvokeChain());
        }
        return CONTEXT.get();
    }

    static void push(Class<?> tagClass, Method method) {
        Transactional transactional = method.getAnnotation(Transactional.class);
        if (transactional == null) {
            transactional = tagClass.getAnnotation(Transactional.class);
            if (transactional == null) {
                return;
            }
        }
        TransactionInvokeChain chain = ensureChain();
        TxMethodMetadata methodMetadata = new TxMethodMetadata();
        methodMetadata.setMethod(method);
        methodMetadata.setTransactional(transactional);
        chain.push(methodMetadata);
    }

    static void setThrowable(Throwable throwable) {
        TransactionInvokeChain chain = ensureChain();
        chain.setThrowable(throwable);
    }

    /**
     * 当前是否存在事务
     *
     * @return
     */
    public static boolean currentExistTransaction() {
        TransactionInvokeChain chain = CONTEXT.get();
        if (chain == null) {
            return false;
        }
        return chain.peek() != null;
    }

    /**
     * 注解当前事务退出监听器
     *
     * @param listener 事务退出监听器
     */
    public static void addCurrentTransactionMethodPopListener(PopTransactionListener listener) {
        TransactionInvokeChain chain = ensureChain();
        chain.registPopListenerToCurrent(listener);
    }

    static void pop() {
        TransactionInvokeChain chain = CONTEXT.get();
        if (chain == null) {
            return;
        }
        chain.pop();
        if (chain.methodCount() < 1) {
            CONTEXT.remove();
        }
    }

}
