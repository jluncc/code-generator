import com.alibaba.fastjson.JSONObject;
import model.config.CodeGenConfigInfo;
import org.apache.commons.io.IOUtils;
import util.CodeGeneratorUtil;

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
        InputStream stream = CodeGenerator.class.getResourceAsStream("config.json");
        try {
            String json = IOUtils.toString(stream, StandardCharsets.UTF_8);
            System.out.println(json);

            CodeGenConfigInfo codeGenConfigInfo = JSONObject.parseObject(json, CodeGenConfigInfo.class);
            System.out.println(JSONObject.toJSONString(codeGenConfigInfo));

            CodeGeneratorUtil codeGeneratorUtil = new CodeGeneratorUtil(codeGenConfigInfo);
            codeGeneratorUtil.process();
            System.out.println("=== 执行成功，请检查。===");
        } catch (IOException e) {
            System.out.println("=== 执行失败！===");
            e.printStackTrace();
        }


    }
}
