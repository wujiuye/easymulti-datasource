package com.github.wujiuye.datasource.sqlwatcher.base;

import com.baomidou.mybatisplus.core.parser.AbstractJsqlParser;
import com.github.wujiuye.datasource.sqlwatcher.CommandType;
import com.github.wujiuye.datasource.sqlwatcher.WatchMetadata;
import com.github.wujiuye.datasource.sqlwatcher.base.MatchResult;
import com.github.wujiuye.datasource.sqlwatcher.base.TableFieldChangeWatcher;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitorAdapter;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.update.Update;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * SQL解析，判断是否是插入、更新、删除
 *
 * @author wujiuye 2020/07/10
 */
class ModifySqlParser extends AbstractJsqlParser {

    private final static String WATCH_METADATA_KEY = "watch_metadata";
    private final static String MATCH_RESULT_KEY = "match_result";

    private final static ThreadLocal<Map<String, Object>> MATCH_RESULT_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 解析插入语句
     *
     * @param insert
     */
    @Override
    public void processInsert(Insert insert) {
        List<Column> columns = insert.getColumns();
        if (CollectionUtils.isEmpty(columns)) {
            return;
        }
        columns.parallelStream().forEach(column -> column.setTable(insert.getTable()));
        MatchResult result = matchUpdateOrInsert(CommandType.INSERT, columns);
        if (result != null) {
            result.setModifyColums(columns);
            insert.getItemsList().accept(new ItemsListVisitorAdapter() {
                @Override
                public void visit(ExpressionList expressionList) {
                    result.setModifyParams(expressionList.getExpressions());
                }
            });
        }
        MATCH_RESULT_THREAD_LOCAL.get().put(MATCH_RESULT_KEY, result);
    }

    private MatchResult matchUpdateOrInsert(CommandType commandType, List<Column> columns) {
        TableFieldChangeWatcher watcher = (TableFieldChangeWatcher) MATCH_RESULT_THREAD_LOCAL.get().get(WATCH_METADATA_KEY);
        Set<WatchMetadata> watchMetadatas = watcher.getSetting();
        if (!CollectionUtils.isEmpty(watchMetadatas)) {
            MatchResult result = new MatchResult();
            result.setCommandType(commandType);
            result.setMatchFields(new HashMap<>());
            int count = 0;
            for (Column column : columns) {
                for (WatchMetadata watchMetadata : watchMetadatas) {
                    if (CollectionUtils.isEmpty(watchMetadata.getFields())) {
                        continue;
                    }
                    if (column.getTable().getName().equalsIgnoreCase(watchMetadata.getTable())) {
                        String columnName = column.getColumnName().toLowerCase();
                        columnName = columnName.contains(".") ? columnName.split("\\.")[1] : columnName;
                        if (watchMetadata.getFields().contains(columnName)) {
                            if (result.getMatchFields().get(watchMetadata) == null) {
                                count++;
                                result.getMatchFields().put(watchMetadata, new HashSet<>());
                            }
                            result.getMatchFields().get(watchMetadata).add(columnName);
                        }
                    }
                }
            }
            result.setCount(count);
            return result;
        }
        return null;
    }

    /**
     * 解析删除语句
     *
     * @param delete
     */
    @Override
    public void processDelete(Delete delete) {
        String tableName = delete.getTable().getName();
        TableFieldChangeWatcher watcher = (TableFieldChangeWatcher) MATCH_RESULT_THREAD_LOCAL.get().get(WATCH_METADATA_KEY);
        Set<WatchMetadata> watchMetadatas = watcher.getSetting();
        if (!CollectionUtils.isEmpty(watchMetadatas)) {
            MatchResult result = new MatchResult();
            result.setMatchFields(new HashMap<>());
            result.setCommandType(CommandType.DELETE);
            int count = 0;
            for (WatchMetadata watchMetadata : watchMetadatas) {
                if (watchMetadata.getTable().equalsIgnoreCase(tableName)) {
                    count++;
                    result.getMatchFields().put(watchMetadata, new HashSet<>(watchMetadata.getFields()));
                }
            }
            result.setCount(count);
            result.setWhere(delete.getWhere());
            MATCH_RESULT_THREAD_LOCAL.get().put(MATCH_RESULT_KEY, result);
        }
    }

    /**
     * 解析更新语句
     *
     * @param update
     */
    @Override
    public void processUpdate(Update update) {
        List<Column> columns = update.getColumns();
        if (CollectionUtils.isEmpty(columns)) {
            return;
        }
        columns.parallelStream().forEach(column -> column.setTable(update.getTable()));
        MatchResult result = matchUpdateOrInsert(CommandType.UPDATE, columns);
        if (result != null) {
            result.setModifyColums(columns);
            result.setModifyParams(update.getExpressions());
            result.setWhere(update.getWhere());
        }
        MATCH_RESULT_THREAD_LOCAL.get().put(MATCH_RESULT_KEY, result);
    }

    @Override
    public void processSelectBody(SelectBody selectBody) {

    }

    /**
     * 匹配当前执行的SQL是否有监听器在监听
     *
     * @param watcher 监听器
     * @param sql     将要执行的SQL（已经替换了参数的SQL）
     * @return
     */
    public MatchResult matchResult(TableFieldChangeWatcher watcher, String sql) {
        if (watcher == null || !quickMatch(watcher, sql)) {
            return null;
        }
        MATCH_RESULT_THREAD_LOCAL.set(new HashMap<>());
        try {
            MATCH_RESULT_THREAD_LOCAL.get().put(WATCH_METADATA_KEY, watcher);
            super.parser(null, sql);
            return (MatchResult) MATCH_RESULT_THREAD_LOCAL.get().get(MATCH_RESULT_KEY);
        } finally {
            MATCH_RESULT_THREAD_LOCAL.remove();
        }
    }

    /**
     * 快速匹配一遍，避免把每条sql都解析一遍
     *
     * @return
     */
    private boolean quickMatch(TableFieldChangeWatcher watcher, String sql) {
        Set<WatchMetadata> watchMetadataSet = watcher.getSetting();
        sql = sql.toLowerCase();
        for (WatchMetadata watchMetadata : watchMetadataSet) {
            if (sql.contains(watchMetadata.getTable().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
