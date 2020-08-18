package com.github.wujiuye.datasource.sqlwatcher;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * sql监听配置
 *
 * @author wujiuye 2020/08/18
 */
@Component
@ConditionalOnProperty(value = "easymuti.sql-watcher.enable", havingValue = "true")
@ConfigurationProperties(prefix = "easymuti.sql-watcher")
public class SqlWatcherProps {

    /**
     * 是否启用sql监听埋点功能
     */
    private Boolean enable;
    /**
     * 是否输出调用链上的sql
     */
    private Boolean showRealLogInvokeLink;

    public Boolean getShowRealLogInvokeLink() {
        return showRealLogInvokeLink;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public void setShowRealLogInvokeLink(Boolean showRealLogInvokeLink) {
        this.showRealLogInvokeLink = showRealLogInvokeLink;
    }

}
