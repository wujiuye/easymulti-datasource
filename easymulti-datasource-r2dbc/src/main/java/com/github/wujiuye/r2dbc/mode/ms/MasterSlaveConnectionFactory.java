package com.github.wujiuye.r2dbc.mode.ms;

import com.github.wujiuye.r2dbc.config.ConnectionConfig;
import com.github.wujiuye.r2dbc.mode.BaseModeConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wujiuye 2020/11/03
 */
public class MasterSlaveConnectionFactory extends BaseModeConnectionFactory {

    public MasterSlaveConnectionFactory(MasterSlaveModeConfig masterSlaveMode) {
        super(toMapDataBaseConfig(masterSlaveMode), MasterSlaveMode.Master);
    }

    private static Map<String, ConnectionConfig> toMapDataBaseConfig(MasterSlaveModeConfig masterSlaveMode) {
        Map<String, ConnectionConfig> configMap = new HashMap<>();
        if (masterSlaveMode.getMaster() == null) {
            throw new NullPointerException("主从模式的主数据源配置不能为空！");
        }
        configMap.put(MasterSlaveMode.Master, masterSlaveMode.getMaster());
        if (masterSlaveMode.getSlave() != null) {
            configMap.put(MasterSlaveMode.Slave, masterSlaveMode.getSlave());
        }
        return configMap;
    }

}
