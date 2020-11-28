package ${packageName};

import ${entityPackageName};
import ${daoPackageName};
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

/**
* ${tableComment} service层接口实现类
*
* Created by ${author} on ${date}
*/
@Service
public class ${tableName}ServiceImpl implements ${tableName}Service {
    @Resource
    private ${tableName}Mapper ${tableName?uncap_first}Mapper;

    @Override
    public ${tableName} get${tableName}ById(Integer id) {
        if (id == null) return new ${tableName}();
        return ${tableName?uncap_first}Mapper.get${tableName}ById(id);
    }

    @Override
    public List<${tableName}> list${tableName}ByEntity(${tableName} ${tableName?uncap_first}) {
        if (${tableName?uncap_first} == null) return new ArrayList<>();
        return ${tableName?uncap_first}Mapper.list${tableName}ByEntity(${tableName?uncap_first});
    }

    @Override
    public Integer create${tableName}(${tableName} ${tableName?uncap_first}) {
        if (${tableName?uncap_first} == null) return 0;
        return ${tableName?uncap_first}Mapper.create${tableName}(${tableName?uncap_first});
    }

    @Override
    public Integer update${tableName}(${tableName} ${tableName?uncap_first}) {
        if (${tableName?uncap_first} == null) return 0;
        return ${tableName?uncap_first}Mapper.update${tableName}(${tableName?uncap_first});
    }

}