package cjl.service.generateStrategy;

import cjl.constant.BizConstant;
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

public class GenerateDaoHandler implements GenerateStrategyHandler {
    @Override
    public void generateFile(DbInfo dbInfo, GeneratorInfo generatorInfo, List<ColumnInfo> columnInfos, PluginInfo pluginInfo, Map<Object, Object> params) {
        String fileName = String.format("%sMapper.java", StrUtil.line2Hump(dbInfo.getTableName(), true));
        String packageBaseLocation = generatorInfo.getPackageBaseLocation();
        String packageBaseName = generatorInfo.getPackageBaseName();
        ModuleInfo generatorDao = generatorInfo.getGeneratorDao();

        String orm = generatorInfo.getOrm();
        String entityPackageName = generatorInfo.getEntityPackageName();

        // 如果是mybatis，还要生成mapper文件 TODO mybatis待优化生成注解文件
        if (BizConstant.ORM.MyBatis.equalsIgnoreCase(orm)) {
            String filePath = String.format("%s/mapper", packageBaseLocation);
            String packageName = String.format("%s,mapper", packageBaseName);
            if (StringUtils.isNotEmpty(generatorDao.getDetailPackageName())) {
                filePath = packageBaseLocation + generatorDao.getDetailPackageName();
                packageName = packageBaseName + generatorDao.getDetailPackageName().replaceAll("/", ".");
            }
            generatorInfo.setDaoPackageName(String.format("%s.%sMapper", packageName, StrUtil.line2Hump(dbInfo.getTableName(), true)));

            String finalDaoFilePath = filePath + fileName;
            if (!filePath.endsWith("/")) {
                finalDaoFilePath = String.format("%s/%s", filePath, fileName);
            }
            LogUtil.SYS.info("生成dao文件的路径为：{}", finalDaoFilePath);
            File mapperFile = new File(finalDaoFilePath);

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("columns", columnInfos);
            dataMap.put("packageName", packageName);
            dataMap.put("entityPackageName", entityPackageName);
            CommonGenerateHandler.generatorFileByTemplate("Mapper.ftl", mapperFile, dataMap, dbInfo, generatorInfo);
            LogUtil.SYS.info("生成dao文件完毕");
            CommonGenerateHandler.generatorMapperXML(dataMap, generatorDao.getMapperXMLPath(), dbInfo.getTableName(), dbInfo, generatorInfo);
        }
        if (BizConstant.ORM.JPA.equals(orm)) {
            fileName = String.format("%sRepository.java", StrUtil.line2Hump(dbInfo.getTableName(), true));
            String filePath = String.format("%s/repository", packageBaseLocation);
            String packageName = String.format("%s,repository", packageBaseName);
            if (StringUtils.isNotEmpty(generatorDao.getDetailPackageName())) {
                filePath = packageBaseLocation + generatorDao.getDetailPackageName();
                packageName = packageBaseName + generatorDao.getDetailPackageName().replaceAll("/", ".");
            }
            generatorInfo.setDaoPackageName(String.format("%s.%sMapper", packageName, StrUtil.line2Hump(dbInfo.getTableName(), true)));

            String finalDaoFilePath = filePath + fileName;
            if (!filePath.endsWith("/")) {
                finalDaoFilePath = String.format("%s/%s", filePath, fileName);
            }
            LogUtil.SYS.info("生成dao文件的路径为：{}", finalDaoFilePath);
            File repositoryFile = new File(finalDaoFilePath);

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("columns", columnInfos);
            dataMap.put("packageName", packageName);
            dataMap.put("entityPackageName", entityPackageName);
            CommonGenerateHandler.generatorFileByTemplate("Repository.ftl", repositoryFile, dataMap, dbInfo, generatorInfo);
            LogUtil.SYS.info("生成dao文件完毕");
        }
    }
}
