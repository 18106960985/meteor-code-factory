package com.github.qiyue.code.factory.entity;

import java.util.List;

/**
 * created by yebinghuan on 2018/4/17
 *  表属性
 *
 */
public class TableEntity {

    //表的名称
    private String tableName;
    //表的注释
    private String tableComment;
    //表的创建时间
    private String createTime;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
