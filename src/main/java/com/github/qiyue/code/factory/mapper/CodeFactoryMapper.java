package com.github.qiyue.code.factory.mapper;

import com.github.qiyue.code.factory.entity.ColumnEntity;
import com.github.qiyue.code.factory.entity.TableEntity;

import java.util.List;
import java.util.Map;

/**
 * created by yebinghuan on 2018/4/17
 */
public interface CodeFactoryMapper {


    /**
     *  获取全部数据库表
     * @param map
     * @return
     */
    List<TableEntity> getList(Map<String, Object> map);


    int getTotal(Map<String, Object> map);

    TableEntity getTableByTableName (String tableName);

    List<ColumnEntity> getColumnsByTableName(String tableName);
}
