package util;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串操作的工具类
 *
 * Created by jinglun on 2020-03-08
 */
public class StrUtil {

    /**
     * 下划线转驼峰，并根据firstLetterUp情况，将首字母转成大写。如：
     *
     * firstLetterUp为true时，my_method -> MyMethod
     * firstLetterUp为false时，my_method -> myMethod
     *
     * 入参为空或其他异常情况返回空字符串。
     */
    public static String line2Hump(String str, Boolean firstLetterUp) {
        if (StringUtils.isEmpty(str)) return "";

        str = str.toLowerCase();
        StringBuilder result = new StringBuilder();
        boolean needUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            char letter = str.charAt(i);
            if (i == 0) {
                if (firstLetterUp && Character.isLowerCase(letter)) {
                    letter = Character.toUpperCase(letter);
                }
                result.append(letter);
                continue;
            }

            if (letter == '_') {
                needUpperCase = true;
                continue;
            }
            if (needUpperCase && Character.isLowerCase(letter)) {
                letter = Character.toUpperCase(letter);
                needUpperCase = false;
            }
            result.append(letter);
        }
        return result.toString();
    }
}
