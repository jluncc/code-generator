package ${packageName};

import ${entityPackageName};
import ${servicePackageName};
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * ${tableComment} service层接口
 *
 * TODO 接口默认出参为ApiResult对象，使用者可能要替换该对象
 * ApiResult的详细内容可见源码的model.vo.ApiResult对象
 *
 * Created by ${author} on ${date}
 */
@RestController
@RequestMapping("${mappingUrl}")
public class ${tableName}Controller {
    @Resource
    private ${tableName}Service ${tableName?uncap_first}Service;

    @GetMapping("/${tableName?uncap_first}/get")
    public ApiResult get${tableName}ById(Integer id) {
        if (id == null) return ApiResult.fail("参数不符合要求");
        return ApiResult.success(${tableName?uncap_first}Service.get${tableName}ById(id));
    }

    @PostMapping("/${tableName?uncap_first}/list")
    public ApiResult list${tableName}ByEntity(@RequestBody ${tableName} ${tableName?uncap_first}) {
        if (${tableName?uncap_first} == null) return ApiResult.fail("参数不符合要求");
        return ApiResult.success(${tableName?uncap_first}Service.list${tableName}ByEntity(${tableName?uncap_first}));
    }

    @PostMapping("/${tableName?uncap_first}/create")
    public ApiResult create${tableName}(@RequestBody ${tableName} ${tableName?uncap_first}) {
        if (${tableName?uncap_first} == null) return ApiResult.fail("参数不符合要求");
        if (${tableName?uncap_first}Service.create${tableName}(${tableName?uncap_first}) > 0) {
            return ApiResult.success();
        }
        return ApiResult.fail();
    }

    @PostMapping("/${tableName?uncap_first}/update")
    public ApiResult update${tableName}(@RequestBody ${tableName} ${tableName?uncap_first}) {
        if (${tableName?uncap_first} == null) return ApiResult.fail("参数不符合要求");
        if (${tableName?uncap_first}Service.update${tableName}(${tableName?uncap_first}) > 0) {
            return ApiResult.success();
        }
        return ApiResult.fail();
    }

}
