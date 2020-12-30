package cjl.service.generateStrategy;

import cjl.constant.BizConstant;
import cjl.model.config.DbInfo;
import cjl.model.config.GeneratorInfo;
import cjl.model.config.PluginInfo;
import cjl.model.db.ColumnInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GenerateStrategyFactory {

    private final GenerateStrategyHandler generateStrategyHandler;

    public GenerateStrategyFactory(String generateType) {
        switch (generateType) {
            case BizConstant.GENERATE_STRATEGY.ENTITY:
                this.generateStrategyHandler = new GenerateEntityHandler();
                break;
            case BizConstant.GENERATE_STRATEGY.DAO:
                this.generateStrategyHandler = new GenerateDaoHandler();
                break;
            case BizConstant.GENERATE_STRATEGY.SERVICE:
                this.generateStrategyHandler = new GenerateServiceHandler();
                break;
            case BizConstant.GENERATE_STRATEGY.CONTROLLER:
                this.generateStrategyHandler = new GenerateControllerHandler();
                break;
            default:
                this.generateStrategyHandler = new CommonGenerateHandler();
        }
    }

    public void generateFile(DbInfo dbInfo, GeneratorInfo generatorInfo, List<ColumnInfo> columnInfos, PluginInfo pluginInfo, Map<Object, Object> params) {
        generateStrategyHandler.generateFile(dbInfo, generatorInfo, columnInfos, pluginInfo, params);
    }

}
