package com.github.wujiuye.datasource.sqlwatcher.plugin;

import com.github.wujiuye.datasource.sqlwatcher.CommandType;
import com.github.wujiuye.datasource.sqlwatcher.MatchItem;
import com.github.wujiuye.datasource.sqlwatcher.WatchMetadata;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 匹配结果
 *
 * @author wujiuye 2020/07/10
 */
class MatchResult {

    /**
     * SQL类型
     */
    private CommandType commandType;
    /**
     * 匹配的监听器对应匹配的字段
     */
    private Map<WatchMetadata, Set<String>> matchFields;
    /**
     * 匹配的WatchMetadata总数
     */
    private int count;
    /**
     * where条件，更新和删除有
     */
    private Expression where;
    /**
     * 插入与更新的字段
     */
    private List<Column> modifyColums;
    /**
     * 插入与更新的字段对应的值
     */
    private List<Expression> modifyParams;

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public Map<WatchMetadata, Set<String>> getMatchFields() {
        return matchFields;
    }

    public void setMatchFields(Map<WatchMetadata, Set<String>> matchFields) {
        this.matchFields = matchFields;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    /**
     * 转为MatchItem
     *
     * @return
     */
    public List<MatchItem> toMatchItems() {
        return this.getMatchFields().entrySet().parallelStream()
                .map(entry -> {
                    MatchItem matchItem = new MatchItem();
                    matchItem.setTable(entry.getKey().getTable());
                    matchItem.setFields(entry.getValue());
                    matchItem.setWhere(this.getWhere());
                    matchItem.setModifyColums(this.getModifyColums());
                    matchItem.setModifyParams(this.getModifyParams());
                    return matchItem;
                }).collect(Collectors.toList());
    }

}
