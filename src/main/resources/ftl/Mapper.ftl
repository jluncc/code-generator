package ${packageName};

import ${entityPackageName};

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * ${tableComment} dao层接口
 *
 * Created by ${author} on ${date}
 */
@Mapper
public interface ${tableName}Mapper {

    ${tableName} get${tableName}ById(Integer id);

    List<${tableName}> list${tableName}ByEntity(${tableName} ${tableName?uncap_first});

    Integer create${tableName}(${tableName} ${tableName?uncap_first});

    Integer update${tableName}(${tableName} ${tableName?uncap_first});

}