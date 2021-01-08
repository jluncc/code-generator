package cjl.util;

import cjl.constant.BizConstant;
import cjl.model.config.CodeGenConfigInfo;
import cjl.model.config.DbInfo;
import cjl.model.config.GeneratorInfo;
import cjl.model.config.PluginInfo;
import cjl.model.db.ColumnInfo;
import cjl.service.generateStrategy.GenerateStrategyFactory;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 模板代码生成工具类
 * <p>
 * Created by jinglun on 2020-03-08
 */
public class CodeGeneratorUtil {

    private final CodeGenConfigInfo codeGenConfigInfo;

    public CodeGeneratorUtil(CodeGenConfigInfo codeGenConfigInfo) {
        this.codeGenConfigInfo = codeGenConfigInfo;
    }

    public void process() {
        if (!configEffective(codeGenConfigInfo)) {
            LogUtil.SYS.info("=== 配置信息不合法，请检查配置信息 ===");
            return;
        }

        DbInfo dbInfo = codeGenConfigInfo.getDbInfo();
        GeneratorInfo generatorInfo = codeGenConfigInfo.getGeneratorInfo();
        generatorInfo.setOrm(codeGenConfigInfo.getOrm());
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

            if (generatorInfo.getGeneratorEntity().isNeedGenerate()) {
                new GenerateStrategyFactory(BizConstant.GENERATE_STRATEGY.ENTITY).generateFile(dbInfo, generatorInfo, columnInfos, pluginInfo, null);
            }
            if (generatorInfo.getGeneratorDao().isNeedGenerate()) {
                new GenerateStrategyFactory(BizConstant.GENERATE_STRATEGY.DAO).generateFile(dbInfo, generatorInfo, columnInfos, pluginInfo, null);
            }
            if (generatorInfo.getGeneratorService().isNeedGenerate()) {
                new GenerateStrategyFactory(BizConstant.GENERATE_STRATEGY.SERVICE).generateFile(dbInfo, generatorInfo, columnInfos, pluginInfo, null);
            }
            if (generatorInfo.getGeneratorController().isNeedGenerate()) {
                new GenerateStrategyFactory(BizConstant.GENERATE_STRATEGY.CONTROLLER).generateFile(dbInfo, generatorInfo, columnInfos, pluginInfo, null);
            }
        } catch (Exception e) {
            LogUtil.SYS.error("处理文件生成出现异常。", e);
        }
    }

    /**
     * 校验参数是否合法：合法-true；不合法-false
     */
    private boolean configEffective(CodeGenConfigInfo codeGenConfigInfo) {
        if (StringUtils.isEmpty(codeGenConfigInfo.getOrm())) {
            LogUtil.SYS.info("=== 参数校验异常：orm框架不能为空！===");
            return false;
        }

        DbInfo dbInfo = codeGenConfigInfo.getDbInfo();
        if (dbInfo == null || StringUtils.isEmpty(dbInfo.getUrl()) || StringUtils.isEmpty(dbInfo.getUsername())
                || StringUtils.isEmpty(dbInfo.getPassword()) || StringUtils.isEmpty(dbInfo.getDriver())
                || StringUtils.isEmpty(dbInfo.getDbName()) || StringUtils.isEmpty(dbInfo.getTableName())) {
            LogUtil.SYS.info("=== 参数校验异常：数据库配置信息异常！===");
            return false;
        }

        GeneratorInfo generatorInfo = codeGenConfigInfo.getGeneratorInfo();
        if (generatorInfo == null || StringUtils.isEmpty(generatorInfo.getPackageBaseLocation())
                || StringUtils.isEmpty(generatorInfo.getPackageBaseName()) || generatorInfo.getGeneratorEntity() == null
                || generatorInfo.getGeneratorDao() == null || generatorInfo.getGeneratorService() == null
                || generatorInfo.getGeneratorController() == null) {
            LogUtil.SYS.info("=== 参数校验异常：生成文件配置信息异常！===");
            return false;
        }

        PluginInfo pluginInfo = codeGenConfigInfo.getPluginInfo();
        if (pluginInfo == null) {
            LogUtil.SYS.info("=== 参数校验异常：插件配置信息异常！===");
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

}
