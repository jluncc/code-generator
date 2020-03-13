package util;

import constant.BizConstant;
import freemarker.template.Template;
import model.config.CodeGenConfigInfo;
import model.config.DbInfo;
import model.config.GeneratorInfo;
import model.db.ColumnInfo;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
    private CodeGenConfigInfo codeGenConfigInfo;

    public CodeGeneratorUtil(CodeGenConfigInfo codeGenConfigInfo) {
        this.codeGenConfigInfo = codeGenConfigInfo;
    }

    public void process() {
        DbInfo dbInfo = codeGenConfigInfo.getDbInfo();
        GeneratorInfo generatorInfo = codeGenConfigInfo.getGeneratorInfo();
        try {
            Connection connection = getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getColumns(dbInfo.getDbName(), "", dbInfo.getTableName(), null);

            List<ColumnInfo> columnInfos = new ArrayList<>();
            ColumnInfo columnInfo;
            while (resultSet.next()) {
                columnInfo = new ColumnInfo();
                // 获取字段名称，类型，备注
                columnInfo.setColumnName(StrUtil.line2Hump(resultSet.getString("COLUMN_NAME"), false));
                columnInfo.setColumnNameOrigin(resultSet.getString("COLUMN_NAME"));
                columnInfo.setColumnNameFirstLetterUp(StrUtil.line2Hump(resultSet.getString("COLUMN_NAME"), true));
                columnInfo.setColumnType(resultSet.getString("TYPE_NAME"));
                columnInfo.setColumnComment(resultSet.getString("REMARKS").replaceAll("\n", ""));
                columnInfos.add(columnInfo);
            }

            if (generatorInfo.getGeneratorEntity().isNeedGenerate()) generatorEntity(columnInfos);
            if (generatorInfo.getGeneratorDao().isNeedGenerate()) generatorDao(columnInfos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generatorEntity(List<ColumnInfo> columnInfos) {
        DbInfo dbInfo = codeGenConfigInfo.getDbInfo();
        GeneratorInfo generatorInfo = codeGenConfigInfo.getGeneratorInfo();
        String fileName = StrUtil.line2Hump(dbInfo.getTableName(), true) + ".java";
        if (StringUtils.isEmpty(generatorInfo.getPackageBasePath())
                || StringUtils.isEmpty(generatorInfo.getPackageBaseName())) {
            System.out.println("项目存放的物理路径，或类的基本包名为空");
            return;
        }
        final String packageBasePath = generatorInfo.getPackageBasePath();
        final String packageBaseName = generatorInfo.getPackageBaseName();
        String filePath = packageBasePath + "/model/";
        String packageName = packageBaseName + ".model";
        if (StringUtils.isNotEmpty(generatorInfo.getGeneratorEntity().getDetailPath())) {
            filePath = packageBasePath + generatorInfo.getGeneratorEntity().getDetailPath();
        }
        final String templateName = "Entity.ftl";
        File entityFile = new File(filePath + fileName);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("columns", columnInfos);
        dataMap.put("filePath", filePath);
        dataMap.put("packageName", packageName);
        generatorFileByTemplate(templateName, entityFile, dataMap);
    }

    private void generatorDao(List<ColumnInfo> columnInfos) {
        DbInfo dbInfo = codeGenConfigInfo.getDbInfo();
        GeneratorInfo generatorInfo = codeGenConfigInfo.getGeneratorInfo();

        String fileName = StrUtil.line2Hump(dbInfo.getTableName(), true) + "Mapper.java";
        if (StringUtils.isEmpty(generatorInfo.getPackageBasePath())
                || StringUtils.isEmpty(generatorInfo.getPackageBaseName())) {
            System.out.println("项目存放的物理路径，或类的基本包名为空");
            return;
        }

        final String packageBasePath = generatorInfo.getPackageBasePath();
        final String packageBaseName = generatorInfo.getPackageBaseName();

        // 如果是mybatis，还要生成mapper文件
        if (codeGenConfigInfo.getOrm().toLowerCase().equals(BizConstant.ORM.MyBatis)) {
            String filePath = packageBasePath + "/mapper/";
            String packageName = packageBaseName + ".mapper";
            String entityPackageName = packageBaseName + ".model." + StrUtil.line2Hump(dbInfo.getTableName(), true);
            if (StringUtils.isNotEmpty(generatorInfo.getGeneratorEntity().getDetailPath())) {
                filePath = packageBasePath + generatorInfo.getGeneratorEntity().getDetailPath();
            }
            String templateName = "Mapper.ftl";
            String finalFilePath = filePath + fileName;
            System.out.println("finalFilePath: " + finalFilePath);
            File mapperFile = new File(finalFilePath);

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("columns", columnInfos);
            dataMap.put("filePath", filePath);
            dataMap.put("packageName", packageName);
            dataMap.put("entityPackageName", entityPackageName);
            generatorFileByTemplate(templateName, mapperFile, dataMap);

            generatorMapperXML(dataMap, packageBasePath, dbInfo.getTableName());
        }

    }

    private void generatorMapperXML(Map<String, Object> dataMap, String packageBasePath, String tableName) {
        String templateName = "MapperXML.ftl";
        String finalFilePath = packageBasePath + "/mapper/" + StrUtil.line2Hump(tableName, true) + "Mapper.xml";
        System.out.println("finalFilePath: " + finalFilePath);
        File mapperFile = new File(finalFilePath);
        generatorFileByTemplate(templateName, mapperFile, dataMap);
        System.out.println("generatorMapperXML done.");
    }


    private void generatorFileByTemplate(String templateName, File file, Map<String, Object> dataMap) {
        DbInfo dbInfo = codeGenConfigInfo.getDbInfo();
        GeneratorInfo generatorInfo = codeGenConfigInfo.getGeneratorInfo();
        try {
            Template template = new FreeMarkerTemplateUtil().getTemplate(templateName);
            FileOutputStream fos = new FileOutputStream(file);
            dataMap.put("author", generatorInfo.getAuthor());
            dataMap.put("date", DateUtil.getCurDateStr());
            dataMap.put("tableName", StrUtil.line2Hump(dbInfo.getTableName(), true));
            dataMap.put("tableNameOrigin", dbInfo.getTableName());
            dataMap.put("tableComment", dbInfo.getTableComment());

            Writer writer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8), 1024 * 10);
            template.process(dataMap, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws Exception {
        DbInfo dbInfo = codeGenConfigInfo.getDbInfo();
        Class.forName(dbInfo.getDriver());
        return DriverManager.getConnection(dbInfo.getUrl(), dbInfo.getUsername(), dbInfo.getPassword());
    }
}
