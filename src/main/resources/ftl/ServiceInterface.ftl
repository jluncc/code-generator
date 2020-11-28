package ${packageName};

import ${entityPackageName};

import java.util.List;

/**
* ${tableComment} service层接口
*
* Created by ${author} on ${date}
*/
public interface ${tableName}Service {

    ${tableName} get${tableName}ById(Integer id);

    List<${tableName}> list${tableName}ByEntity(${tableName} ${tableName?uncap_first});

    Integer create${tableName}(${tableName} ${tableName?uncap_first});

    Integer update${tableName}(${tableName} ${tableName?uncap_first});

}