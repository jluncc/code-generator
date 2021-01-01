package cjl.service.generateStrategy;

import cjl.model.config.DbInfo;
import cjl.model.config.GeneratorInfo;
import cjl.model.config.ModuleInfo;
import cjl.model.config.PluginInfo;
import cjl.model.db.ColumnInfo;
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
        File serviceFile = new File(finalControllerFilePath);

        String orm = params.get("orm").toString();
        String entityPackageName = params.get("entityPackageName").toString();
        String servicePackageName = params.get("servicePackageName").toString();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("columns", columnInfos);
        dataMap.put("packageName", packageName);
        dataMap.put("mappingUrl", generatorController.getMappingUrl());
        dataMap.put("entityPackageName", entityPackageName);
        dataMap.put("servicePackageName", servicePackageName);
        dataMap.put("pluginInfo", pluginInfo);
        dataMap.put("orm", orm);
        CommonGenerateHandler.generatorFileByTemplate("Controller.ftl", serviceFile, dataMap, dbInfo, generatorInfo);
        LogUtil.SYS.info("生成controller文件完毕");
    }
}
