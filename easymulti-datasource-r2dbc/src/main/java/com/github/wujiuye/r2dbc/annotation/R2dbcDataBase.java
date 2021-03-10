package com.github.wujiuye.r2dbc.annotation;

import com.github.wujiuye.r2dbc.mode.cluster.ClusterMode;
import com.github.wujiuye.r2dbc.mode.ms.MasterSlaveMode;

import java.lang.annotation.*;

/**
 * 动态数据源切点
 *
 * @author wujiuye 2020/11/03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface R2dbcDataBase {

    /**
     * 指定DB的名称
     *
     * @return 选择的DB
     * @see MasterSlaveMode （当使用主从模式时）
     * @see ClusterMode （当使用集群模式时）
     */
    String value();

}
