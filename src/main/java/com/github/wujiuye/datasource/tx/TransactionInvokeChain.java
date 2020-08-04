package com.github.wujiuye.datasource.tx;

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

    public void registPopListenerToCurrent(PopTransactionListener listener) {
        if (last != null && listener != null) {
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
