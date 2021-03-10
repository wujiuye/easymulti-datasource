package com.github.wujiuye.r2dbc.mode.ms;

import com.github.wujiuye.r2dbc.config.ConnectionConfig;

/**
 * @author wujiuye 2020/11/03
 */
public class MasterSlaveModeConfig {

    private ConnectionConfig master;
    private ConnectionConfig slave;

    public ConnectionConfig getMaster() {
        return master;
    }

    public ConnectionConfig getSlave() {
        return slave;
    }

    public void setMaster(ConnectionConfig master) {
        this.master = master;
    }

    public void setSlave(ConnectionConfig slave) {
        this.slave = slave;
    }

}
