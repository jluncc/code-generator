package model.config;

/**
 * 具体生成模块的配置信息
 *
 * Created by jinglun on 2020-03-09
 */
public class ModuleInfo {
    private boolean needGenerate;
    private String detailPath;

    public boolean isNeedGenerate() {
        return needGenerate;
    }

    public void setNeedGenerate(boolean needGenerate) {
        this.needGenerate = needGenerate;
    }

    public String getDetailPath() {
        return detailPath;
    }

    public void setDetailPath(String detailPath) {
        this.detailPath = detailPath;
    }
}
