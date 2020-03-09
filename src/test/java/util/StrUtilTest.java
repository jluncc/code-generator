package util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jinglun on 2020-03-09
 */
public class StrUtilTest {

    @Test
    public void line2Hump() {
        String test1 = "my_method";
        String test2 = "My_Method";
        String test3 = "my_1method";

        Assert.assertEquals(StrUtil.line2Hump(test1, true), "MyMethod");
        Assert.assertEquals(StrUtil.line2Hump(test1, false), "myMethod");

        Assert.assertEquals(StrUtil.line2Hump(test2, true), "MyMethod");
        Assert.assertEquals(StrUtil.line2Hump(test2, false), "myMethod");

        Assert.assertEquals(StrUtil.line2Hump(test3, true), "My1Method");
        Assert.assertEquals(StrUtil.line2Hump(test3, false), "my1Method");
    }
}