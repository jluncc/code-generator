package model.db;

/**
 * 数据库表实体类
 *
 * Created by jinglun on 2020-03-08
 */
public class ColumnInfo {
    private String columnName;
    private String columnNameOrigin;
    private String columnNameFirstLetterUp;
    private String columnType;
    private String columnComment;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnNameOrigin() {
        return columnNameOrigin;
    }

    public void setColumnNameOrigin(String columnNameOrigin) {
        this.columnNameOrigin = columnNameOrigin;
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
