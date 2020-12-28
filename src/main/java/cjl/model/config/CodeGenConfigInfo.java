package cjl.model.config;

/**
 * 配置信息，为config.json文件所转换成的对象
 *
 * Created by jinglun on 2020-03-09
 */
public class CodeGenConfigInfo {
    /** orm框架：mybatis */
    private String orm;
    /** 数据库连接信息 */
    private DbInfo dbInfo;
    /** 生成文件配置信息 */
    private GeneratorInfo generatorInfo;
    /** 是否配置插件相应代码的配置信息 */
    private PluginInfo pluginInfo;

    public String getOrm() {
        return orm;
    }

    public void setOrm(String orm) {
        this.orm = orm;
    }

    public DbInfo getDbInfo() {
        return dbInfo;
    }

    public void setDbInfo(DbInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    public GeneratorInfo getGeneratorInfo() {
        return generatorInfo;
    }

    public void setGeneratorInfo(GeneratorInfo generatorInfo) {
        this.generatorInfo = generatorInfo;
    }

    public PluginInfo getPluginInfo() {
        return pluginInfo;
    }

    public void setPluginInfo(PluginInfo pluginInfo) {
        this.pluginInfo = pluginInfo;
    }
}
