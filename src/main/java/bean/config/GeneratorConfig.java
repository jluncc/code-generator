package bean.config;

/**
 * 生成文件配置信息
 *
 * Created by jinglun on 2020-03-08
 */
public class GeneratorConfig {
    private String outPath;
    // 默认都为flase
    private boolean generatorEntity;
    private boolean generatorDao;
    private boolean generatorService;
    private boolean generatorController;

    public GeneratorConfig(String outPath) {
        this.outPath = outPath;
        this.generatorEntity = false;
        this.generatorDao = false;
        this.generatorService = false;
        this.generatorController = false;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public boolean isGeneratorEntity() {
        return generatorEntity;
    }

    public void setGeneratorEntity(boolean generatorEntity) {
        this.generatorEntity = generatorEntity;
    }

    public boolean isGeneratorDao() {
        return generatorDao;
    }

    public void setGeneratorDao(boolean generatorDao) {
        this.generatorDao = generatorDao;
    }

    public boolean isGeneratorService() {
        return generatorService;
    }

    public void setGeneratorService(boolean generatorService) {
        this.generatorService = generatorService;
    }

    public boolean isGeneratorController() {
        return generatorController;
    }

    public void setGeneratorController(boolean generatorController) {
        this.generatorController = generatorController;
    }
}
