package com.github.wujiuye.datasource.tx;

import org.junit.Test;

public class TransactionInvokeChainTest {

    @Test
    public void test() {
        TransactionInvokeChain chain = new TransactionInvokeChain();
        for (int i = 0; i < 10; i++) {
            chain.push(new TxMethodMetadata());
            chain.registPopListenerToCurrent(methodInfo -> System.out.println("pop....."));
            System.out.println("count:" + chain.methodCount());
        }
        for (int i = 0; i < 11; i++) {
            chain.pop();
            System.out.println("count:" + chain.methodCount());
        }
    }

}
