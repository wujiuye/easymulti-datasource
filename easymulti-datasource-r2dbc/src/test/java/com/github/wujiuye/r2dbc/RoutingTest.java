package com.github.wujiuye.r2dbc;

import com.github.wujiuye.r2dbc.mode.ms.MasterSlaveMode;
import org.junit.Test;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

public class RoutingTest extends SupporSpringBootTest {

    @Resource
    private DatabaseClient client;
    @Resource
    private ReactiveTransactionManager reactiveTransactionManager;

    @Test
    public void test() throws InterruptedException {
        TransactionalOperator operator = TransactionalOperator.create(reactiveTransactionManager);
        Mono<Void> atomicOperation = client.execute("INSERT INTO person (id, name, age) VALUES(:id, :name, :age)")
                .bind("id", "joe")
                .bind("name", "Joe")
                .bind("age", 34)
                .fetch().rowsUpdated()
                .then(client.execute("INSERT INTO person (id, name) VALUES(:id, :name)")
                        .bind("id", "joe")
                        .bind("name", "Joe")
                        .fetch().rowsUpdated())
                .then();
        // 包装事务
        Mono<Void> txOperation = operator.transactional(atomicOperation);
        // 包装切换数据源
        EasyMutiR2dbcRoutingConnectionFactory.putDataSource(txOperation, MasterSlaveMode.Slave).subscribe();
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

}
