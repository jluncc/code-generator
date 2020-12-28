package cjl;

import cjl.model.config.CodeGenConfigInfo;
import cjl.util.CodeGeneratorUtil;
import cjl.util.LogUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 启动入口
 *
 * Created by jinglun on 2020-03-08
 */
public class CodeGenerator {
    public static void main(String[] args) {
        InputStream stream = CodeGenerator.class.getResourceAsStream("../config.json");
        try {
            long start = System.currentTimeMillis();
            String json = IOUtils.toString(stream, StandardCharsets.UTF_8);
            LogUtil.SYS.info("读取配置文件--->完成");

            CodeGenConfigInfo codeGenConfigInfo = JSONObject.parseObject(json, CodeGenConfigInfo.class);
            LogUtil.SYS.info("转换配置文件--->完成");
            LogUtil.SYS.info("配置文件信息：{}", JSONObject.toJSONString(codeGenConfigInfo));

            LogUtil.SYS.info("=== 开始执行文件生成 ===");
            CodeGeneratorUtil codeGeneratorUtil = new CodeGeneratorUtil(codeGenConfigInfo);
            codeGeneratorUtil.process();
            Long costTime = System.currentTimeMillis() - start;
            LogUtil.SYS.info("=== 执行成功，耗时 {} 毫秒。请检查文件 ===", costTime);
        } catch (IOException e) {
            LogUtil.SYS.error("=== 执行失败！===", e);
        }
    }
}
