package cjl.service.generateStrategy;

import cjl.constant.BizConstant;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class GenerateStrategyFactory {

    private Map<String, GenerateStrategyHandler> handlerMap;

    @Resource
    private CommonGenerateHandler commonGenerateHandler;
    @Resource
    private GenerteEntityHandler generteEntityHandler;

    @PostConstruct
    private void init() {
        handlerMap = new HashMap<>();
        handlerMap.put(BizConstant.GENERATE_STRATEGY.ENTITY, generteEntityHandler);
    }

    public GenerateStrategyHandler get(String generateType) {
        return handlerMap.containsKey(generateType) ? handlerMap.get(generateType) : commonGenerateHandler;
    }

}
