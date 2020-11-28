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
        PluginInfo pluginInfo = codeGenConfigInfo.getPluginInfo();

        try {
            Connection connection = getDbConnection(dbInfo);
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getColumns(dbInfo.getDbName(), "", dbInfo.getTableName(), null);

            // 收集表的字段名称，类型，备注
            List<ColumnInfo> columnInfos = new ArrayList<>();
            while (resultSet.next()) {
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setColumnNameOrigin(resultSet.getString("COLUMN_NAME"));
                columnInfo.setColumnName(StrUtil.line2Hump(resultSet.getString("COLUMN_NAME"), false));
                columnInfo.setColumnNameFirstLetterUp(StrUtil.line2Hump(resultSet.getString("COLUMN_NAME"), true));
                columnInfo.setColumnType(resultSet.getString("TYPE_NAME"));
                columnInfo.setColumnComment(resultSet.getString("REMARKS").replaceAll("\n", ""));
                columnInfos.add(columnInfo);
            }

            if (generatorInfo.getGeneratorEntity().isNeedGenerate()) generatorEntity(dbInfo, generatorInfo, columnInfos, pluginInfo);
            if (generatorInfo.getGeneratorDao().isNeedGenerate()) generatorDao(dbInfo, generatorInfo, columnInfos);
            if (generatorInfo.getGeneratorService().isNeedGenerate()) generatorService(dbInfo, generatorInfo, columnInfos);
            if (generatorInfo.getGeneratorController().isNeedGenerate()) generatorController(dbInfo, generatorInfo, columnInfos, pluginInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generatorEntity(DbInfo dbInfo, GeneratorInfo generatorInfo, List<ColumnInfo> columnInfos, PluginInfo pluginInfo) {
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
        setEntityPackageName(packageName + "." + StrUtil.line2Hump(dbInfo.getTableName(), true));

        String finalEntityFilePath = filePath + fileName;
        if (!filePath.endsWith("/")) {
            finalEntityFilePath = filePath + "/" + fileName;
        }
        System.out.println("生成entity文件的路径为：" + finalEntityFilePath);
        // TODO 若目录没有存在，会创建文件失败，待优化
        File entityFile = new File(finalEntityFilePath);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("columns", columnInfos);
        dataMap.put("packageName", packageName);
        dataMap.put("pluginInfo", pluginInfo);
        generatorFileByTemplate("Entity.ftl", entityFile, dataMap);
        System.out.println("生成entity文件完毕");
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
                packageName = packageBaseName + generatorDao.getDetailPackageName().replaceAll("/", ".");
            }
            setDaoPackageName(packageName + "." + StrUtil.line2Hump(dbInfo.getTableName(), true) + "Mapper");

            String entityPackageName = getEntityPackageName();

            String finalDaoFilePath = filePath + fileName;
            if (!filePath.endsWith("/")) {
                finalDaoFilePath = filePath + "/" + fileName;
            }
            System.out.println("生成dao文件的路径为：" + finalDaoFilePath);
            File mapperFile = new File(finalDaoFilePath);

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("columns", columnInfos);
            dataMap.put("packageName", packageName);
            dataMap.put("entityPackageName", entityPackageName);
            generatorFileByTemplate("Mapper.ftl", mapperFile, dataMap);
            System.out.println("生成dao文件完毕");
            generatorMapperXML(dataMap, generatorDao.getMapperXMLPath(), dbInfo.getTableName());
        }
        if (codeGenConfigInfo.getOrm().toLowerCase().equals(BizConstant.ORM.JPA)) {
            System.out.println("待实现...");
            return;
        }
    }

    private void generatorMapperXML(Map<String, Object> dataMap, String mapperXMLPath, String tableName) {
        if (StringUtils.isEmpty(mapperXMLPath)) {
            System.out.println("mapperXMLPath不能为空");
            return;
        }
        String finalMapperXMLPath = mapperXMLPath + StrUtil.line2Hump(tableName, true) + "Mapper.xml";
        if (!mapperXMLPath.endsWith("/")) {
            finalMapperXMLPath = mapperXMLPath + "/" + StrUtil.line2Hump(tableName, true) + "Mapper.xml";
        }
        System.out.println("生成mapperXML文件的路径为：" + finalMapperXMLPath);
        File mapperXMLFile = new File(finalMapperXMLPath);
        generatorFileByTemplate("MapperXML.ftl", mapperXMLFile, dataMap);
        System.out.println("生成mapperXML文件完毕");
    }

    private void generatorService(DbInfo dbInfo, GeneratorInfo generatorInfo, List<ColumnInfo> columnInfos) {
        String fileName = StrUtil.line2Hump(dbInfo.getTableName(), true) + "Service.java";
        String implFileName = StrUtil.line2Hump(dbInfo.getTableName(), true) + "ServiceImpl.java";
        String packageBaseLocation = generatorInfo.getPackageBaseLocation();
        String packageBaseName = generatorInfo.getPackageBaseName();
        ModuleInfo generatorService = generatorInfo.getGeneratorService();

        String filePath = packageBaseLocation + "/service";
        String packageName = packageBaseName + ".service";
        if (StringUtils.isNotEmpty(generatorService.getDetailPackageName())) {
            filePath = packageBaseLocation + generatorService.getDetailPackageName();
            packageName = packageBaseName + generatorService.getDetailPackageName().replaceAll("/", ".");
        }
        setServicePackageName(packageName + "." + StrUtil.line2Hump(dbInfo.getTableName(), true) + "Service");

        String finalServiceFilePath = filePath + fileName;
        String finalServiceImplFilePath = filePath + implFileName;
        if (!filePath.endsWith("/")) {
            finalServiceFilePath = filePath + "/" + fileName;
            finalServiceImplFilePath = filePath + "/" + implFileName;
        }

        System.out.println("生成service文件的路径为：" + finalServiceFilePath);
        File serviceFile = new File(finalServiceFilePath);

        String entityPackageName = getEntityPackageName();
        String daoPackageName = getDaoPackageName();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("columns", columnInfos);
        dataMap.put("packageName", packageName);
        dataMap.put("entityPackageName", entityPackageName);
        dataMap.put("daoPackageName", daoPackageName);
        if (generatorService.isInterfaceMode()) {
            // 生成接口
            generatorFileByTemplate("ServiceInterface.ftl", serviceFile, dataMap);
            // 生成接口实现类
            System.out.println("生成serviceImpl文件的路径为：" + finalServiceImplFilePath);
            File serviceImplFile = new File(finalServiceImplFilePath);
            generatorFileByTemplate("ServiceImpl.ftl", serviceImplFile, dataMap);
        } else {
            generatorFileByTemplate("Service.ftl", serviceFile, dataMap);
        }
        System.out.println("生成service文件完毕");
    }

    private void generatorController(DbInfo dbInfo, GeneratorInfo generatorInfo, List<ColumnInfo> columnInfos, PluginInfo pluginInfo) {
        String fileName = StrUtil.line2Hump(dbInfo.getTableName(), true) + "Controller.java";
        String packageBaseLocation = generatorInfo.getPackageBaseLocation();
        String packageBaseName = generatorInfo.getPackageBaseName();
        ModuleInfo generatorController = generatorInfo.getGeneratorController();

        String filePath = packageBaseLocation + "/controller";
        String packageName = packageBaseName + ".controller";
        if (StringUtils.isNotEmpty(generatorController.getDetailPackageName())) {
            filePath = packageBaseLocation + generatorController.getDetailPackageName();
            packageName = packageBaseName + generatorController.getDetailPackageName().replaceAll("/", ".");
        }

        String finalControllerFilePath = filePath + fileName;
        if (!filePath.endsWith("/")) {
            finalControllerFilePath = filePath + "/" + fileName;
        }
        System.out.println("生成controller文件的路径为：" + finalControllerFilePath);
        File serviceFile = new File(finalControllerFilePath);

        String entityPackageName = getEntityPackageName();
        String servicePackageName = getServicePackageName();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("columns", columnInfos);
        dataMap.put("packageName", packageName);
        dataMap.put("mappingUrl", generatorController.getMappingUrl());
        dataMap.put("entityPackageName", entityPackageName);
        dataMap.put("servicePackageName", servicePackageName);
        dataMap.put("pluginInfo", pluginInfo);
        generatorFileByTemplate("Controller.ftl", serviceFile, dataMap);
        System.out.println("生成controller文件完毕");
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
