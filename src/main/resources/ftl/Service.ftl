package ${packageName};

import ${entityPackageName};
import ${daoPackageName};
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
<#if orm == 'jpa'>
import java.util.NoSuchElementException;
</#if>

/**
 * ${tableComment} service层接口
 *
 * Created by ${author} on ${date}
 */
@Service
public class ${tableName}Service {
    @Resource
    <#if orm == 'mybatis'>
    private ${tableName}Mapper ${tableName?uncap_first}Mapper;
    </#if>
    <#if orm == 'jpa'>
    private ${tableName}Repository ${tableName?uncap_first}Repository;
    </#if>

    public ${tableName} get${tableName}ById(Integer id) {
        if (id == null) return new ${tableName}();
        <#if orm == 'mybatis'>
        return ${tableName?uncap_first}Mapper.get${tableName}ById(id);
        </#if>
        <#if orm == 'jpa'>
        ${tableName} ${tableName?uncap_first} = null;
        try {
            ${tableName?uncap_first} = ${tableName?uncap_first}Repository.findById(id).get();
        } catch (NoSuchElementException e) {
            System.out.println("没有该数据，id:" + id);
        }
        return ${tableName?uncap_first};
        </#if>
    }

    <#if orm == 'mybatis'>
    public List<${tableName}> list${tableName}ByEntity(${tableName} ${tableName?uncap_first}) {
        if (${tableName?uncap_first} == null) return new ArrayList<>();
        return ${tableName?uncap_first}Mapper.list${tableName}ByEntity(${tableName?uncap_first});
    }

    public Integer create${tableName}(${tableName} ${tableName?uncap_first}) {
        if (${tableName?uncap_first} == null) return 0;
        return ${tableName?uncap_first}Mapper.create${tableName}(${tableName?uncap_first});
    }

    public Integer update${tableName}(${tableName} ${tableName?uncap_first}) {
        if (${tableName?uncap_first} == null) return 0;
        return ${tableName?uncap_first}Mapper.update${tableName}(${tableName?uncap_first});
    }
    </#if>
    <#if orm == 'jpa'>
    public Integer saveOrUpdate${tableName}(${tableName} ${tableName?uncap_first}) {
        if (${tableName?uncap_first} == null) return 0;
        try {
            ${tableName?uncap_first}Repository.save(${tableName?uncap_first});
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    </#if>

}