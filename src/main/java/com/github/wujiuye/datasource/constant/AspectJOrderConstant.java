package com.github.wujiuye.datasource.constant;

/**
 * 约定切面排序
 *
 * @author wujiuye 2020/08/10
 */
public interface AspectJOrderConstant {

    /**
     * 数据源切换切面order
     */
    int DATA_SOUCE_ASPECT_ORDER = 0;

    /**
     * 事务切换切面order
     */
    int TRANSACTION_ASPECT_ORDER = DATA_SOUCE_ASPECT_ORDER - 1;

}
