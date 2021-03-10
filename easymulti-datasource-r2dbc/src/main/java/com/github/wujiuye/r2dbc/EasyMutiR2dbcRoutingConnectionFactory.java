package com.github.wujiuye.r2dbc;

import com.github.wujiuye.r2dbc.mode.BaseModeConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.data.r2dbc.connectionfactory.lookup.AbstractRoutingConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * ConnectionFactory路由
 *
 * @author wujiuye 2020/11/03
 */
public class EasyMutiR2dbcRoutingConnectionFactory extends AbstractRoutingConnectionFactory {

    private final static String DB_KEY = "HOTKIT-R2DBC-DB";

    public EasyMutiR2dbcRoutingConnectionFactory(BaseModeConnectionFactory modeConnectionFactory) {
        Map<String, ConnectionFactory> connectionFactoryMap = modeConnectionFactory.build();
        setTargetConnectionFactories(connectionFactoryMap);
        ConnectionFactory defaultTarget = connectionFactoryMap.get(modeConnectionFactory.getDefaultDataBase());
        setDefaultTargetConnectionFactory(defaultTarget);
    }

    public static <T> Mono<T> putDataSource(Mono<T> mono, String dataSource) {
        return mono.subscriberContext(context -> context.put(DB_KEY, dataSource));
    }

    public static <T> Flux<T> putDataSource(Flux<T> flux, String dataSource) {
        return flux.subscriberContext(context -> context.put(DB_KEY, dataSource));
    }

    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        return Mono.subscriberContext().handle((context, sink) -> {
            if (context.hasKey(DB_KEY)) {
                sink.next(context.get(DB_KEY));
            }
        });
    }

}
