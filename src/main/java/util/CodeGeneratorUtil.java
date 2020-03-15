package util;

import constant.BizConstant;
import freemarker.template.Template;
import model.config.*;
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
 * 模板代码生成工具类
 *
 * Created by jinglun on 2020-03-08
 */
public class CodeGeneratorUtil {
    private CodeGenConfigInfo codeGenConfigInfo;
    private String entityPackageName = "";
    private String daoPackageName = "";
    private String servicePackageName = "";

    public CodeGeneratorUtil(CodeGenConfigInfo codeGenConfigInfo) {
        this.codeGenConfigInfo = codeGenConfigInfo;
    }

    public void process() {
        if (!configEffective(codeGenConfigInfo)) {
            System.out.println("=== 配置信息不合法，请检查配置信息 ===");
            return;
        }
        DbInfo dbInfo = codeGenConfigInfo.getDbInfo();
        GeneratorInfo generatorInfo = codeGenConfigInfo.getGeneratorInfo();

        try {
            Connection connection = getDbConnection(dbInfo);
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getColumns(dbInfo.getDbName(), "", dbInfo.getTableName(), null);

            // 收集表的字段名称，类型，备注
            List<ColumnInfo> columnInfos = new ArrayList<>();
            ColumnInfo columnInfo = new ColumnInfo();
            while (resultSet.next()) {
                columnInfo.setColumnNameOrigin(resultSet.getString("COLUMN_NAME"));
                columnInfo.setColumnName(StrUtil.line2Hump(resultSet.getString("COLUMN_NAME"), false));
                columnInfo.setColumnNameFirstLetterUp(StrUtil.line2Hump(resultSet.getString("COLUMN_NAME"), true));
                columnInfo.setColumnType(resultSet.getString("TYPE_NAME"));
                columnInfo.setColumnComment(resultSet.getString("REMARKS").replaceAll("\n", ""));
                columnInfos.add(columnInfo);
            }

            if (generatorInfo.getGeneratorEntity().isNeedGenerate()) generatorEntity(dbInfo, generatorInfo, columnInfos);
            if (generatorInfo.getGeneratorDao().isNeedGenerate()) generatorDao(dbInfo, generatorInfo, columnInfos);
            if (generatorInfo.getGeneratorService().isNeedGenerate()) generatorService(columnInfos);
            if (generatorInfo.getGeneratorController().isNeedGenerate()) generatorController(columnInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generatorEntity(DbInfo dbInfo, GeneratorInfo generatorInfo, List<ColumnInfo> columnInfos) {
        String fileName = StrUtil.line2Hump(dbInfo.getTableName(), true) + ".java";
        String packageBaseLocation = generatorInfo.getPackageBaseLocation();
        String packageBaseName = generatorInfo.getPackageBaseName();
        ModuleInfo generatorEntity = generatorInfo.getGeneratorEntity();

        String filePath = packageBaseLocation + "/model";
        String packageName = packageBaseName + ".model";
        if (StringUtils.isNotEmpty(generatorEntity.getDetailPackageName())) {
            filePath = packageBaseLocation + generatorEntity.getDetailPackageName();
            packageName = packageBaseName + generatorEntity.getDetailPackageName().replaceAll("/", ".");
        }
        setEntityPackageName(packageName);

        String finalEntityFilePath = filePath + fileName;
        if (!filePath.endsWith("/")) {
            finalEntityFilePath = filePath + "/" + fileName;
        }
        System.out.println("=== 生成entity文件的路径为：" + finalEntityFilePath);
        // TODO 若目录没有存在，会创建文件失败，待优化
        File entityFile = new File(filePath + fileName);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("columns", columnInfos);
        //dataMap.put("filePath", finalEntityFilePath);
        dataMap.put("packageName", packageName);
        generatorFileByTemplate("Entity.ftl", entityFile, dataMap);
    }

    private void generatorDao(DbInfo dbInfo, GeneratorInfo generatorInfo, List<ColumnInfo> columnInfos) {
        String fileName = StrUtil.line2Hump(dbInfo.getTableName(), true) + "Mapper.java";
        String packageBaseLocation = generatorInfo.getPackageBaseLocation();
        String packageBaseName = generatorInfo.getPackageBaseName();
        ModuleInfo generatorDao = generatorInfo.getGeneratorDao();

        // 如果是mybatis，还要生成mapper文件 TODO mybatis待优化生成注解文件
        if (codeGenConfigInfo.getOrm().toLowerCase().equals(BizConstant.ORM.MyBatis)) {
            String filePath = packageBaseLocation + "/mapper";
            String packageName = packageBaseName + ".mapper";
            if (StringUtils.isNotEmpty(generatorDao.getDetailPackageName())) {
                filePath = packageBaseLocation + generatorDao.getDetailPackageName();
                packageName = packageBaseName + generatorDao.getDetailPackageName().replaceAll("/", ",");
            }
            setDaoPackageName(packageName);

            String entityPackageName = getEntityPackageName();

            String finalDaoFilePath = filePath + fileName;
            if (!filePath.endsWith("/")) {
                finalDaoFilePath = filePath + "/" + fileName;
            }
            System.out.println("=== 生成dao文件的路径为：" + finalDaoFilePath);
            File mapperFile = new File(finalDaoFilePath);

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("columns", columnInfos);
            //dataMap.put("filePath", finalDaoFilePath);
            dataMap.put("packageName", packageName);
            dataMap.put("entityPackageName", entityPackageName);
            generatorFileByTemplate("Mapper.ftl", mapperFile, dataMap);
            generatorMapperXML(dataMap, packageBaseLocation, dbInfo.getTableName());
        }
        if (codeGenConfigInfo.getOrm().toLowerCase().equals(BizConstant.ORM.JPA)) {
            System.out.println("待实现...");
            return;
        }
    }

    // TODO
    private void generatorMapperXML(Map<String, Object> dataMap, String packageBasePath, String tableName) {
        String finalFilePath = packageBasePath + "/mapper/" + StrUtil.line2Hump(tableName, true) + "Mapper.xml";
        System.out.println("finalFilePath: " + finalFilePath);
        File mapperFile = new File(finalFilePath);
        generatorFileByTemplate("MapperXML.ftl", mapperFile, dataMap);
        System.out.println("generatorMapperXML done.");
    }

    private void generatorService(List<ColumnInfo> columnInfos) {
    }

    private void generatorController(List<ColumnInfo> columnInfos) {
    }

    private void generatorFileByTemplate(String templateName, File file, Map<String, Object> dataMap) {
        DbInfo dbInfo = codeGenConfigInfo.getDbInfo();
        GeneratorInfo generatorInfo = codeGenConfigInfo.getGeneratorInfo();
        try {
            Template template = new FreeMarkerTemplateUtil().getTemplate(templateName);
            FileOutputStream fos = new FileOutputStream(file);
            dataMap.put("author", generatorInfo.getAuthor());
            dataMap.put("date", DateUtil.getCurDateStr());
            dataMap.put("tableNameOrigin", dbInfo.getTableName());
            dataMap.put("tableName", StrUtil.line2Hump(dbInfo.getTableName(), true));
            dataMap.put("tableComment", dbInfo.getTableComment());

            Writer writer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8), 1024 * 10);
            template.process(dataMap, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验参数是否合法：合法-true；不合法-false
     */
    private boolean configEffective(CodeGenConfigInfo codeGenConfigInfo) {
        if (StringUtils.isEmpty(codeGenConfigInfo.getOrm())) {
            System.out.println("=== 参数校验异常：orm框架不能为空！===");
            return false;
        }

        DbInfo dbInfo = codeGenConfigInfo.getDbInfo();
        if (dbInfo == null || StringUtils.isEmpty(dbInfo.getUrl()) || StringUtils.isEmpty(dbInfo.getUsername())
                || StringUtils.isEmpty(dbInfo.getPassword()) || StringUtils.isEmpty(dbInfo.getDriver())
                || StringUtils.isEmpty(dbInfo.getDbName()) || StringUtils.isEmpty(dbInfo.getTableName())) {
            System.out.println("=== 参数校验异常：数据库配置信息异常！===");
            return false;
        }

        GeneratorInfo generatorInfo = codeGenConfigInfo.getGeneratorInfo();
        if (generatorInfo == null || StringUtils.isEmpty(generatorInfo.getPackageBaseLocation())
                || StringUtils.isEmpty(generatorInfo.getPackageBaseName()) || generatorInfo.getGeneratorEntity() == null
                || generatorInfo.getGeneratorDao() == null || generatorInfo.getGeneratorService() == null
                || generatorInfo.getGeneratorController() == null) {
            System.out.println("=== 参数校验异常：生成文件配置信息异常！===");
            return false;
        }

        PluginInfo pluginInfo = codeGenConfigInfo.getPluginInfo();
        if (pluginInfo == null) {
            System.out.println("=== 参数校验异常：插件配置信息异常！===");
            return false;
        }

        return true;
    }

    /**
     * 获取数据库连接
     */
    private Connection getDbConnection(DbInfo dbInfo) throws Exception {
        Class.forName(dbInfo.getDriver());
        return DriverManager.getConnection(dbInfo.getUrl(), dbInfo.getUsername(), dbInfo.getPassword());
    }

    public String getEntityPackageName() {
        return entityPackageName;
    }

    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    public String getDaoPackageName() {
        return daoPackageName;
    }

    public void setDaoPackageName(String daoPackageName) {
        this.daoPackageName = daoPackageName;
    }

    public String getServicePackageName() {
        return servicePackageName;
    }

    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }
}
