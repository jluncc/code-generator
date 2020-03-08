package util;

import bean.config.DbConfig;
import bean.config.GeneratorConfig;
import bean.db.ColumnInfo;
import constant.CodeConst;
import freemarker.template.Template;

import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jinglun on 2020-03-08
 */
public class CodeGeneratorUtil {
    private DbConfig dbConfig;
    private GeneratorConfig generatorConfig;

    public CodeGeneratorUtil(DbConfig dbConfig, GeneratorConfig generatorConfig) {
        this.dbConfig = dbConfig;
        this.generatorConfig = generatorConfig;
    }

    public void process() {
        try {
            Connection connection = getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getColumns(dbConfig.getDbName(), "", dbConfig.getTableName(), null);

            List<ColumnInfo> columnInfos = new ArrayList<>();
            ColumnInfo columnInfo;
            while (resultSet.next()) {
                columnInfo = new ColumnInfo();
                // 获取字段名称，类型，备注
                columnInfo.setColumnName(resultSet.getString("COLUMN_NAME"));
                columnInfo.setColumnType(resultSet.getString("TYPE_NAME"));
                columnInfo.setColumnComment(resultSet.getString("REMARKS").replaceAll("\n", ""));
                columnInfos.add(columnInfo);
            }

            if (generatorConfig.isGeneratorEntity()) generatorEntity(columnInfos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generatorEntity(List<ColumnInfo> columnInfos) {
        String fileName = StrUtil.line2Hump(dbConfig.getTableName(), true) + ".java";
        final String outPath = generatorConfig.getOutPath() + fileName;
        final String templateName = "Entity.ftl";
        File odsFile = new File(outPath);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("columns", columnInfos);
        generatorFileByTemplate(templateName, odsFile, dataMap);
    }

    private void generatorFileByTemplate(String templateName, File file, Map<String, Object> dataMap) {
        try {
            Template template = new FreeMarkerTemplateUtil().getTemplate(templateName);
            FileOutputStream fos = new FileOutputStream(file);
            dataMap.put("table_name", dbConfig.getTableName());
            dataMap.put("table_annotation", dbConfig.getTableComment());

            Writer writer = new BufferedWriter(new OutputStreamWriter(fos, CodeConst.UTF8), 1024 * 10);
            template.process(dataMap, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws Exception {
        Class.forName(dbConfig.getDriver());
        return DriverManager.getConnection(dbConfig.getConnectionUrl(), dbConfig.getUsername(), dbConfig.getPassword());
    }
}
