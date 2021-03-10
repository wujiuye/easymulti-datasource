package com.github.wujiuye.datasource.sqlwatcher.plugin;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * sql监听配置
 *
 * @author wujiuye 2020/08/18
 */
@Component
@ConfigurationProperties(prefix = "easymuti.sql-watcher")
public class SqlWatcherProps {

    /**
     * 是否启用sql监听埋点功能
     */
    private boolean enable = false;
    /**
     * 是否输出调用链上的sql
     */
    private boolean showRealLogInvokeLink = false;

    public boolean isShowRealLogInvokeLink() {
        return showRealLogInvokeLink;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setShowRealLogInvokeLink(boolean showRealLogInvokeLink) {
        this.showRealLogInvokeLink = showRealLogInvokeLink;
    }

}
