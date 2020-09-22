package com.github.wujiuye.datasource.sqlwatcher;

import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 监听配置
 *
 * @author wujiuye 2020/07/10
 */
public class WatchMetadata {

    /**
     * 监听的表名
     */
    private String table;
    /**
     * 监听的字段
     */
    private Set<String> fields;

    public String getTable() {
        return table;
    }

    public Set<String> getFields() {
        return fields;
    }

    public void setTable(String table) {
        this.table = table.toLowerCase();
    }

    public void setFields(Set<String> fields) {
        if (!CollectionUtils.isEmpty(fields)) {
            this.fields = fields.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
        }
    }

    public void addField(String... fields) {
        if (this.fields == null) {
            this.fields = new HashSet<>();
        }
        for (String field : fields) {
            this.fields.add(field.toLowerCase());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WatchMetadata)) {
            return false;
        }
        WatchMetadata that = (WatchMetadata) o;
        return Objects.equals(getTable(), that.getTable()) &&
                Objects.equals(getFields(), that.getFields());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTable(), getFields());
    }

}
