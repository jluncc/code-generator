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

public class GenerateServiceHandler implements GenerateStrategyHandler {
    @Override
    public void generateFile(DbInfo dbInfo, GeneratorInfo generatorInfo, List<ColumnInfo> columnInfos, PluginInfo pluginInfo, Map<Object, Object> params) {
        String fileName = String.format("%sService.java", StrUtil.line2Hump(dbInfo.getTableName(), true));
        String implFileName = String.format("%sServiceImpl.java", StrUtil.line2Hump(dbInfo.getTableName(), true));
        String packageBaseLocation = generatorInfo.getPackageBaseLocation();
        String packageBaseName = generatorInfo.getPackageBaseName();
        ModuleInfo generatorService = generatorInfo.getGeneratorService();

        String filePath = String.format("%s/cjl.service", packageBaseLocation);
        String packageName = String.format("%s.cjl.service", packageBaseName);
        if (StringUtils.isNotEmpty(generatorService.getDetailPackageName())) {
            filePath = packageBaseLocation + generatorService.getDetailPackageName();
            packageName = packageBaseName + generatorService.getDetailPackageName().replaceAll("/", ".");
        }
        //setServicePackageName(String.format("%s.%sService", packageName, StrUtil.line2Hump(dbInfo.getTableName(), true)));

        String finalServiceFilePath = filePath + fileName;
        String finalServiceImplFilePath = filePath + implFileName;
        if (!filePath.endsWith("/")) {
            finalServiceFilePath = String.format("%s/%s", filePath, fileName);
            finalServiceImplFilePath = String.format("%s/%s", filePath, implFileName);
        }

        LogUtil.SYS.info("生成service文件的路径为：{}", finalServiceFilePath);
        File serviceFile = new File(finalServiceFilePath);

        String orm = params.get("orm").toString();
        String entityPackageName = params.get("entityPackageName").toString();
        String daoPackageName = params.get("daoPackageName").toString();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("columns", columnInfos);
        dataMap.put("packageName", packageName);
        dataMap.put("entityPackageName", entityPackageName);
        dataMap.put("daoPackageName", daoPackageName);
        dataMap.put("orm", orm);
        if (generatorService.isInterfaceMode()) {
            // 生成接口
            CommonGenerateHandler.generatorFileByTemplate("ServiceInterface.ftl", serviceFile, dataMap, dbInfo, generatorInfo);
            // 生成接口实现类
            LogUtil.SYS.info("生成serviceImpl文件的路径为：{}", finalServiceImplFilePath);
            File serviceImplFile = new File(finalServiceImplFilePath);
            CommonGenerateHandler.generatorFileByTemplate("ServiceImpl.ftl", serviceImplFile, dataMap, dbInfo, generatorInfo);
        } else {
            CommonGenerateHandler.generatorFileByTemplate("Service.ftl", serviceFile, dataMap, dbInfo, generatorInfo);
        }
        LogUtil.SYS.info("生成service文件完毕");
    }
}
