package cjl.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * traceId工具类
 * <p>
 * Created by jinglun on 2021-02-22
 */
public class TraceIdUtil {
    /**
     * 生成由8位字符组成的随机字符串traceId
     */
    public static String buildTraceId() {
        return RandomStringUtils.randomAlphanumeric(8);
    }
}
