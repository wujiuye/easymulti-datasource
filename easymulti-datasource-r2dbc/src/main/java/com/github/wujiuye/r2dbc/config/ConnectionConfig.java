package com.github.wujiuye.r2dbc.config;

/**
 * @author wujiuye 2020/11/03
 */
public class ConnectionConfig {

    private String url;
    private String username;
    private String password;
    /**
     * 连接池
     */
    private ConnectionPoolConfig pool;

    public void setPool(ConnectionPoolConfig pool) {
        this.pool = pool;
    }

    public ConnectionPoolConfig getPool() {
        return pool;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
