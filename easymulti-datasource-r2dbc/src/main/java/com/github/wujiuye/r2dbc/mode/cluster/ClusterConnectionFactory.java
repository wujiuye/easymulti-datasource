package com.github.wujiuye.r2dbc.mode.cluster;

import com.github.wujiuye.r2dbc.config.ConnectionConfig;
import com.github.wujiuye.r2dbc.mode.BaseModeConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wujiuye 2020/11/03
 */
public class ClusterConnectionFactory extends BaseModeConnectionFactory {

    public ClusterConnectionFactory(ClusterModeConfig clusterMode) {
        super(toMapDataBaseConfig(clusterMode), ClusterMode.First);
    }

    private static Map<String, ConnectionConfig> toMapDataBaseConfig(ClusterModeConfig clusterMode) {
        Map<String, ConnectionConfig> configMap = new HashMap<>();
        if (clusterMode.getFirst() != null) {
            configMap.put(ClusterMode.First, clusterMode.getFirst());
        }
        if (clusterMode.getSecond() != null) {
            configMap.put(ClusterMode.Second, clusterMode.getSecond());
        }
        if (clusterMode.getThird() != null) {
            configMap.put(ClusterMode.Third, clusterMode.getThird());
        }
        if (configMap.isEmpty()) {
            throw new NullPointerException("未配置任何数据源！");
        }
        return configMap;
    }

}
