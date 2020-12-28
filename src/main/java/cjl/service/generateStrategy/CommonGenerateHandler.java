package cjl.service.generateStrategy;

import cjl.model.config.DbInfo;
import cjl.model.config.GeneratorInfo;
import cjl.model.config.PluginInfo;
import cjl.model.db.ColumnInfo;
import cjl.util.DateUtil;
import cjl.util.FreeMarkerTemplateUtil;
import cjl.util.StrUtil;
import freemarker.template.Template;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class CommonGenerateHandler implements GenerateStrategyHandler {
    @Override
    public void generateFile(DbInfo dbInfo, GeneratorInfo generatorInfo, List<ColumnInfo> columnInfos, PluginInfo pluginInfo) {
        return;
    }

    public void generatorFileByTemplate(String templateName, File file, Map<String, Object> dataMap,
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
            e.printStackTrace();
        }
    }
}
