package com.github.qiyue.code.factory.model;

import java.util.List;

/**
 * created by yebinghuan on 2018/4/18
 *  模板模型
 */
public class TemplaseMdeol {

    private String tableName;

    //模板
    private List<String> templases;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getTemplases() {
        return templases;
    }

    public void setTemplases(List<String> templases) {
        this.templases = templases;
    }
}
