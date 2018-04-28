package com.qiyue.meteor.code.factory.utils;

import com.qiyue.meteor.code.factory.entity.ColumnEntity;
import com.qiyue.meteor.code.factory.entity.TableEntity;
import com.qiyue.meteor.code.factory.model.ColumnModel;
import com.qiyue.meteor.code.factory.model.TableModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * created by yebinghuan on 2018/4/17
 * <p>
 * 代码生成器 工具类（核心）
 */
@Slf4j
public class CodeFactoryUtils {

    /**
     * 后期改成前端选择
     *
     * @return
     */
    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("template/index.js.vm");
        templates.add("template/index.vue.vm");
        templates.add("template/mapper.xml.vm");
        templates.add("template/biz.java.vm");
        templates.add("template/entity.java.vm");
        templates.add("template/mapper.java.vm");
        templates.add("template/controller.java.vm");
        return templates;
    }

    /**
     *  代码生成
     * @param tableEntity  表信息
     * @param columnEntities 表-> 列信息
     * @param zip    压缩包流
     * @param templates  选用的模板列表
     */
    public static void produceCode(TableEntity tableEntity, List<ColumnEntity> columnEntities, ZipOutputStream zip, List<String> templates) {

        //获取配置文件信息
        Configuration config = getConfig();

        //表模型
        TableModel tableModel = new TableModel();
        //表的信息
        String tableName = tableEntity.getTableName();
        tableModel.setTableName(tableName);
        tableModel.setComments(tableEntity.getTableComment());
        //表名转换成 java类名
        final String className = tableNameToJava(tableName, config.getString("tablePrefix"));
        tableModel.setClassName(className);
        tableModel.setClassname(StringUtils.uncapitalize(className));

        //存放表的列
        tableModel.setColumns(columnEntities
                .stream()
                .map(index -> {
                    ColumnModel model = new ColumnModel();
                    //列信息
                    String columnName = index.getColumnName();
                    model.setColumnName(columnName);
                    model.setComments(index.getColumnComment());
                    model.setDataType(index.getDataType());
                    model.setExtra(index.getExtra());
                    model.setIsNullable(index.getIsNullable());
                    //属性转换
                    String attrName = columnNameToJava(columnName);
                    model.setAttrName(attrName);
                    model.setAttrname(StringUtils.uncapitalize(attrName));
                    //属性类型转换
                    model.setAttrType(config.getString(index.getDataType(), "unknowType"));
                    //是否主键
                    if ("PRI".equalsIgnoreCase(index.getColumnKey()) && tableModel.getPk() == null) {
                        tableModel.setPk(model);
                    }
                    return model;

                })
                .collect(Collectors.toList()));

        /**
         * 代码生成器的原理很简单，其实是使用数据去渲染模板，然后我们从中得取渲染结果的流
         */
        //设置velocity资源加载器
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(properties);

        //封装模板数据
        Map<String , Object> params = new HashMap<>();
        params.put("tableName", tableModel.getTableName());
        params.put("comments", tableModel.getComments());
        params.put("pk", tableModel.getPk());
        params.put("className", tableModel.getClassName());
        params.put("classname", tableModel.getClassname());
        params.put("pathName", tableModel.getClassname().toLowerCase());
        params.put("columns", tableModel.getColumns());
        params.put("package", config.getString("package"));
        params.put("author", config.getString("author"));
        params.put("email", config.getString("email"));
        params.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        params.put("moduleName", config.getString("mainModule"));
        params.put("secondModuleName", toLowerCaseFirstOne(className));
        VelocityContext context = new VelocityContext(params);



        templates.stream().forEach( template ->{
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, tableModel.getClassName(), config.getString("package"), config.getString("mainModule"))));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败，表名：" + tableModel.getTableName(),e);
                throw new RuntimeException("渲染模板失败，表名：" + tableModel.getTableName(), e);
            }
        });

    }

    /**
     * 表名转换成Java类名
     *
     * @param tableName   表名
     * @param tablePrefix 表前错误的Unicode字符串!
     * @return
     */
    public static String tableNameToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        return columnNameToJava(tableName);
    }


    /**
     * 列名转换成Java属性名  user_id to userId
     *
     * @param columnName 字段名
     * @return
     */
    public static String columnNameToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("templatesConfig.properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException("获取配置文件失败，", e);
        }
    }
    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 获取文件名
     * @param template 模板
     * @param className 列名
     * @param packageName  包名
     * @param moduleName 前端模块前缀
     * @return
     */
    public static String getFileName(String template, String className, String packageName, String moduleName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        String frontPath = "ui" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }

        if (template.contains("index.js.vm")) {
            return frontPath + "api" + File.separator + moduleName + File.separator + toLowerCaseFirstOne(className) + File.separator + "index.js";
        }

        if (template.contains("index.vue.vm")) {
            return frontPath + "views" + File.separator + moduleName + File.separator + toLowerCaseFirstOne(className) + File.separator + "index.vue";
        }

        if (template.contains("biz.java.vm")) {
            return packagePath + "biz" + File.separator + className + "Biz.java";
        }
        if (template.contains("mapper.java.vm")) {
            return packagePath + "mapper" + File.separator + className + "Mapper.java";
        }
        if (template.contains("entity.java.vm")) {
            return packagePath + "entity" + File.separator + className + ".java";
        }
        if (template.contains("controller.java.vm")) {
            return packagePath + "rest" + File.separator + className + "Controller.java";
        }
        if (template.contains("mapper.xml.vm")) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + className + "Mapper.xml";
        }

        return null;
    }


}
