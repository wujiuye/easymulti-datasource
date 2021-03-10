package com.github.wujiuye.datasource.annotation;

/**
 * @author wujiuye
 * @version 1.0 on 2019/6/29 {描述：}
 */
public final class DataSourceContextHolder {

    /**
     * 保存当前线程使用的数据源
     */
    private static final ThreadLocal<DataSourceSwitchStack> multipleDataSourceThreadLocal = new ThreadLocal<>();

    /**
     * 设置数据源
     *
     * @param multipleDataSource
     */
    public static void setDataSource(EasyMutiDataSource.MultipleDataSource multipleDataSource) {
        DataSourceSwitchStack switchStack = multipleDataSourceThreadLocal.get();
        if (switchStack == null) {
            switchStack = new DataSourceSwitchStack();
            multipleDataSourceThreadLocal.set(switchStack);
        }
        switchStack.push(multipleDataSource);
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public static EasyMutiDataSource.MultipleDataSource getDataSource() {
        if (multipleDataSourceThreadLocal.get() == null) {
            return null;
        }
        return multipleDataSourceThreadLocal.get().peek();
    }

    /**
     * 清除数据源
     */
    public static void clearDataSource() {
        DataSourceSwitchStack switchStack = multipleDataSourceThreadLocal.get();
        if (switchStack == null) {
            return;
        }
        switchStack.pop();
        if (switchStack.size() == 0) {
            multipleDataSourceThreadLocal.remove();
        }
    }

}
