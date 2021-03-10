package com.github.wujiuye.r2dbc;

import com.github.wujiuye.r2dbc.mode.cluster.ClusterModeConfig;
import com.github.wujiuye.r2dbc.mode.ms.MasterSlaveModeConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 数据源配置
 *
 * @author wujiuye 2020/11/03
 */
@Component
@ConfigurationProperties(prefix = "easymuti.datasource.r2dbc")
public class R2dbcDataBaseConfig {

    /**
     * 主从模式
     */
    private MasterSlaveModeConfig masterSlaveMode;
    /**
     * 集群模式
     */
    private ClusterModeConfig clusterMode;

    public ClusterModeConfig getClusterMode() {
        return clusterMode;
    }

    public void setMasterSlaveMode(MasterSlaveModeConfig masterSlaveMode) {
        this.masterSlaveMode = masterSlaveMode;
    }

    public void setClusterMode(ClusterModeConfig clusterMode) {
        this.clusterMode = clusterMode;
    }

    public MasterSlaveModeConfig getMasterSlaveMode() {
        return masterSlaveMode;
    }

}
