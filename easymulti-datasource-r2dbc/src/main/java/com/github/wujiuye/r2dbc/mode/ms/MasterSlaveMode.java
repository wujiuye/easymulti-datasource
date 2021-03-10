package com.github.wujiuye.r2dbc.mode.ms;

/**
 * @author wujiuye 2020/11/03
 */
public interface MasterSlaveMode {
    /**
     * 主节点
     */
    String Master = "masterDataBase";
    /**
     * 从节点
     */
    String Slave = "slaveDataBase";
}
