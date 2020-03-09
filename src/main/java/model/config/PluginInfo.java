package model.config;

/**
 * Created by jinglun on 2020-03-09
 */
public class PluginInfo {
    private boolean needSwagger;
    private boolean needLombok;

    public boolean isNeedSwagger() {
        return needSwagger;
    }

    public void setNeedSwagger(boolean needSwagger) {
        this.needSwagger = needSwagger;
    }

    public boolean isNeedLombok() {
        return needLombok;
    }

    public void setNeedLombok(boolean needLombok) {
        this.needLombok = needLombok;
    }
}
