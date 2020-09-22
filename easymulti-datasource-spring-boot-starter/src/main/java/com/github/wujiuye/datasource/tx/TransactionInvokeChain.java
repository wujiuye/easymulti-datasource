package com.github.wujiuye.datasource.tx;

import org.springframework.transaction.annotation.Propagation;

import java.util.ArrayList;
import java.util.List;

/**
 * 事务方法调用链路
 *
 * @author wujiuye 2020/08/04
 */
class TransactionInvokeChain {

    private MethodMetadataWarp first;
    private MethodMetadataWarp last;

    private void setFirst(TxMethodMetadata first) {
        this.first = new MethodMetadataWarp(first);
        this.last = this.first;
    }

    public void push(TxMethodMetadata methodInfo) {
        if (first == null) {
            setFirst(methodInfo);
            return;
        }
        MethodMetadataWarp ptr = this.last;
        MethodMetadataWarp cur = new MethodMetadataWarp(methodInfo);
        if (ptr != null) {
            cur.pre = ptr;
            ptr.next = cur;
            this.last = cur;
        }
    }

    public TxMethodMetadata peek() {
        if (last != null) {
            return last.methodMetadata;
        }
        return null;
    }

    public void pop() {
        MethodMetadataWarp popPtr;
        if (last != null) {
            if (last.pre != null) {
                popPtr = last;
                last.pre.next = null;
                last = last.pre;
            } else {
                popPtr = last;
                last = null;
                first = null;
            }
            if (popPtr.getListeners() != null) {
                for (PopTransactionListener listener : popPtr.getListeners()) {
                    listener.onTransactionPop(popPtr.methodMetadata);
                }
            }
        }
    }

    public void setThrowable(Throwable throwable) {
        if (last != null && throwable != null) {
            last.methodMetadata.setThrowable(throwable);
        }
    }

    /**
     * 判断是否允许将监听器添加到{preTxPropagation}所在事务方法上
     *
     * @param preTxPropagation 前置{n}的事务方法，例如：当前事务方法的前一个事务方法、当前事务方法的前前一个事务方法、当前事务方法的前前前一个事务方法
     * @param curTxPropagation 当前事务方法的
     * @return
     */
    private static boolean allowAddPopListenerPropagation(Propagation preTxPropagation, Propagation curTxPropagation) {
        if (curTxPropagation != null) {
            switch (curTxPropagation) {
                // 新建事务，如果当前存在事务，把当前事务挂起。
                case REQUIRES_NEW:
                    // 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
                case NOT_SUPPORTED:
                    return true;
                // 支持当前事务，如果当前没有事务，则新建一个事务，默认。
                case REQUIRED:
                    // 以非事务方式执行，如果当前存在事务，则抛出异常。
                case NEVER:
                    // 加入当前事务，如果当前没有事务，则以非事务方式执行。
                case SUPPORTS:
                    return preTxPropagation == null;
                // Nested的事务和它的父事务是相依的，它的提交是要等和它的父事务一块提交的。
                case NESTED:
                    return false;
                // 支持当前事务，如果当前没有事务，则抛出异常。
                case MANDATORY:
                    return false;
                default:
            }
        }
        return false;
    }

    /**
     * 注册事务监听器到当前调用链路上的合适的事务方法上
     *
     * @param listener 事务方法执行结束监听器
     */
    public void registPopListenerToCurrent(PopTransactionListener listener) {
        if (last != null && listener != null) {
            MethodMetadataWarp ptr = last;
            MethodMetadataWarp prePtr = last.pre;
            // 当前事务
            Propagation curTransactional = last.getMethodMetadata().getPropagation();
            // 找到合适的前置事务添加监听器
            while (ptr != null) {
                Propagation preTransactional = prePtr == null ? null : prePtr.getMethodMetadata().getPropagation();
                if (allowAddPopListenerPropagation(preTransactional, curTransactional)) {
                    ptr.addListener(listener);
                    return;
                }
                ptr = last.pre;
                prePtr = ptr == null ? null : ptr.pre;
            }
            // 没有办法就加上吧
            last.addListener(listener);
        }
    }

    public int methodCount() {
        int count = 0;
        if (first == null) {
            return count;
        }
        MethodMetadataWarp ptr = first;
        count++;
        while (ptr != last) {
            count++;
            ptr = ptr.next;
        }
        return count;
    }

    static class MethodMetadataWarp {
        private TxMethodMetadata methodMetadata;
        private List<PopTransactionListener> listeners;
        private MethodMetadataWarp pre;
        private MethodMetadataWarp next;

        public MethodMetadataWarp(TxMethodMetadata methodMetadata) {
            this.methodMetadata = methodMetadata;
        }

        public TxMethodMetadata getMethodMetadata() {
            return methodMetadata;
        }

        public void addListener(PopTransactionListener listener) {
            if (listener == null) {
                return;
            }
            if (listeners == null) {
                listeners = new ArrayList<>();
            }
            listeners.add(listener);
        }

        public List<PopTransactionListener> getListeners() {
            return listeners;
        }

        public MethodMetadataWarp getNext() {
            return next;
        }

        public MethodMetadataWarp getPre() {
            return pre;
        }
    }

}
