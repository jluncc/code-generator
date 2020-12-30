package cjl.service.generateStrategy;

import cjl.model.config.DbInfo;
import cjl.model.config.GeneratorInfo;
import cjl.model.config.PluginInfo;
import cjl.model.db.ColumnInfo;
import cjl.util.DateUtil;
import cjl.util.FreeMarkerTemplateUtil;
import cjl.util.LogUtil;
import cjl.util.StrUtil;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class CommonGenerateHandler implements GenerateStrategyHandler {
    @Override
    public void generateFile(DbInfo dbInfo, GeneratorInfo generatorInfo, List<ColumnInfo> columnInfos, PluginInfo pluginInfo, Map<Object, Object> params) {
    }

    public static void generatorFileByTemplate(String templateName, File file, Map<String, Object> dataMap,
                                        DbInfo dbInfo, GeneratorInfo generatorInfo) {
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
            LogUtil.SYS.error("生成文件出现异常。", e);
        }
    }

    public static void generatorMapperXML(Map<String, Object> dataMap, String mapperXMLPath, String tableName,
                                          DbInfo dbInfo, GeneratorInfo generatorInfo) {
        if (StringUtils.isEmpty(mapperXMLPath)) {
            LogUtil.SYS.info("mapperXMLPath不能为空");
            return;
        }
        String finalMapperXMLPath = mapperXMLPath + StrUtil.line2Hump(tableName, true) + "Mapper.xml";
        if (!mapperXMLPath.endsWith("/")) {
            finalMapperXMLPath = String.format("%s/%sMapper.xml", mapperXMLPath, StrUtil.line2Hump(tableName, true));
        }
        LogUtil.SYS.info("生成mapperXML文件的路径为：{}", finalMapperXMLPath);
        File mapperXMLFile = new File(finalMapperXMLPath);
        generatorFileByTemplate("MapperXML.ftl", mapperXMLFile, dataMap, dbInfo, generatorInfo);
        LogUtil.SYS.info("生成mapperXML文件完毕");
    }
}
