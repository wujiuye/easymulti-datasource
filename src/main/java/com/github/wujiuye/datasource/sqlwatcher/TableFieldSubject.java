package com.github.wujiuye.datasource.sqlwatcher;

import com.github.wujiuye.datasource.tx.TransactionInvokeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 默认表字段改变观察者
 *
 * @author wujiuye 2020/07/10
 */
class TableFieldSubject implements TableFieldChangeWatcher, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TableFieldSubject.class);

    /**
     * 延迟消费事件线程池
     */
    @Autowired
    private AsyncTaskExecutor executor;

    /**
     * 观察者
     */
    @Autowired(required = false)
    private Set<TableFieldObserver> observers;

    private Set<WatchMetadata> watchMetadataSet = new HashSet<>();
    private Map<String, Set<TableFieldObserver>> watchObserverMap = new HashMap<>();

    private final static ConcurrentMap<String, AsyncFuture<List<AsyncConsumer>>> futureMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (!CollectionUtils.isEmpty(observers)) {
            for (TableFieldObserver observer : observers) {
                Set<WatchMetadata> watchMetadatas = observer.observeMetadatas();
                watchMetadataSet.addAll(watchMetadatas);
                if (CollectionUtils.isEmpty(watchMetadatas)) {
                    continue;
                }
                for (WatchMetadata metadata : watchMetadatas) {
                    Set<TableFieldObserver> os = watchObserverMap.computeIfAbsent(metadata.getTable(), key -> new HashSet<>());
                    os.add(observer);
                }
            }
        }
    }

    @Override
    public Set<WatchMetadata> getSetting() {
        return watchMetadataSet;
    }

    @Override
    public void watchStart(final String transactionId, MatchResult matchResult) {
        if (matchResult.getCount() == 0) {
            return;
        }
        Map<WatchMetadata, Set<String>> watchMetadataMap = matchResult.getMatchFields();
        if (CollectionUtils.isEmpty(watchMetadataMap)) {
            return;
        }
        futureMap.put(transactionId, new AsyncFuture<>());
        List<AsyncConsumer> consumers = new ArrayList<>();
        try {
            for (MatchItem matchItem : matchResult.toMatchItems()) {
                for (TableFieldObserver observer : watchObserverMap.get(matchItem.getTable())) {
                    AsyncConsumer consumer = observer.observe(matchResult.getCommandType(), matchItem);
                    if (consumer == null) {
                        continue;
                    }
                    consumers.add(consumer);
                }
            }
        } finally {
            AsyncFuture<List<AsyncConsumer>> future = futureMap.get(transactionId);
            future.complete(consumers);
        }
        // 存储事务，则事务方法完成之后再提交
        if (TransactionInvokeContext.currentExistTransaction()) {
            // 添加事务监听器
            TransactionInvokeContext.addCurrentTransactionMethodPopListener(methodInfo -> {
                if (methodInfo.isRollback()) {
                    logger.info("tx method name={}, error class={}, 事务回滚了！", methodInfo.getMethod().getName(), methodInfo.getThrowable().getClass());
                    return;
                }
                complete(transactionId, methodInfo.getThrowable());
            });
        }
    }

    @Override
    public void watchSuccess(final String transactionId) {
        if (TransactionInvokeContext.currentExistTransaction()) {
            return;
        }
        this.complete(transactionId, null);
    }

    @Override
    public void watchFail(final String transactionId, Throwable e) {
        if (TransactionInvokeContext.currentExistTransaction()) {
            return;
        }
        this.complete(transactionId, e);
    }

    private void complete(final String transactionId, Throwable throwable) {
        AsyncFuture<List<AsyncConsumer>> future = futureMap.get(transactionId);
        if (future == null) {
            return;
        }
        executor.execute(() -> {
            try {
                List<AsyncConsumer> consumers = future.get();
                for (AsyncConsumer consumer : consumers) {
                    consumer.complete(throwable);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                futureMap.remove(transactionId);
            }
        });
    }

}
