package cjl.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by jinglun on 2020-03-08
 */
public class FreeMarkerTemplateUtil {
    private final Configuration configuration;

    public FreeMarkerTemplateUtil() {
        this.configuration = new Configuration(Configuration.VERSION_2_3_22);
        this.configuration.setClassForTemplateLoading(this.getClass(), "/ftl");
    }

    public Template getTemplate(String templateName) throws IOException {
        return this.configuration.getTemplate(templateName, StandardCharsets.UTF_8.toString());
    }
}
