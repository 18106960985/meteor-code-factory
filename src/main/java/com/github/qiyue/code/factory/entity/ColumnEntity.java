package com.github.qiyue.code.factory.entity;

/**
 * created by yebinghuan on 2018/4/17
 *  字段属性
 */
public class ColumnEntity {

    //字段名
    private String columnName;
    //数据类型
    private String dataType;
    //字段备注
    private String columnComment;
    //列 键
    private String columnKey;
    //外 键
    private String extra;
    //是否可为空 NO YES
    private String isNullable;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }
}
