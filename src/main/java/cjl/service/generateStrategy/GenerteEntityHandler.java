package cjl.service.generateStrategy;


import cjl.model.config.DbInfo;
import cjl.model.config.GeneratorInfo;
import cjl.model.config.ModuleInfo;
import cjl.model.config.PluginInfo;
import cjl.model.db.ColumnInfo;
import cjl.util.LogUtil;
import cjl.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerteEntityHandler implements GenerateStrategyHandler {

    @Resource
    private CommonGenerateHandler commonGenerateHandler;

    @Override
    public void generateFile(DbInfo dbInfo, GeneratorInfo generatorInfo, List<ColumnInfo> columnInfos, PluginInfo pluginInfo) {
        String fileName = String.format("%s.java", StrUtil.line2Hump(dbInfo.getTableName(), true));
        String packageBaseLocation = generatorInfo.getPackageBaseLocation();
        String packageBaseName = generatorInfo.getPackageBaseName();
        ModuleInfo generatorEntity = generatorInfo.getGeneratorEntity();

        String filePath = String.format("%s/cjl.model", packageBaseLocation);
        String packageName = String.format("%s.cjl.model", packageBaseLocation);
        if (StringUtils.isNotEmpty(generatorEntity.getDetailPackageName())) {
            filePath = packageBaseLocation + generatorEntity.getDetailPackageName();
            packageName = packageBaseName + generatorEntity.getDetailPackageName().replaceAll("/", ".");
        }
        //setEntityPackageName(String.format("%s.%s", packageName, StrUtil.line2Hump(dbInfo.getTableName(), true)));

        String finalEntityFilePath = filePath + fileName;
        if (!filePath.endsWith("/")) {
            finalEntityFilePath = String.format("%s/%s", filePath, fileName);
        }
        LogUtil.SYS.info("生成entity文件的路径为：{}", finalEntityFilePath);
        // TODO 若目录没有存在，会创建文件失败，待优化
        File entityFile = new File(finalEntityFilePath);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("columns", columnInfos);
        dataMap.put("packageName", packageName);
        dataMap.put("pluginInfo", pluginInfo);
        commonGenerateHandler.generatorFileByTemplate("Entity.ftl", entityFile, dataMap, dbInfo, generatorInfo);
        LogUtil.SYS.info("生成entity文件完毕");
    }

}
