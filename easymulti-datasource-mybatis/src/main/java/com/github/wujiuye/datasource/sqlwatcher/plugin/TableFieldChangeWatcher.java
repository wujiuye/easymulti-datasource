package com.github.wujiuye.datasource.sqlwatcher.plugin;

import com.github.wujiuye.datasource.sqlwatcher.WatchMetadata;

import java.util.Set;

/**
 * 表字段改变监听器
 *
 * @author wujiuye 2020/07/10
 */
interface TableFieldChangeWatcher {

    /**
     * 获取监听配置
     *
     * @return
     */
    Set<WatchMetadata> getSetting();

    /**
     * 在监听到sql时被回调执行，但sql并未执行
     *
     * @param transactionId 事务ID
     * @param matchResult   匹配结果
     * @return
     */
    void watchStart(String transactionId, MatchResult matchResult);

    /**
     * 原sql执行成功时被调用
     * （在SQL执行完成且没有抛出异常的情况下才会回调）
     *
     * @param transactionId 事务ID
     */
    void watchSuccess(String transactionId);

    /**
     * 原sql执行失败时被调用
     *
     * @param transactionId 事务ID
     * @param e             抛出的异常
     */
    void watchFail(String transactionId, Throwable e);

}
