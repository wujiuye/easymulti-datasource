package com.github.wujiuye.datasource.sqlwatcher;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitorAdapter;
import net.sf.jsqlparser.schema.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * SQL表达式
 *
 * @author wujiuye 2020/07/10
 */
public class SqlExpressionUtils {

    /**
     * 适配Inset、Delete、Update，且针对update事件优先从where匹配
     *
     * @param commandType
     * @param matchItem
     * @param cloumnName
     * @return
     */
    public static Object getColumnValue(CommandType commandType, MatchItem matchItem, String cloumnName) {
        return getColumnValue(commandType, matchItem, cloumnName, true);
    }

    /**
     * 适配Inset、Delete、Update
     *
     * @param commandType
     * @param matchItem
     * @param cloumnName
     * @param where       是否优先从where条件匹配字段的值
     * @return
     */
    public static Object getColumnValue(CommandType commandType, MatchItem matchItem, String cloumnName, boolean where) {
        switch (commandType) {
            case INSERT:
            case DELETE:
                return getColumnValueByInsertOrDelete(commandType, matchItem, cloumnName);
            case UPDATE:
                return getColumnValueByUpdate(commandType, matchItem, cloumnName, where);
            default:
                return null;
        }
    }

    /**
     * 首先从where取字段的值，拿不到的话，如果是更新或插入操作，则尝试从需要插入或更新的字段取
     *
     * @param commandType SQL命令类型
     * @param matchItem   匹配项
     * @param cloumnName  取值字段名
     * @return
     */
    public static Object getColumnValueByInsertOrDelete(CommandType commandType, MatchItem matchItem, String cloumnName) {
        switch (commandType) {
            case INSERT:
                return getFromValue(cloumnName, matchItem);
            case DELETE:
                return getWhereValue(cloumnName, matchItem.getWhere());
            default:
                throw new UnsupportedOperationException("不支持！");
        }
    }

    /**
     * 针对update语句，从set或where获取字段的值
     *
     * @param commandType
     * @param matchItem
     * @param cloumnName
     * @param where       是否优先从where获取
     * @return
     */
    public static Object getColumnValueByUpdate(CommandType commandType, MatchItem matchItem, String cloumnName, boolean where) {
        if (commandType == CommandType.UPDATE) {
            // where优先
            if (where) {
                Object result = getWhereValue(cloumnName, matchItem.getWhere());
                if (result != null) {
                    return result;
                }
                return getFromValue(cloumnName, matchItem);
            }
            // set xxx=xxx优先
            else {
                Object result = getFromValue(cloumnName, matchItem);
                if (result != null) {
                    return result;
                }
                return getWhereValue(cloumnName, matchItem.getWhere());
            }
        }
        throw new UnsupportedOperationException("不支持！");
    }

    private static Object getFromValue(String cloumnName, MatchItem matchItem) {
        int index = -1;
        for (int i = 0, length = matchItem.getModifyColums().size(); i < length; i++) {
            Column column = matchItem.getModifyColums().get(i);
            if (equalsColumn(column, cloumnName)) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            Expression expression = matchItem.getModifyParams().get(index);
            return getValue(expression);
        }
        return null;
    }

    private static Object getWhereValue(String column, Expression expression) {
        if (expression instanceof EqualsTo) {
            Expression left = ((EqualsTo) expression).getLeftExpression();
            Expression right = ((EqualsTo) expression).getRightExpression();
            if (left instanceof Column && equalsColumn((Column) left, column)) {
                return getValue(right);
            }
            return null;
        } else if (expression instanceof BinaryExpression) {
            Expression left = ((BinaryExpression) expression).getLeftExpression();
            Object result = getWhereValue(column, left);
            if (result != null) {
                return result;
            }
            Expression right = ((BinaryExpression) expression).getRightExpression();
            return getWhereValue(column, right);
        } else if (expression instanceof Parenthesis) {
            Expression expression1 = ((Parenthesis) expression).getExpression();
            return getWhereValue(column, expression1);
        } else if (expression instanceof InExpression) {
            Expression left = ((InExpression) expression).getLeftExpression();
            AtomicReference<List<Object>> reference = new AtomicReference<>(new ArrayList<>());
            if (left instanceof Column && equalsColumn((Column) left, column)) {
                ((InExpression) expression).getRightItemsList().accept(new ItemsListVisitorAdapter() {
                    @Override
                    public void visit(ExpressionList expressionList) {
                        List<Expression> expressions = expressionList.getExpressions();
                        for (Expression expression : expressions) {
                            reference.get().add(getValue(expression));
                        }
                    }
                });
            }
            return reference.get();
        }
        return null;
    }

    private static Object getValue(Expression expression) {
        if (expression instanceof NullValue) {
            return null;
        } else if (expression instanceof LongValue) {
            return ((LongValue) expression).getValue();
        } else if (expression instanceof DoubleValue) {
            return ((DoubleValue) expression).getValue();
        } else if (expression instanceof DateValue) {
            return ((DateValue) expression).getValue();
        } else if (expression instanceof StringValue) {
            return ((StringValue) expression).getValue();
        } else if (expression instanceof TimeValue) {
            return ((TimeValue) expression).getValue().getTime();
        } else if (expression instanceof TimestampValue) {
            return ((TimestampValue) expression).getValue().toLocalDateTime();
        }
        return null;
    }

    private static boolean equalsColumn(Column column, String cloumnName) {
        String cName = column.getColumnName().toLowerCase();
        // 要考虑：表名+"."+列名的情况
        return cName.equalsIgnoreCase(cloumnName) || cName.endsWith("." + cloumnName);
    }

}
