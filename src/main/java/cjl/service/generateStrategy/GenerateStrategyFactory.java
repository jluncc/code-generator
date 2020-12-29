package cjl.service.generateStrategy;

import cjl.constant.BizConstant;
import cjl.model.config.DbInfo;
import cjl.model.config.GeneratorInfo;
import cjl.model.config.PluginInfo;
import cjl.model.db.ColumnInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenerateStrategyFactory {

    private final GenerateStrategyHandler generateStrategyHandler;

    public GenerateStrategyFactory(String generateType) {
        switch (generateType) {
            case BizConstant.GENERATE_STRATEGY.ENTITY:
                this.generateStrategyHandler = new GenerteEntityHandler();
                break;
            // TODO 待实现
            case BizConstant.GENERATE_STRATEGY.DAO:
            case BizConstant.GENERATE_STRATEGY.SERVICE:
            case BizConstant.GENERATE_STRATEGY.CONTROLLER:
            default:
                this.generateStrategyHandler = new CommonGenerateHandler();
                break;
        }
    }

    public void generateFile(DbInfo dbInfo, GeneratorInfo generatorInfo, List<ColumnInfo> columnInfos, PluginInfo pluginInfo) {
        generateStrategyHandler.generateFile(dbInfo, generatorInfo, columnInfos, pluginInfo);
    }

}
