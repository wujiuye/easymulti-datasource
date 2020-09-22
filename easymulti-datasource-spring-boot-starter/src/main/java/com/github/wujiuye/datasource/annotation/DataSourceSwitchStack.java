package com.github.wujiuye.datasource.annotation;

import java.util.Arrays;

/**
 * 解决在同一个线程下数据源多次切换的回溯问题
 *
 * @author wujiuye 2020/04/24
 */
class DataSourceSwitchStack {

    private EasyMutiDataSource.MultipleDataSource[] stack;
    private int topIndex;
    private int leng = 2;

    public DataSourceSwitchStack() {
        stack = new EasyMutiDataSource.MultipleDataSource[leng];
        topIndex = -1;
    }

    public void push(EasyMutiDataSource.MultipleDataSource source) {
        if (topIndex + 1 == leng) {
            leng *= 2;
            stack = Arrays.copyOf(stack, leng);
        }
        this.stack[++topIndex] = source;
    }

    public EasyMutiDataSource.MultipleDataSource peek() {
        return stack[topIndex];
    }

    public EasyMutiDataSource.MultipleDataSource pop() {
        return stack[topIndex--];
    }

    public int size() {
        return topIndex + 1;
    }

}