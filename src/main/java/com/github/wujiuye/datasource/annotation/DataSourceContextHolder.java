package com.github.wujiuye.datasource.annotation;

/**
 * @author wujiuye
 * @version 1.0 on 2019/6/29 {描述：}
 */
public final class DataSourceContextHolder {

    /**
     * 保存当前线程使用的数据源
     */
    private static final ThreadLocal<EasyMutiDataSource.MultipleDataSource> multipleDataSourceThreadLocal = new ThreadLocal<>();

    /**
     * 设置数据源
     *
     * @param multipleDataSource
     */
    public static void setDataSource(EasyMutiDataSource.MultipleDataSource multipleDataSource) {
        multipleDataSourceThreadLocal.set(multipleDataSource);
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public static EasyMutiDataSource.MultipleDataSource getDataSource() {
        return multipleDataSourceThreadLocal.get();
    }

    /**
     * 清除数据源
     */
    public static void clearDataSource() {
        multipleDataSourceThreadLocal.remove();
    }

}
