package ${packageName};

import ${entityPackageName};

import org.apache.ibatis.annotations.Mapper;

/**
 * ${tableComment} dao层接口
 *
 * Created by ${author} on ${date}
 */
@Mapper
public interface ${tableName}Mapper {

    ${tableName} get${tableName}ById(Integer id);

    Integer create${tableName}(${tableName} ${tableName?uncap_first});

    Integer update${tableName}(${tableName} ${tableName?uncap_first});

}