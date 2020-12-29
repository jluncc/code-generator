package ${packageName};

<#if pluginInfo.needLombok>
import lombok.Data;
</#if>
import java.util.Date;

/**
 * ${tableComment}实体类
 *
 * Created by ${author} on ${date}
 */
<#if pluginInfo.needLombok>
@Data
</#if>
public class ${tableName} {
<#if columns?exists>
    <#list columns as column>
        <#if (column.columnType = 'INT UNSIGNED' || column.columnType = 'INT'
        || column.columnType = 'SMALLINT' || column.columnType = 'BIT' || column.columnType = 'TINYINT' )>
    <#if column.columnComment??>/** ${column.columnComment} */</#if>
    private Integer ${column.columnName};
        <#elseif (column.columnType = 'DOUBLE' || column.columnType = 'FLOAT' || column.columnType = 'DECIMAL')>
    <#if column.columnComment??>/** ${column.columnComment} */</#if>
    private Double ${column.columnName};
        <#elseif (column.columnType = 'CHAR' || column.columnType = 'VARCHAR' || column.columnType = 'TEXT')>
    <#if column.columnComment??>/** ${column.columnComment} */</#if>
    private String ${column.columnName};
        <#elseif (column.columnType = 'TIMESTAMP' || column.columnType = 'DATE' || column.columnType = 'DATETIME')>
    <#if column.columnComment??>/** ${column.columnComment} */</#if>
    private Date ${column.columnName};
        <#else>
    // 暂没匹配该类型：${column.columnType}，自动赋类型为STRING，请手动匹配
    <#if column.columnComment??>/** ${column.columnComment} */</#if>
    // private String ${column.columnName};
        </#if>
    </#list>
</#if>

<#if !pluginInfo.needLombok>
<#if columns?exists>
    <#list columns as column>
        <#if (column.columnType = 'INT UNSIGNED' || column.columnType = 'INT'
        || column.columnType = 'SMALLINT' || column.columnType = 'BIT' || column.columnType = 'TINYINT' )>
    public Integer get${column.columnNameFirstLetterUp}() {
        return ${column.columnName};
    }

    public void set${column.columnNameFirstLetterUp}(Integer ${column.columnName}) {
        this.${column.columnName} = ${column.columnName};
    }

        <#elseif (column.columnType = 'DOUBLE' || column.columnType = 'FLOAT' || column.columnType = 'DECIMAL')>
    public Double get${column.columnNameFirstLetterUp}() {
        return ${column.columnName};
    }

    public void set${column.columnNameFirstLetterUp}(Double ${column.columnName}) {
        this.${column.columnName} = ${column.columnName};
    }

        <#elseif (column.columnType = 'CHAR' || column.columnType = 'VARCHAR' || column.columnType = 'TEXT')>
    public String get${column.columnNameFirstLetterUp}() {
        return ${column.columnName};
    }

    public void set${column.columnNameFirstLetterUp}(String ${column.columnName}) {
        this.${column.columnName} = ${column.columnName};
    }

        <#elseif (column.columnType = 'TIMESTAMP' || column.columnType = 'DATE' || column.columnType = 'DATETIME')>
    public Date get${column.columnNameFirstLetterUp}() {
        return ${column.columnName};
    }

    public void set${column.columnNameFirstLetterUp}(Date ${column.columnName}) {
        this.${column.columnName} = ${column.columnName};
    }

        <#else>
    // 暂没匹配该类型：${column.columnType}，自动赋类型为STRING，请手动匹配
    //public String get${column.columnNameFirstLetterUp}() {
    //    return ${column.columnName};
    //}

    //public void set${column.columnNameFirstLetterUp}(String ${column.columnName}) {
    //    this.${column.columnName} = ${column.columnName};
    //}

        </#if>
    </#list>
</#if>
</#if>
}