package com.github.wujiuye.r2dbc.mode.cluster;

import com.github.wujiuye.r2dbc.config.ConnectionConfig;

/**
 * @author wujiuye 2020/11/03
 */
public class ClusterModeConfig {

    private ConnectionConfig first;
    private ConnectionConfig second;
    private ConnectionConfig third;

    public ConnectionConfig getFirst() {
        return first;
    }

    public void setFirst(ConnectionConfig first) {
        this.first = first;
    }

    public ConnectionConfig getSecond() {
        return second;
    }

    public void setSecond(ConnectionConfig second) {
        this.second = second;
    }

    public ConnectionConfig getThird() {
        return third;
    }

    public void setThird(ConnectionConfig third) {
        this.third = third;
    }

}
