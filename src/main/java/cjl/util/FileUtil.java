package cjl.util;

import java.io.File;

/**
 * 文件工具类
 * <p>
 * Created by jinglun on 2021-02-08
 */
public class FileUtil {

    public static boolean mkdirs(File file) {
        if (!file.exists()) {
            if (!file.mkdirs()) {
                LogUtil.ERR.error("尝试创建文件夹失败，直接返回！文件夹路径：{}", file);
                return false;
            }
        }
        return true;
    }

}
