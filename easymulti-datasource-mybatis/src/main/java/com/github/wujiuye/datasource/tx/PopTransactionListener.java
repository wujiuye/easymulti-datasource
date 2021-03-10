package com.github.wujiuye.datasource.tx;

/**
 * 事件退出监听器
 *
 * @author wujiuye 2020/08/04
 */
@FunctionalInterface
public interface PopTransactionListener {

    /**
     * 事务退出时回调用
     *
     * @param methodInfo 事务方法
     */
    void onTransactionPop(TxMethodMetadata methodInfo);

}
