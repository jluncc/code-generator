package cjl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jinglun on 2020-12-20
 */
public class LogUtil {
    /**
     * 获取sys的logger，指定打印到code-generator-sys-%d{yyyy-MM-dd}.log文件。见logback.xml
     */
    public static Logger SYS = LoggerFactory.getLogger("sys");

    /**
     * 获取sys-error的logger，指定打印到code-generator-err-%d{yyyy-MM-dd}.log文件。见logback.xml
     * 注意：该logger的level="ERROR"，只有打印error级别日志才会输出
     */
    public static Logger ERR = LoggerFactory.getLogger("sys-error");

}
