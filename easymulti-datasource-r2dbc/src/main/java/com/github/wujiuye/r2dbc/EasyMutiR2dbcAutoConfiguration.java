package com.github.wujiuye.r2dbc;

import com.github.wujiuye.r2dbc.mode.cluster.ClusterModeConfig;
import com.github.wujiuye.r2dbc.mode.ms.MasterSlaveConnectionFactory;
import com.github.wujiuye.r2dbc.mode.ms.MasterSlaveModeConfig;
import com.github.wujiuye.r2dbc.mode.BaseModeConnectionFactory;
import com.github.wujiuye.r2dbc.mode.cluster.ClusterConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;

import javax.annotation.Resource;

/**
 * @author wujiuye 2020/11/03
 */
@Import(R2dbcDataBaseConfig.class)
@Configuration
public class EasyMutiR2dbcAutoConfiguration extends AbstractR2dbcConfiguration {

    @Resource
    private R2dbcDataBaseConfig r2dbcDataBaseConfig;

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        return new EasyMutiR2dbcRoutingConnectionFactory(createBaseModeConnectionFactory());
    }

    @Bean
    public ReactiveTransactionManager reactiveTransactionManager(@Autowired ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    private BaseModeConnectionFactory createBaseModeConnectionFactory() {
        if (this.r2dbcDataBaseConfig.getMasterSlaveMode() != null) {
            MasterSlaveModeConfig masterSlaveMode = this.r2dbcDataBaseConfig.getMasterSlaveMode();
            return new MasterSlaveConnectionFactory(masterSlaveMode);
        }
        if (this.r2dbcDataBaseConfig.getClusterMode() != null) {
            ClusterModeConfig clusterMode = this.r2dbcDataBaseConfig.getClusterMode();
            return new ClusterConnectionFactory(clusterMode);
        }
        throw new NullPointerException("请选择一种模式配置数据源！");
    }

}
