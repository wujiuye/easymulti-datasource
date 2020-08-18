package com.github.wujiuye.datasource.sqlwatcher;

/**
 * @author wujiuye 2020/08/07
 */
public class AsyncFuture<T> {

    private T result;
    private Throwable exception;

    public AsyncFuture() {
    }

    synchronized void complete(T result) {
        this.result = result;
        this.notifyAll();
    }

    synchronized void setException(Throwable exception) {
        this.exception = exception;
        this.notifyAll();
    }

    public synchronized T get() throws Throwable {
        if (result == null && exception == null) {
            this.wait();
        }
        if (this.exception != null) {
            throw this.exception;
        }
        return this.result;
    }

}
