package cjl.service.generateStrategy;

import cjl.model.config.DbInfo;
import cjl.model.config.GeneratorInfo;
import cjl.model.config.ModuleInfo;
import cjl.model.config.PluginInfo;
import cjl.model.db.ColumnInfo;
import cjl.util.FileUtil;
import cjl.util.LogUtil;
import cjl.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateControllerHandler implements GenerateStrategyHandler {
    @Override
    public void generateFile(DbInfo dbInfo, GeneratorInfo generatorInfo, List<ColumnInfo> columnInfos, PluginInfo pluginInfo, Map<Object, Object> params) {
        String fileName = String.format("%sController.java", StrUtil.line2Hump(dbInfo.getTableName(), true));
        String packageBaseLocation = generatorInfo.getPackageBaseLocation();
        String packageBaseName = generatorInfo.getPackageBaseName();
        ModuleInfo generatorController = generatorInfo.getGeneratorController();

        String filePath = String.format("%s/controller", packageBaseLocation);
        String packageName = String.format("%s.controller", packageBaseName);
        if (StringUtils.isNotEmpty(generatorController.getDetailPackageName())) {
            filePath = packageBaseLocation + generatorController.getDetailPackageName();
            packageName = packageBaseName + generatorController.getDetailPackageName().replaceAll("/", ".");
        }

        String finalControllerFilePath = filePath + fileName;
        if (!filePath.endsWith("/")) {
            finalControllerFilePath = String.format("%s/%s", filePath, fileName);
        }
        LogUtil.SYS.info("生成controller文件的路径为：{}", finalControllerFilePath);
        File controllerFile = new File(finalControllerFilePath);
        if (!FileUtil.mkdirs(controllerFile.getParentFile())) return;

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("columns", columnInfos);
        dataMap.put("packageName", packageName);
        dataMap.put("mappingUrl", generatorController.getMappingUrl());
        dataMap.put("entityPackageName", generatorInfo.getEntityPackageName());
        dataMap.put("servicePackageName", generatorInfo.getServicePackageName());
        dataMap.put("pluginInfo", pluginInfo);
        dataMap.put("orm", generatorInfo.getOrm());

        handleApiResultGenerate(filePath, dataMap, dbInfo, generatorInfo);
        CommonGenerateHandler.generatorFileByTemplate("Controller.ftl", controllerFile, dataMap, dbInfo, generatorInfo);
        LogUtil.SYS.info("生成controller文件完毕");
    }

    private void handleApiResultGenerate(String filePath, Map<String, Object> dataMap, DbInfo dbInfo, GeneratorInfo generatorInfo) {
        String finalApiResultFilePath = filePath + "/ApiResult.java";
        LogUtil.SYS.info("生成ApiResult文件的路径为：{}", finalApiResultFilePath);
        File apiResultFile = new File(finalApiResultFilePath);
        if (!FileUtil.mkdirs(apiResultFile.getParentFile())) return;

        CommonGenerateHandler.generatorFileByTemplate("ApiResult.ftl", apiResultFile, dataMap, dbInfo, generatorInfo);
    }
}
