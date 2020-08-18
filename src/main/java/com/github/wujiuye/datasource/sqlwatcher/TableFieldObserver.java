package com.github.wujiuye.datasource.sqlwatcher;

import java.util.HashSet;
import java.util.Set;

/**
 * 观察者
 *
 * @author wujiuye 2020/07/10
 */
public interface TableFieldObserver {

    /**
     * 注册监听事件
     *
     * @return
     */
    default WatchMetadata getObserveMetadata() {
        throw new NullPointerException("未注册事件！");
    }

    default Set<WatchMetadata> observeMetadatas() {
        Set<WatchMetadata> set = new HashSet<>();
        set.add(getObserveMetadata());
        return set;
    }

    /**
     * 观察到事件时被同步调用
     *
     * @param commandType 事件类型
     * @param matchResult 匹配的ITEM
     * @return 异步事件消费者，再sql执行完成时，或者在事务方法执行完成时（如果存在事务），完成指：正常执行完成 or 方法异常退出
     */
    AsyncConsumer observe(CommandType commandType, MatchItem matchResult);

}
