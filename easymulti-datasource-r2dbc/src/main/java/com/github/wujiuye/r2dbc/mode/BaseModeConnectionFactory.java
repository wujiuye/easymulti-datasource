package com.github.wujiuye.r2dbc.mode;

import com.github.wujiuye.r2dbc.config.ConnectionConfig;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

/**
 * @author wujiuye 2020/11/03
 */
public abstract class BaseModeConnectionFactory {

    private final Map<String, ConnectionConfig> configMap;
    private final String defaultDataBase;

    public BaseModeConnectionFactory(Map<String, ConnectionConfig> configMap, String defaultDataBase) {
        this.configMap = configMap;
        this.defaultDataBase = defaultDataBase;
    }

    private ConnectionFactory createConnectionFactory(ConnectionConfig connectionConfig) {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.parse(connectionConfig.getUrl());
        options = options.mutate().option(USER, connectionConfig.getUsername())
                .option(PASSWORD, connectionConfig.getPassword())
                .option(CONNECT_TIMEOUT, Duration.ofSeconds(5))
                .build();
        ConnectionFactory connectionFactory = ConnectionFactories.get(options);
        if (connectionConfig.getPool() == null) {
            return connectionFactory;
        }
        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
                .maxIdleTime(Duration.ofMillis(connectionConfig.getPool().getIdelTimeout() * 1000))
                .maxSize(connectionConfig.getPool().getMaxSize())
                .validationQuery(connectionConfig.getPool().getValidationQuery())
                .initialSize(connectionConfig.getPool().getInitialSize())
                .build();
        return new ConnectionPool(configuration);
    }

    public String getDefaultDataBase() {
        return defaultDataBase;
    }

    public Map<String, ConnectionFactory> build() {
        Map<String, ConnectionFactory> connectionFactoryMap = new HashMap<>();
        for (Map.Entry<String, ConnectionConfig> entry : configMap.entrySet()) {
            connectionFactoryMap.put(entry.getKey(), createConnectionFactory(entry.getValue()));
        }
        return connectionFactoryMap;
    }

}
