package com.github.wujiuye.datasource.sqlwatcher;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;

import java.util.List;
import java.util.Set;

/**
 * 匹配结果Item
 *
 * @author wujiuye 2020/07/10
 */
public class MatchItem {

    /**
     * 监听的表名
     */
    private String table;
    /**
     * 匹配的字段
     */
    private Set<String> fields;
    /**
     * where条件，更新和删除有
     */
    private transient Expression where;
    /**
     * 插入与更新的字段
     */
    private transient List<Column> modifyColums;
    /**
     * 插入与更新的字段对应的值
     */
    private transient List<Expression> modifyParams;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Set<String> getFields() {
        return fields;
    }

    public void setFields(Set<String> fields) {
        this.fields = fields;
    }

    public Expression getWhere() {
        return where;
    }

    public void setWhere(Expression where) {
        this.where = where;
    }

    public List<Column> getModifyColums() {
        return modifyColums;
    }

    public void setModifyColums(List<Column> modifyColums) {
        this.modifyColums = modifyColums;
    }

    public List<Expression> getModifyParams() {
        return modifyParams;
    }

    public void setModifyParams(List<Expression> modifyParams) {
        this.modifyParams = modifyParams;
    }

    @Override
    public String toString() {
        return "MatchItem{" +
                "table='" + table + '\'' +
                ", fields=" + fields +
                ", where=" + where +
                ", modifyColums=" + modifyColums +
                ", modifyParams=" + modifyParams +
                '}';
    }

}
