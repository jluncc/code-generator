package util;

import constant.CodeConst;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;

/**
 * Created by jinglun on 2020-03-08
 */
public class FreeMarkerTemplateUtil {
    private Configuration configuration;

    public FreeMarkerTemplateUtil() {
        this.configuration = new Configuration(Configuration.VERSION_2_3_22);
        this.configuration.setClassForTemplateLoading(this.getClass(), "/ftl");
    }

    public Template getTemplate(String templateName) throws IOException {
        try {
            return this.configuration.getTemplate(templateName, CodeConst.UTF8);
        } catch (IOException e) {
            throw e;
        }
    }
}
