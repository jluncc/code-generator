<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${packageName}.${tableName}Mapper">

    <resultMap id="${tableName}" type="${packageName}.${tableName}"/>

    <sql id="columns">
        <#if columns?exists><#list columns as column>${column.columnNameOrigin}<#if column_has_next>,</#if></#list></#if>
    </sql>

    <select id="get${tableName}ById" resultMap="${tableName}">
        select
            <include refid="columns"/>
        from ${tableNameOrigin}
        where id = ${r'#{id}'};
    </select>

    <insert id="create${tableName}" keyProperty="id" keyColumn="id" useGeneratedKeys="true">
        insert into ${tableNameOrigin}(<#if columns?exists><#list columns as column>column.columnNameOrigin<#if column_has_next>,</#if></#list></#if>)
        values (<#if columns?exists><#list columns as column>${r'#{'}${column.columnName?uncap_first}${r'}'}<#if column_has_next>,</#if></#list></#if>);
    </insert>

    <update id="update${tableName}">
        update ${tableNameOrigin}
        set
        <#if columns?exists><#list columns as column>
        <if test="${column.columnName?uncap_first} != null">${column.columnNameOrigin} = ${r'#{'}${column.columnName?uncap_first}${r'}'}<#if column_has_next>,</#if></if>
        </#list></#if>
        update_time = now()
        where id = ${r'#{id}'};
    </update>

</mapper>