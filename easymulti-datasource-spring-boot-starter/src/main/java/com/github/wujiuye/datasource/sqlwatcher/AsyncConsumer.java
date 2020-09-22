package com.github.wujiuye.datasource.sqlwatcher;

/**
 * 异步消费
 *
 * @author wujiuye 2020/08/03
 */
public interface AsyncConsumer {

    /**
     * 在sql执行完成时被回调
     *
     * @param throwable 当抛出异常时不为null
     */
    void complete(Throwable throwable);

}
