package com.qiyue.meteor.code.factory.service;

import com.qiyue.meteor.code.factory.entity.ColumnEntity;
import com.qiyue.meteor.code.factory.entity.TableEntity;
import com.qiyue.meteor.code.factory.model.TemplaseMdeol;

import java.util.List;
import java.util.Map;

/**
 * created by yebinghuan on 2018/4/17
 */
public interface CodeFactoryService {
    /**
     *  获取全部数据库表
     * @param map
     * @return
     */
    List<TableEntity> getList(Map<String, Object> map);


    int getTotal(Map<String, Object> map);

    TableEntity getTableByTableName (String tableName);

    List<ColumnEntity> getColumnsByTableName(String tableName);

    public byte[] produceCode(List<TemplaseMdeol> templaseMdeols);
}
