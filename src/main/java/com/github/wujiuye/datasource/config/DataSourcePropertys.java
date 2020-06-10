package com.github.wujiuye.datasource.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * yml配置
 *
 * @author wujiuye
 * @version 1.0 on 2020/03/15
 */
@Component
@ConfigurationProperties(prefix = "easymuti.datasource")
public class DataSourcePropertys {

    /**
     * 连接池通用配置
     */
    private ConnectionPool pool;

    /**
     * 默认使用哪个数据源，不配置则：
     * 1、主从==>默认使用主库
     * 2、1～10==>默认使用第一个库
     */
    private String defalutDataSource;

    /**
     * 主从模式请使用这两个
     */
    private MasterSlaveDataSource master;
    private MasterSlaveDataSource slave;

    /**
     * 非主从数据库请使用这种，最大支持10个数据源，使用多少个都可以，建议按顺序
     */
    private MasterSlaveDataSource first;
    private MasterSlaveDataSource second;
    private MasterSlaveDataSource third;
    private MasterSlaveDataSource fourth;
    private MasterSlaveDataSource fifth;
    private MasterSlaveDataSource sixth;
    private MasterSlaveDataSource seventh;
    private MasterSlaveDataSource eighth;
    private MasterSlaveDataSource ninth;
    private MasterSlaveDataSource tenth;


    public static class MasterSlaveDataSource {
        private String jdbcUrl;
        private String username;
        private String password;

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isEmpty() {
            return StringUtils.isEmpty(jdbcUrl)
                    || StringUtils.isEmpty(username);
        }

    }

    public static class ConnectionPool {

        /**
         * 是否使用连接池
         */
        boolean useConnPool;
        int maxPoolSize;
        long connectionTimeout;
        long maxLifetime;
        long idleTimeout;

        public boolean isUseConnPool() {
            return useConnPool;
        }

        public void setUseConnPool(boolean useConnPool) {
            this.useConnPool = useConnPool;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public long getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public long getMaxLifetime() {
            return maxLifetime;
        }

        public void setMaxLifetime(long maxLifetime) {
            this.maxLifetime = maxLifetime;
        }

        public long getIdleTimeout() {
            return idleTimeout;
        }

        public void setIdleTimeout(long idleTimeout) {
            this.idleTimeout = idleTimeout;
        }
    }

    public ConnectionPool getPool() {
        return pool;
    }

    public void setPool(ConnectionPool pool) {
        this.pool = pool;
    }

    public MasterSlaveDataSource getMaster() {
        return master;
    }

    public void setMaster(MasterSlaveDataSource master) {
        this.master = master;
    }

    public MasterSlaveDataSource getSlave() {
        return slave;
    }

    public void setSlave(MasterSlaveDataSource slave) {
        this.slave = slave;
    }

    public MasterSlaveDataSource getFirst() {
        return first;
    }

    public void setFirst(MasterSlaveDataSource first) {
        this.first = first;
    }

    public MasterSlaveDataSource getSecond() {
        return second;
    }

    public void setSecond(MasterSlaveDataSource second) {
        this.second = second;
    }

    public MasterSlaveDataSource getThird() {
        return third;
    }

    public void setThird(MasterSlaveDataSource third) {
        this.third = third;
    }

    public MasterSlaveDataSource getFourth() {
        return fourth;
    }

    public void setFourth(MasterSlaveDataSource fourth) {
        this.fourth = fourth;
    }

    public MasterSlaveDataSource getFifth() {
        return fifth;
    }

    public void setFifth(MasterSlaveDataSource fifth) {
        this.fifth = fifth;
    }

    public MasterSlaveDataSource getSixth() {
        return sixth;
    }

    public void setSixth(MasterSlaveDataSource sixth) {
        this.sixth = sixth;
    }

    public MasterSlaveDataSource getSeventh() {
        return seventh;
    }

    public void setSeventh(MasterSlaveDataSource seventh) {
        this.seventh = seventh;
    }

    public MasterSlaveDataSource getEighth() {
        return eighth;
    }

    public void setEighth(MasterSlaveDataSource eighth) {
        this.eighth = eighth;
    }

    public MasterSlaveDataSource getNinth() {
        return ninth;
    }

    public void setNinth(MasterSlaveDataSource ninth) {
        this.ninth = ninth;
    }

    public MasterSlaveDataSource getTenth() {
        return tenth;
    }

    public void setTenth(MasterSlaveDataSource tenth) {
        this.tenth = tenth;
    }

    public String getDefalutDataSource() {
        return defalutDataSource;
    }

    public void setDefalutDataSource(String defalutDataSource) {
        this.defalutDataSource = defalutDataSource;
    }

}
