package cjl.model.db;

/**
 * 数据库表信息实体类
 *
 * Created by jinglun on 2020-03-08
 */
public class ColumnInfo {
    /** 数据库表字段原名称 */
    private String columnNameOrigin;
    /** 数据库表字段名称驼峰格式（首字母小写） */
    private String columnName;
    /** 数据库表字段名称驼峰格式（首字母大写） */
    private String columnNameFirstLetterUp;
    /** 数据库表字段类型 */
    private String columnType;
    /** 数据库表字段注释 */
    private String columnComment;

    public String getColumnNameOrigin() {
        return columnNameOrigin;
    }

    public void setColumnNameOrigin(String columnNameOrigin) {
        this.columnNameOrigin = columnNameOrigin;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnNameFirstLetterUp() {
        return columnNameFirstLetterUp;
    }

    public void setColumnNameFirstLetterUp(String columnNameFirstLetterUp) {
        this.columnNameFirstLetterUp = columnNameFirstLetterUp;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }
}
