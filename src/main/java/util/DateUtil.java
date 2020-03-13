package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具类
 *
 * Created by jinglun on 2020-03-11
 */
public class DateUtil {

    /**
     * 获取当天日期的字符串，例如：2020-03-11
     */
    public static String getCurDateStr() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(date.format(formatter));
        return date.format(formatter);
    }

}
