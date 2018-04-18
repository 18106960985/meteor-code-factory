package com.github.qiyue.code.factory.service.impl;

import com.github.qiyue.code.factory.entity.ColumnEntity;
import com.github.qiyue.code.factory.entity.TableEntity;
import com.github.qiyue.code.factory.mapper.CodeFactoryMapper;
import com.github.qiyue.code.factory.model.TemplaseMdeol;
import com.github.qiyue.code.factory.service.CodeFactoryService;
import com.github.qiyue.code.factory.utils.CodeFactoryUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * created by yebinghuan on 2018/4/17
 */
@Service
public class CodeFactoryServiceImpl implements CodeFactoryService {

    @Autowired
    private CodeFactoryMapper codeFactoryMapper;


    @Override
    public List<TableEntity> getList(Map<String, Object> map) {
        int offset = Integer.parseInt(map.get("offset").toString());
        int limit = Integer.parseInt(map.get("limit").toString());
        map.put("offset", offset);
        map.put("limit", limit);
        return codeFactoryMapper.getList(map);
    }

    @Override
    public int getTotal(Map<String, Object> map) {
        return codeFactoryMapper.getTotal(map);
    }

    @Override
    public TableEntity getTableByTableName(String tableName) {
        return codeFactoryMapper.getTableByTableName(tableName);
    }

    @Override
    public List<ColumnEntity> getColumnsByTableName(String tableName) {
        return codeFactoryMapper.getColumnsByTableName(tableName);
    }

    @Override
    public byte[] produceCode(List<TemplaseMdeol> templaseMdeols) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        templaseMdeols
                .stream()
                .forEach(templaseMdeol -> {
                    //查询表信息
                    TableEntity tableEntity = getTableByTableName(templaseMdeol.getTableName());
                    //查询列信息
                    List<ColumnEntity> columnEntities = getColumnsByTableName(templaseMdeol.getTableName());
                    //生成代码
                    CodeFactoryUtils.produceCode(tableEntity,columnEntities,zip,templaseMdeol.getTemplases());
                });

        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }


}
