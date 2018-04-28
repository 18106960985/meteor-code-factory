package com.qiyue.meteor.code.factory.controller;

import com.qiyue.meteor.code.factory.constant.Constant;
import com.qiyue.meteor.code.factory.entity.TableEntity;
import com.qiyue.meteor.code.factory.model.TemplaseMdeol;
import com.qiyue.meteor.code.factory.service.CodeFactoryService;
import com.qiyue.meteor.code.factory.utils.CodeFactoryUtils;
import com.qiyue.meteor.code.factory.vo.TableResult;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by y747718944 on 2018/4/28
 */
@RestController
@RequestMapping(Constant.API+"/generator")
public class GeneratorController {

    @Autowired
    CodeFactoryService codeFactoryService;


    @GetMapping("/page")
    public TableResult<TableEntity> getTablePage(@RequestParam Map<String, Object> params){

        List<TableEntity> tableEntityList =   codeFactoryService.getList(params);
        int total = codeFactoryService.getTotal(params);
        return new TableResult<TableEntity>(total,tableEntityList);
    }

    /**
     * 生成代码
     */
    @RequestMapping("/code")
    public void code(@RequestParam String[] tableNames, HttpServletResponse response) throws IOException {
        System.err.println("tableNames:" +tableNames[0]);
        List<TemplaseMdeol> templaseMdeols = new ArrayList<>();
        for(String tableName : tableNames){
            TemplaseMdeol model = new TemplaseMdeol();
            model.setTableName(tableName);
            model.setTemplases(CodeFactoryUtils.getTemplates());
            templaseMdeols.add(model);
        }
        byte[] data = codeFactoryService.produceCode(templaseMdeols);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"ag-admin-code.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }


}
