package model.config;

/**
 * 生成文件的配置信息
 *
 * Created by jinglun on 2020-03-08
 */
public class GeneratorInfo {
    private String author;
    private String date;
    /** 项目存放的物理路径 */
    private String packageBaseLocation;
    /** 类的基本包名 */
    private String packageBaseName;
    // 需要生成代码的模块
    private ModuleInfo generatorEntity;
    private ModuleInfo generatorDao;
    private ModuleInfo generatorService;
    private ModuleInfo generatorController;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPackageBaseLocation() {
        return packageBaseLocation;
    }

    public void setPackageBaseLocation(String packageBaseLocation) {
        this.packageBaseLocation = packageBaseLocation;
    }

    public String getPackageBaseName() {
        return packageBaseName;
    }

    public void setPackageBaseName(String packageBaseName) {
        this.packageBaseName = packageBaseName;
    }

    public ModuleInfo getGeneratorEntity() {
        return generatorEntity;
    }

    public void setGeneratorEntity(ModuleInfo generatorEntity) {
        this.generatorEntity = generatorEntity;
    }

    public ModuleInfo getGeneratorDao() {
        return generatorDao;
    }

    public void setGeneratorDao(ModuleInfo generatorDao) {
        this.generatorDao = generatorDao;
    }

    public ModuleInfo getGeneratorService() {
        return generatorService;
    }

    public void setGeneratorService(ModuleInfo generatorService) {
        this.generatorService = generatorService;
    }

    public ModuleInfo getGeneratorController() {
        return generatorController;
    }

    public void setGeneratorController(ModuleInfo generatorController) {
        this.generatorController = generatorController;
    }
}
