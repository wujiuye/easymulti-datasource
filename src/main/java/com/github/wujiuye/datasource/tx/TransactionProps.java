package com.github.wujiuye.datasource.tx;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 事务配置
 *
 * @author wujiuye 2020/08/04
 */
@Component
@ConfigurationProperties(prefix = "easymuti.transaction")
public class TransactionProps {

    /**
     * 是否开启事务方法调用链路监听
     */
    private boolean openChain;

    public boolean isOpenChain() {
        return openChain;
    }

    public void setOpenChain(boolean openChain) {
        this.openChain = openChain;
    }

}
