package model.config;

/**
 * 具体生成模块的配置信息
 *
 * Created by jinglun on 2020-03-09
 */
public class ModuleInfo {
    private boolean needGenerate;
    /** 放置文件的具体包名 */
    private String detailPackageName;
    /** service层中，是否是接口模式，若是接口模式，会生成相应接口和实现类，若非接口模式，只生成接口文件 */
    private boolean interfaceMode;
    /** controller层中，当前controller中的基本路径 */
    private String mappingUrl;

    public boolean isNeedGenerate() {
        return needGenerate;
    }

    public void setNeedGenerate(boolean needGenerate) {
        this.needGenerate = needGenerate;
    }

    public String getDetailPackageName() {
        return detailPackageName;
    }

    public void setDetailPackageName(String detailPackageName) {
        this.detailPackageName = detailPackageName;
    }

    public boolean isInterfaceMode() {
        return interfaceMode;
    }

    public void setInterfaceMode(boolean interfaceMode) {
        this.interfaceMode = interfaceMode;
    }

    public String getMappingUrl() {
        return mappingUrl;
    }

    public void setMappingUrl(String mappingUrl) {
        this.mappingUrl = mappingUrl;
    }
}
